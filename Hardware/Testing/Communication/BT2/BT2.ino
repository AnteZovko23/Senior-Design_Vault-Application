#define ledPin 46
#include <SoftwareSerial.h>   
#include <Servo.h>                     
SoftwareSerial bt(10,11); 

int state = 0;
byte current_position = 90;
int servo_pin = 9;


Servo servo_instance;

void setup() {
  pinMode(ledPin, OUTPUT);
  digitalWrite(ledPin, LOW);
  bt.begin(9600); // Default communication rate of the Bluetooth module

  // Servo code
  servo_instance.attach(servo_pin);
  servo_instance.write(0);
  
}

void loop() {
  if(bt.available() > 0){ // Checks whether data is comming from the serial port
    state = bt.read(); // Reads the data from the serial port
 }

 if (state == '0') {
  digitalWrite(ledPin, LOW); // Turn LED OFF
  bt.println("LED: OFF"); // Send back, to the phone, the String "LED: ON"
  state = 0;
  servo_instance.write(0);
 }
 else if (state == '1') {
  digitalWrite(ledPin, HIGH);
  bt.println("LED: ON");;
  state = 0;
  servo_instance.write(90);
 } 
}
