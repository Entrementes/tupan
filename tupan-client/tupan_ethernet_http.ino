#include <SPI.h>
#include <Ethernet.h>
#include <EthernetUdp.h>
#include <ArduinoJson.h>
#include "EmonLib.h"

byte mac[] = {
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED
};

unsigned int ntpPort = 8888;

char timeServer[] = "time.nist.gov";

const int NTP_PACKET_SIZE = 48;

char TUPAN_SERVER[] = "45.55.161.149";

unsigned int TUPAN_PORT = 9999;

char USER_ID[] = "gunisalvo";
char UTILITIES_PROVIDER_ID[] = "INFNET";
char EQUIPMENT_ID[] = "BLANKA-001";
char MANUFACTURER[] = "Entrementes";
char FIRMWARE[] = "1.0.0";
char APPLIANCE_CATEGORY[] = "Smart Light Socket";

float FARE_THRESHOLD = 4.0;

byte packetBuffer[ NTP_PACKET_SIZE];

char httpBuffer[32]; // string for incoming serial data
int readPosition = 0; // string index counter
boolean httpRead = false; // is reading?

EthernetUDP Udp;

EnergyMonitor consumptionMonitor;

int RELAY_PIN = 7;

int ELECTRIC_CURRENT_MONITOR = 1;

int gridVoltage = 127;

unsigned long operationStartedIn = 0;
unsigned long lastUpdate = 0;
unsigned long nextUpdate = 0;

double totalConsumption = 0.0;

boolean needStartTimeUpdate = false;
boolean needUpdateConsumption = false;

EthernetClient client;

void setup()
{
  Serial.begin(9600);
  //Cur Const= Ratio/BurdenR. 1800/62 = 29.
  consumptionMonitor.current(ELECTRIC_CURRENT_MONITOR, 29);
  pinMode(RELAY_PIN, OUTPUT);
  while (!Serial) {
    ;
  }

  if (Ethernet.begin(mac) == 0) {
    Serial.println("Failed to configure Ethernet using DHCP");
    //DRAW ERROR
    for (;;)
      ;
  }
  Udp.begin(ntpPort);
  
  //register Device
  if(registerDevice()){
    Serial.println("device registred");
  }else{
    Serial.println("error connecting with server.");
  }
  deviceOn();
}

void deviceOn(){
  digitalWrite(RELAY_PIN, HIGH);
  Serial.print("The device is ");
  Serial.println(digitalRead(RELAY_PIN));
  needStartTimeUpdate = true;
  needUpdateConsumption = true; 
}

void deviceOff(const unsigned long serverTime){
  digitalWrite(RELAY_PIN, LOW);
  Serial.print("The device is ");
  Serial.println(digitalRead(RELAY_PIN));
  needUpdateConsumption = false;
  
  StaticJsonBuffer<250> jsonBuffer;
  
  JsonObject& registerRequestBody = jsonBuffer.createObject();
  registerRequestBody["userId"] = USER_ID;
  registerRequestBody["utlitiesProviderId"] = UTILITIES_PROVIDER_ID;
  registerRequestBody["equipamentId"] = EQUIPMENT_ID;
  registerRequestBody["electricalConsumption"] = totalConsumption;
  registerRequestBody["operationTime"] = serverTime - operationStartedIn;
  registerRequestBody["finished"] = true;
  
  if (client.connect(TUPAN_SERVER, TUPAN_PORT)) {
    Serial.println("sending consumption report...");
    
    JsonObject& registerRequestBody = jsonBuffer.createObject();
    registerRequestBody["userId"] = USER_ID;
    registerRequestBody["utlitiesProviderId"] = UTILITIES_PROVIDER_ID;
    registerRequestBody["equipamentId"] = EQUIPMENT_ID;
    registerRequestBody["electricalConsumption"] = totalConsumption;
    registerRequestBody["operationTime"] = serverTime - operationStartedIn;
    registerRequestBody["finished"] = true;
    
    
    String post = "POST /v1/grid/";
    post += UTILITIES_PROVIDER_ID;
    post += "/";
    post += USER_ID;
    post += "/";
    post += EQUIPMENT_ID;
    post += " HTTP/1.1";
    Serial.println(post);
    client.println(post);
    String host = "Host: ";
    host += TUPAN_SERVER;
    host += ":";
    host += TUPAN_PORT;
    Serial.println(host);
    client.println(host);
    Serial.println("Content-Type: application/json");
    client.println("Content-Type: application/json");
    Serial.println("Content-Length: 171");
    client.println("Content-Length: 171");
    Serial.println("Cache-Control: no-cache");
    client.println("Cache-Control: no-cache");
    Serial.println();
    client.println();
    registerRequestBody.printTo(Serial);
    registerRequestBody.printTo(client);
    Serial.println();
    client.println();
    Serial.println("Connection: close");
    client.println("Connection: close");
    client.println();
  }
  client.stop();
}

void loop()
{
  unsigned long serverTime = queryNTP();
  double Irms = consumptionMonitor.calcIrms(1480);
  updateTotalConsumption(Irms,serverTime);
  
  if(needStartTimeUpdate){
    Serial.println("Setting start time.");
    operationStartedIn = serverTime;
    needStartTimeUpdate = false;
  }
  if(serverTime > nextUpdate){
    String gridReport = queryGridState();
    Serial.println("Grid Report: " + gridReport);
    processGridState(gridReport, serverTime);
  }
  Serial.print("The device is ");
  Serial.println(digitalRead(RELAY_PIN));
  Serial.print("Electrical Current : ");
  Serial.println(Irms);
  Serial.print("Power Consumption (W) : ");
  Serial.println(Irms * gridVoltage);
  Serial.print("Total Consumption (KW/h): ");
  Serial.println(totalConsumption);
  delay(10000);
}

void updateTotalConsumption(double current, const unsigned long serverTime){
  
  long milliseconds = serverTime - lastUpdate;
  Serial.print("Opration time : ");
  Serial.println(serverTime - operationStartedIn);
  //[w/ms] to [Kw/h]
  //double correction = 0.000277;
  //totalConsumption =+ (( current * gridVoltage )  / milliseconds ) * correction;
  totalConsumption = totalConsumption + ( current * gridVoltage );
  lastUpdate = serverTime;
}

void processGridState(String gridReport, const unsigned long serverTime){
  char *jsonBody = new char[gridReport.length() + 1]; // or

  strcpy(jsonBody, gridReport.c_str());
  StaticJsonBuffer<250> jsonBuffer;
  JsonObject& root = jsonBuffer.parseObject(jsonBody);

  if (!root.success()) {
    Serial.println("parseObject() failed");
    return;
  }
  nextUpdate = root["nextUpdate"];
  const char* message = root["systemStateCode"];
  const char* fareCode = root["fareCode"];
  float electricalFare = root["electricalFare"];
  if(strcmp(fareCode,"RED") == 0){
    Serial.println("turning device of due to time of operation policy.");
    deviceOff(serverTime);
  }
  if(strcmp(message,"OK") != 0){
    Serial.println("turning device of due to electrical grid instability.");
    deviceOff(serverTime);
  }
  if(electricalFare > FARE_THRESHOLD){
    Serial.println("turning device of due to fare limit policy.");
    deviceOff(serverTime);
  }
}

String queryGridState(){
  String result = "";
  if (client.connect(TUPAN_SERVER, TUPAN_PORT)) {
    Serial.println("querying smart-grid state...");
    
    String getHeader = "GET /v1/grid/";
    getHeader += UTILITIES_PROVIDER_ID;
    getHeader += "/";
    getHeader += USER_ID;
    getHeader += " HTTP/1.1";
    Serial.println(getHeader);
    client.println(getHeader);
    String host = "Host: ";
    host += TUPAN_SERVER;
    host += ":";
    host += TUPAN_PORT;
    Serial.println(host);
    client.println(host);
    Serial.println("Accept: application/json");
    client.println("Accept: application/json");
    Serial.println("Connection: close");
    client.println("Connection: close");
    client.println();
    
    while(client.connected()) {
      while(client.available()) {
        client.find((char*)"HTTP/1.1 ");
        char statusCode[] = "000";
        client.readBytes(statusCode, 3);
        if(strcmp(statusCode, "404") == 0 || strcmp(statusCode, "400") == 0 || strcmp(statusCode, "500") == 0){
          client.stop();
        }
        client.find("{");
        result += "{";
        while(client.available()) {
          char inChar = client.read();
          result += inChar;
        }
      }
    }
    client.stop();
  }
  return result;
}

boolean registerDevice(){
  if (client.connect(TUPAN_SERVER, TUPAN_PORT)) {
    Serial.println("registering smart device...");
    
    StaticJsonBuffer<250> jsonBuffer;
  
    JsonObject& registerRequestBody = jsonBuffer.createObject();
    registerRequestBody["userId"] = USER_ID;
    registerRequestBody["utlitiesProviderId"] = UTILITIES_PROVIDER_ID;
    registerRequestBody["equipamentId"] = EQUIPMENT_ID;
    registerRequestBody["manufacturer"] = MANUFACTURER;
    registerRequestBody["applianceCategory"] = APPLIANCE_CATEGORY;
    registerRequestBody["firmware"] = FIRMWARE;
    
    String post = "POST /v1/grid/";
    post += UTILITIES_PROVIDER_ID;
    post += "/";
    post += USER_ID;
    post += " HTTP/1.1";
    Serial.println(post);
    client.println(post);
    String host = "Host: ";
    host += TUPAN_SERVER;
    host += ":";
    host += TUPAN_PORT;
    Serial.println(host);
    client.println(host);
    Serial.println("Content-Type: application/json");
    client.println("Content-Type: application/json");
    Serial.println("Content-Length: 171");
    client.println("Content-Length: 171");
    Serial.println("Cache-Control: no-cache");
    client.println("Cache-Control: no-cache");
    Serial.println();
    client.println();
    registerRequestBody.printTo(Serial);
    registerRequestBody.printTo(client);
    Serial.println();
    client.println();
    Serial.println("Connection: close");
    client.println("Connection: close");
    client.println();
    
    while(client.connected()) {
      while(client.available()) {
        client.find((char*)"HTTP/1.1 ");
        char statusCode[] = "000";
        client.readBytes(statusCode, 3);
        if(strcmp(statusCode, "201") == 0){
          client.stop();
          return true;
        }
      }
    }
  }
  client.stop();
  return false;
}

unsigned long queryNTP(){
  sendNTPpacket(timeServer); // send an NTP packet to a time server

  // wait to see if a reply is available
  delay(1000);
  unsigned long epoch = -1;
  if ( Udp.parsePacket() ) {
    // We've received a packet, read the data from it
    Udp.read(packetBuffer, NTP_PACKET_SIZE); // read the packet into the buffer

    //the timestamp starts at byte 40 of the received packet and is four bytes,
    // or two words, long. First, esxtract the two words:

    unsigned long highWord = word(packetBuffer[40], packetBuffer[41]);
    unsigned long lowWord = word(packetBuffer[42], packetBuffer[43]);
    // combine the four bytes (two words) into a long integer
    // this is NTP time (seconds since Jan 1 1900):
    unsigned long secsSince1900 = highWord << 16 | lowWord;
    //Serial.print("Seconds since Jan 1 1900 = " );
    //Serial.println(secsSince1900);

    // now convert NTP time into everyday time:
    //Serial.print("Unix time = ");
    // Unix time starts on Jan 1 1970. In seconds, that's 2208988800:
    const unsigned long seventyYears = 2208988800UL;
    // subtract seventy years:
    epoch = secsSince1900 - seventyYears;
    // print Unix time:
    //Serial.println(epoch);


    // print the hour, minute and second:
    Serial.print("The UTC time is ");       // UTC is the time at Greenwich Meridian (GMT)
    Serial.print((epoch  % 86400L) / 3600); // print the hour (86400 equals secs per day)
    Serial.print(':');
    if ( ((epoch % 3600) / 60) < 10 ) {
      // In the first 10 minutes of each hour, we'll want a leading '0'
      Serial.print('0');
    }
    Serial.print((epoch  % 3600) / 60); // print the minute (3600 equals secs per minute)
    Serial.print(':');
    if ( (epoch % 60) < 10 ) {
      // In the first 10 seconds of each minute, we'll want a leading '0'
      Serial.print('0');
    }
    Serial.println(epoch % 60); // print the second
  }
  return epoch;
}

// send an NTP request to the time server at the given address
unsigned long sendNTPpacket(char* address)
{
  // set all bytes in the buffer to 0
  memset(packetBuffer, 0, NTP_PACKET_SIZE);
  // Initialize values needed to form NTP request
  // (see URL above for details on the packets)
  packetBuffer[0] = 0b11100011;   // LI, Version, Mode
  packetBuffer[1] = 0;     // Stratum, or type of clock
  packetBuffer[2] = 6;     // Polling Interval
  packetBuffer[3] = 0xEC;  // Peer Clock Precision
  // 8 bytes of zero for Root Delay & Root Dispersion
  packetBuffer[12]  = 49;
  packetBuffer[13]  = 0x4E;
  packetBuffer[14]  = 49;
  packetBuffer[15]  = 52;

  // all NTP fields have been given values, now
  // you can send a packet requesting a timestamp:
  Udp.beginPacket(address, 123); //NTP requests are to port 123
  Udp.write(packetBuffer, NTP_PACKET_SIZE);
  Udp.endPacket();
}










