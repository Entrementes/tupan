#include <SPI.h>
#include <Ethernet.h>
#include <EthernetUdp.h>
#include <ArduinoJson.h>
#include <SD.h>
#include "EmonLib.h"

byte mac[] = {
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED
};

unsigned int ntpPort = 8888;

char timeServer[] = "time.nist.gov";

const int NTP_PACKET_SIZE = 48;

//TUPAN CONFIGURATIONS
String USER_ID;
String UTILITIES_PROVIDER_ID;
String EQUIPMENT_ID;
String MANUFACTURER;
String FIRMWARE;
String APPLIANCE_CATEGORY;
String TUPAN_SERVER;
unsigned int TUPAN_PORT;
float FARE_THRESHOLD;

byte packetBuffer[ NTP_PACKET_SIZE];

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

void setup(void){
  Serial.println(F("STARTING SETUP..."));
  
  Serial.begin(9600);
  //Cur Const= Ratio/BurdenR. 1800/62 = 29.
  consumptionMonitor.current(ELECTRIC_CURRENT_MONITOR, 29);
  pinMode(RELAY_PIN, OUTPUT);
  while (!Serial) {
    ;
  }
  if (Ethernet.begin(mac) == 0) {
    Serial.println(F("Failed to configure Ethernet using DHCP"));
    //DRAW ERROR
    for (;;)
      ;
  }
  Udp.begin(ntpPort);

  Serial.print(F("Initializing SD card..."));
  pinMode(53, OUTPUT);
  if (!SD.begin(4)) {
    Serial.println(F("initialization failed!"));
    return;
  }
  Serial.println(F("initialization done."));
  
  //Log Configurations;
  initializeConfigurations();
  
  delay(1000);
  
  if(registerDevice()){
    Serial.println(F("device registred"));
  }else{
    Serial.println(F("error connecting with server."));
  }
  deviceOn();
  Serial.println(F("...SETUP DONE"));
}

void loop(void)
{
  unsigned long serverTime = queryNTP();
  double Irms = consumptionMonitor.calcIrms(1480);
  
  if(needUpdateConsumption){
    Serial.println(F("Updating consumption."));
    updateTotalConsumption(Irms,serverTime);
  }
  delay(1000);
  if(needStartTimeUpdate){
    Serial.println(F("Setting start time."));
    operationStartedIn = serverTime;
    needStartTimeUpdate = false;
  }
  delay(1000);
  if(serverTime > nextUpdate){
    String gridReport;
    //while(nextUpdate == 0){
      gridReport = tupanDoGetJson(UTILITIES_PROVIDER_ID + "/" + USER_ID);
    //}
    Serial.println("Grid Report: " + gridReport);
    if(!processGridState(gridReport) && digitalRead(RELAY_PIN) == 1){
      deviceOff(serverTime);
    }
  }
  
  Serial.print(F("The device is  [1 = ON / 0 = OFF]:  "));
  Serial.println(digitalRead(RELAY_PIN));
  Serial.print(F("Electrical Current : "));
  Serial.println(Irms);
  Serial.print(F("Power Consumption (W) : "));
  Serial.println(Irms * gridVoltage);
  Serial.print(F("Total Consumption (KW/h): "));
  Serial.println(totalConsumption);
  delay(10000);
}

void initializeConfigurations(void){
  File configFile = SD.open("tupan.cnf");
  if (configFile) {
    String jsonBody = "";
    Serial.println(F("parsing tupan.cnf content..."));
    while (configFile.available()) {
        char in = configFile.read();
        jsonBody += in;
    }
    configFile.close();
    char *jsonChar = new char[jsonBody.length() +1];
    strcpy(jsonChar,jsonBody.c_str());
    StaticJsonBuffer<500> jsonBuffer;
    JsonObject& loadedConfigurations = jsonBuffer.parseObject(jsonChar);
    if (!loadedConfigurations.success()) {
      Serial.println(F("parseObject() failed"));
      return;
    }
    USER_ID = loadedConfigurations["userId"];  
    UTILITIES_PROVIDER_ID = loadedConfigurations["utlitiesProviderId"];
    EQUIPMENT_ID = loadedConfigurations["equipamentId"];
    MANUFACTURER = loadedConfigurations["manufacturer"];
    FIRMWARE = loadedConfigurations["firmware"];
    APPLIANCE_CATEGORY = loadedConfigurations["applianceCategory"];
    TUPAN_SERVER = loadedConfigurations["serverUrl"];
    TUPAN_PORT = loadedConfigurations["serverPort"];
    FARE_THRESHOLD = loadedConfigurations["electricalFareThreshold"];
  } else {
    Serial.println("error opening tupan.cnf.");
  }
}


void deviceOn(void){
  digitalWrite(RELAY_PIN, HIGH);
  Serial.print(F("The device is [1 = ON / 0 = OFF]: "));
  Serial.println(digitalRead(RELAY_PIN));
  needStartTimeUpdate = true;
  needUpdateConsumption = true; 
}

void deviceOff(const unsigned long serverTime){
  digitalWrite(RELAY_PIN, LOW);
  Serial.print(F("The device is  [1 = ON / 0 = OFF]: "));
  Serial.println(digitalRead(RELAY_PIN));
  needUpdateConsumption = false;
  sendConsumptionReport(serverTime);
}

void sendConsumptionReport(const unsigned long serverTime){  
  Serial.println(F("sending consumption report..."));
  StaticJsonBuffer<250> jsonBuffer;
  Serial.println(F("summarizing report..."));
    
  JsonObject& consumptionReport = jsonBuffer.createObject();
    
  consumptionReport["userId"] = USER_ID.c_str();
  consumptionReport["utlitiesProviderId"] = UTILITIES_PROVIDER_ID.c_str();
  consumptionReport["equipamentId"] = EQUIPMENT_ID.c_str();
  consumptionReport["electricalConsumption"] = totalConsumption;
  consumptionReport["operationTime"] = serverTime - operationStartedIn;
  consumptionReport["finished"] = true;
    
  char contentBuffer[250];
    
  consumptionReport.printTo(contentBuffer, sizeof(contentBuffer));
    
  Serial.print(F("report size: "));
  Serial.println(strlen(contentBuffer));
    
  doTupanPost(UTILITIES_PROVIDER_ID+"/"+USER_ID+"/"+EQUIPMENT_ID, contentBuffer);
  
  Serial.println(F("report done"));
}

void updateTotalConsumption(double current, const unsigned long serverTime){
  
  long milliseconds = serverTime - lastUpdate;
  Serial.print(F("Opration time : "));
  Serial.println(serverTime - operationStartedIn);
  //[w/ms] to [Kw/h]
  //double correction = 0.000277;
  //totalConsumption =+ (( current * gridVoltage )  / milliseconds ) * correction;
  totalConsumption = totalConsumption + ( current * gridVoltage );
  lastUpdate = serverTime;
}

boolean processGridState(String gridReport){
  char *jsonBody = new char[gridReport.length() + 1]; // or

  strcpy(jsonBody, gridReport.c_str());
  StaticJsonBuffer<250> jsonBuffer;
  JsonObject& root = jsonBuffer.parseObject(jsonBody);

  if (!root.success()) {
    Serial.println(F("parseObject() failed"));
  }
  nextUpdate = root["nextUpdate"];
  const char* message = root["systemStateCode"];
  const char* fareCode = root["fareCode"];
  float electricalFare = root["electricalFare"];
  if(strcmp(fareCode,"RED") == 0){
    Serial.println(F("turning device of due to time of operation policy."));
    return false;
  }
  if(strcmp(message,"OK") != 0){
    Serial.println(F("turning device of due to electrical grid instability."));
    return false;
  }
  if(electricalFare > FARE_THRESHOLD){
    Serial.println(F("turning device of due to fare limit policy."));
    return false;
  }
  delete jsonBody;
  delete message;
  delete fareCode;
  return true;
}

boolean registerDevice(void){
  Serial.println(F("registering smart device..."));
    
  StaticJsonBuffer<250> jsonBuffer;
  
  JsonObject& registerRequestBody = jsonBuffer.createObject();
  registerRequestBody["userId"] = USER_ID.c_str();
  registerRequestBody["utlitiesProviderId"] = UTILITIES_PROVIDER_ID.c_str();
  registerRequestBody["equipamentId"] = EQUIPMENT_ID.c_str();
  registerRequestBody["manufacturer"] = MANUFACTURER.c_str();
  registerRequestBody["applianceCategory"] = APPLIANCE_CATEGORY.c_str();
  registerRequestBody["firmware"] = FIRMWARE.c_str();
    
  char contentBuffer[250];
  
  registerRequestBody.printTo(contentBuffer, sizeof(contentBuffer));
  
  String statusCode = doTupanPost(UTILITIES_PROVIDER_ID + "/" + USER_ID, contentBuffer);
  
  Serial.print(F("server returned: "));
  Serial.println(statusCode);
  
  if(strcmp(statusCode.c_str(), "201") == 0){
    return true;
  }else{
    return false;
  }
}

unsigned long queryNTP(void){
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
    Serial.print(F("The UTC time is "));       // UTC is the time at Greenwich Meridian (GMT)
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

String tupanDoGetJson(String path){
  String result = F("");
  if (client.connect(TUPAN_SERVER.c_str(), TUPAN_PORT)) {
    Serial.println(F("querying smart-grid state..."));
    delay(1000);
    
    String getHeader = F("GET /v1/grid/");
    getHeader += path;
    getHeader += " HTTP/1.1";
    webSerialPrintln(getHeader);
    String host = F("Host: ");
    host += TUPAN_SERVER;
    host += ":";
    host += TUPAN_PORT;
    webSerialPrintln(host);
    webSerialPrintln(F("Accept: application/json"));
    webSerialPrintln(F("Connection: close"));
    Serial.println();
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

String doTupanPost(String path, char *contentBuffer){
  String statusCode = "";
  Serial.print(F("POSTing "));
  Serial.println(path);
  if (client.connect(TUPAN_SERVER.c_str(), TUPAN_PORT)) {
    Serial.println(F("client connected"));
    delay(1000);
    String post = F("POST /v1/grid/");
    post += path;
    post += " HTTP/1.1";
    webSerialPrintln(post);
    String host = F("Host: ");
    host += TUPAN_SERVER;
    host += ":";
    host += TUPAN_PORT;
    webSerialPrintln(host);
    webSerialPrintln(F("Content-Type: application/json"));
    String contentLength = F("Content-Length: ");
    contentLength += ( strlen(contentBuffer) + 1 ) ;
    webSerialPrintln(contentLength);
    webSerialPrintln(F("Cache-Control: no-cache"));
    Serial.println();
    client.println();
    webSerialPrintln(contentBuffer);
    client.println(contentBuffer);
    Serial.println();
    client.println();
    webSerialPrintln(F("Connection: close"));
    Serial.println();
    client.println();
    char code[] = "000";
    while(client.connected()) {
      while(client.available()) {
        client.find((char*)"HTTP/1.1 ");
        code[0] = client.read();
        code[1] = client.read();
        code[2] = client.read();
        Serial.print(F("POST returned: "));
        Serial.println(code);
        client.stop();
        return code;
      }
    }
  }
  return statusCode;
}

void webSerialPrint(String content){
  Serial.print(content);
  client.print(content);
}

void webSerialPrintln(String content){
   Serial.println(content);
  client.println(content);
}
