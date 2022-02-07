#define ledPin 46

#include <SoftwareSerial.h>                        
SoftwareSerial bt(10,11); 


int state = 0;
char c = ' ';
void setup() {


  
  pinMode(ledPin, OUTPUT);
  digitalWrite(ledPin, LOW);
  Serial.begin(9600); // Default communication rate of the Bluetooth module
  Serial.print("up");
   bt.begin(38400);
}

void loop() {
  if(bt.available()){ // Checks whether data is comming from the serial port
    c = bt.read(); // Reads the data from the serial port
    Serial.write(c);
 }

 if(Serial.available()) {

    c= Serial.read();
  
 }
//
// if (state == '0') {
//  digitalWrite(ledPin, LOW); // Turn LED OFF
//  Serial.println("LED: OFF"); // Send back, to the phone, the String "LED: ON"
//  state = 0;
// }
// else if (state == '1') {
//  digitalWrite(ledPin, HIGH);
//  Serial.println("LED: ON");;
//  state = 0;
 } 
}
