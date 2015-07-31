/*
  SD card read/write

 This example shows how to read and write data to and from an SD card file
 The circuit:
 * SD card attached to SPI bus as follows:
 ** MOSI - pin 11
 ** MISO - pin 12
 ** CLK - pin 13
 ** CS - pin 4

 created   Nov 2010
 by David A. Mellis
 modified 9 Apr 2012
 by Tom Igoe

 This example code is in the public domain.

 */

#include <SPI.h>
#include <SD.h>
#include <ArduinoJson.h>

File myFile;

void setup()
{
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }


  Serial.print("Initializing SD card...");

  if (!SD.begin(4)) {
    Serial.println("initialization failed!");
    return;
  }
  Serial.println("initialization done.");

  // open the file. note that only one file can be open at a time,
  // so you have to close this one before opening another.
  if (SD.exists("tupan.cnf")) {
    SD.remove("tupan.cnf"); //We don't want to use the same file <<THIS IS THE BUG?
  }

  delay(10); //Make sure changes are applied

  myFile = SD.open("tupan.cnf", FILE_WRITE);

  StaticJsonBuffer<250> jsonBuffer;
  
  JsonObject& configurations = jsonBuffer.createObject();
  configurations["userId"] = "gunisalvo";
  configurations["utlitiesProviderId"] = "INFNET";
  configurations["equipamentId"] = "ETH-PL-HTTP-001";
  configurations["manufacturer"] = "Entrementes";
  configurations["firmware"] = "1.0.0";
  configurations["applianceCategory"] = "Smart Light Socket";
  configurations["electricalFareThreshold"] = 5.0;
  configurations["serverUrl"] = "45.55.161.149";
  configurations["serverPort"] = 9999;
  
  // if the file opened okay, write to it:
  if (myFile) {
    Serial.print("Writing to tupan.cnf...");
    configurations.printTo(myFile);
    // close the file:
    myFile.close();
    Serial.println("done.");
  } else {
    // if the file didn't open, print an error:
    Serial.println("error opening tupan.cnf");
  }

  // re-open the file for reading:
  myFile = SD.open("tupan.cnf");
  if (myFile) {
    Serial.println("tupan.cnf:");

    // read from the file until there's nothing else in it:
    while (myFile.available()) {
      Serial.write(myFile.read());
    }
    // close the file:
    myFile.close();
  } else {
    // if the file didn't open, print an error:
    Serial.println("error opening tupan.cnf");
  }
}

void loop()
{
  // nothing happens after setup
}


