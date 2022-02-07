


#define Password_Length 4

#include <SPI.h>
#include <MFRC522.h>
#include <Servo.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <BigNumbers_I2C.h>
#include <Keypad.h>
#include <Adafruit_Fingerprint.h>
#if (defined(__AVR__) || defined(ESP8266)) && !defined(__AVR_ATmega2560__)
// For UNO and others without hardware serial, we must use software serial...
// pin #2 is IN from sensor (GREEN wire)
// pin #3 is OUT from arduino  (WHITE wire)
// Set up the serial port to use softwareserial..
SoftwareSerial mySerial(19,18);

#else
// On Leonardo/M0/etc, others with hardware serial, use hardware serial!
// #0 is green wire, #1 is white
#define mySerial Serial1

#endif


byte givenKey = 0;
byte x_pos = 0;
byte current_position = 0;

// Constants for rows and column sizes
const byte ROWS = 4;
const byte COLS = 3;

byte Data[Password_Length];
byte Master_Password[Password_Length] = {0, 8, 2, 4};
byte Arm_Password[Password_Length] = {0, 9, 1, 8};

int buzzer_pin = 42;

int red_color_pin = 38;
int yellow_color_pin = 39;
int green_color_pin = 40;

int SS_pin = 53;
int RST_pin = 5;

// Keypad Matrix

//char keys[ROWS][COLS] = {
//
//  {'1', '2', '3', 'A'},
//  {'4', '5', '6', 'B'},
//  {'7', '8', '9', 'C'},
//  {'*', '0', '#', 'D'}
//  
//};

char keys[ROWS][COLS] = {

  {'1', '2', '3'},
  {'4', '5', '6'},
  {'7', '8', '9'},
  {'*', '0', '#'}
  
};


// Arduino pins
byte rowPins[ROWS] = {22, 23, 24, 25};
byte colPins[COLS] = {26, 27, 28};

// Keypad Object
Keypad myKeypad = Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);
LiquidCrystal_I2C lcd(0x27, 16, 2);
BigNumbers_I2C bigNum(&lcd);
MFRC522 mfrc522(SS_pin, RST_pin); // RFID
Servo servo_instance;
Adafruit_Fingerprint finger = Adafruit_Fingerprint(&mySerial);

void start_buzzer(unsigned int freq, unsigned int delayms){

  pinMode(buzzer_pin, OUTPUT);
  tone(buzzer_pin, freq, delayms);
  delay(delayms);
  pinMode(buzzer_pin, 0);
  
}

void reset_lights() {

  digitalWrite(red_color_pin, LOW);
  digitalWrite(yellow_color_pin, HIGH);
  digitalWrite(green_color_pin, LOW);
  
}

void turn_on_lights() {

  digitalWrite(red_color_pin, HIGH);
  digitalWrite(yellow_color_pin, HIGH);
  digitalWrite(green_color_pin, HIGH);
  
}

void turn_off_lights() {

  digitalWrite(red_color_pin, LOW);
  digitalWrite(yellow_color_pin, LOW);
  digitalWrite(green_color_pin, LOW);
  
}

void access_granted() {

  digitalWrite(red_color_pin, LOW);
  digitalWrite(yellow_color_pin, LOW);
  digitalWrite(green_color_pin, HIGH);

  start_buzzer(3000, 1000);
//  servo_instance.write(70);
//  delay(2000);
  servo_instance.write(185);
  // Reset Lights
  reset_lights();
  
}

void access_denied() {

  digitalWrite(red_color_pin, HIGH);
  digitalWrite(yellow_color_pin, LOW);
  digitalWrite(green_color_pin, LOW);

  start_buzzer(4000, 1500);  
  // Reset Lights
  reset_lights();  
  
}

void arm_lock() {
  
  turn_off_lights();
  
  digitalWrite(red_color_pin, HIGH);
  delay(1200);
  digitalWrite(yellow_color_pin, HIGH);
  delay(1200);
  digitalWrite(green_color_pin, HIGH);
  delay(1200);

  start_buzzer(1000, 200);
  
  turn_off_lights();

  delay(1000);

  reset_lights();

  servo_instance.write(90);

  
  
  
  
}

boolean compare_passwords(byte given_array[], byte stored_password[]) {
  
  for (int i = 0;  i < 4; i++){
    
    if(given_array[i] != stored_password[i] ) {
      
      return false;
    
    }
  }

  return true;

}



uint8_t getFingerprintID() {
  uint8_t p = finger.getImage();
  switch (p) {
    case FINGERPRINT_OK:
      Serial.println("Image taken");
      break;
    case FINGERPRINT_NOFINGER:
      Serial.println("No finger detected");
      return p;
    case FINGERPRINT_PACKETRECIEVEERR:
      Serial.println("Communication error");
      return p;
    case FINGERPRINT_IMAGEFAIL:
      Serial.println("Imaging error");
      return p;
    default:
      Serial.println("Unknown error");
      return p;
  }

  // OK success!

  p = finger.image2Tz();
  switch (p) {
    case FINGERPRINT_OK:
      Serial.println("Image converted");
      break;
    case FINGERPRINT_IMAGEMESS:
      Serial.println("Image too messy");
      return p;
    case FINGERPRINT_PACKETRECIEVEERR:
      Serial.println("Communication error");
      return p;
    case FINGERPRINT_FEATUREFAIL:
      Serial.println("Could not find fingerprint features");
      return p;
    case FINGERPRINT_INVALIDIMAGE:
      Serial.println("Could not find fingerprint features");
      return p;
    default:
      Serial.println("Unknown error");
      return p;
  }

  // OK converted!
  p = finger.fingerSearch();
  if (p == FINGERPRINT_OK) {
    Serial.println("Found a print match!");
    access_granted();
  } else if (p == FINGERPRINT_PACKETRECIEVEERR) {
    Serial.println("Communication error");
    return p;
  } else if (p == FINGERPRINT_NOTFOUND) {
    Serial.println("Did not find a match");
    access_denied();
    return p;
  } else {
    Serial.println("Unknown error");
    return p;
  }

  // found a match!
  Serial.print("Found ID #"); Serial.print(finger.fingerID);
  Serial.print(" with confidence of "); Serial.println(finger.confidence);

  return finger.fingerID;
}

// returns -1 if failed, otherwise returns ID #
int getFingerprintIDez() {
  uint8_t p = finger.getImage();
  if (p != FINGERPRINT_OK)  return -1;

  p = finger.image2Tz();
  if (p != FINGERPRINT_OK)  return -1;

  p = finger.fingerFastSearch();
  if (p != FINGERPRINT_OK)  return -1;

  // found a match!
  Serial.print("Found ID #"); Serial.print(finger.fingerID);
  Serial.print(" with confidence of "); Serial.println(finger.confidence);
  return finger.fingerID;
}



void setup()
{
  Serial.begin(9600);
  while (!Serial);  // For Yun/Leo/Micro/Zero/...
  delay(100);
  Serial.println("\n\nAdafruit finger detect test");

  // set the data rate for the sensor serial port
  finger.begin(57600);
  delay(5);
  if (finger.verifyPassword()) {
    Serial.println("Found fingerprint sensor!");
  } else {
    Serial.println("Did not find fingerprint sensor :(");
    while (1) { delay(1); }
  }

  Serial.println(F("Reading sensor parameters"));
  finger.getParameters();
  Serial.print(F("Status: 0x")); Serial.println(finger.status_reg, HEX);
  Serial.print(F("Sys ID: 0x")); Serial.println(finger.system_id, HEX);
  Serial.print(F("Capacity: ")); Serial.println(finger.capacity);
  Serial.print(F("Security level: ")); Serial.println(finger.security_level);
  Serial.print(F("Device address: ")); Serial.println(finger.device_addr, HEX);
  Serial.print(F("Packet len: ")); Serial.println(finger.packet_len);
  Serial.print(F("Baud rate: ")); Serial.println(finger.baud_rate);

  finger.getTemplateCount();

  if (finger.templateCount == 0) {
    Serial.print("Sensor doesn't contain any fingerprint data. Please run the 'enroll' example.");
  }
  else {
    Serial.println("Waiting for valid finger...");
      Serial.print("Sensor contains "); Serial.print(finger.templateCount); Serial.println(" templates");
  }

    SPI.begin();

  lcd.clear();
  lcd.backlight();
  lcd.init();
  bigNum.begin();

  mfrc522.PCD_Init(); // initiate
  // set the data rate for the sensor serial port

  // Servo code
  servo_instance.attach(9);
  servo_instance.write(90);
  
  pinMode(red_color_pin, OUTPUT);
  pinMode(yellow_color_pin, OUTPUT);
  pinMode(green_color_pin, OUTPUT);

  digitalWrite(red_color_pin, HIGH);
  delay(1200);
  digitalWrite(yellow_color_pin, HIGH);
  delay(1200);
  digitalWrite(green_color_pin, HIGH);
  delay(1200);

  reset_lights();

  Serial.println("Reading RFID...");

  
  
}

void loop()                     // run over and over again
{

  
  getFingerprintID();
    // Select Card
  if(mfrc522.PICC_IsNewCardPresent()) {

  if(!mfrc522.PICC_ReadCardSerial())
    return;

  // UID Display
  Serial.print("UID tag: ");
  String content = "";
  byte letter;

  for (byte i = 0; i < mfrc522.uid.size; i++) {

    Serial.print(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " ");
    Serial.print(mfrc522.uid.uidByte[i], HEX);
    content.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " "));
    content.concat(String(mfrc522.uid.uidByte[i], HEX));
  }

  Serial.println();
  Serial.print("Message: ");
  content.toUpperCase();

  

  if(content.substring(1) == "73 A3 0A 16" ) {

    Serial.println("Authorized Access");
    Serial.println();
    delay(500);
    access_granted();
    
  }

  else {

    Serial.println("Access Denied");
    access_denied();
      
    }
  }


  givenKey = myKeypad.getKey();
  //Serial.println(givenKey);
  if(givenKey && givenKey != 65 && givenKey != 66 && givenKey != 67 && givenKey != 68 && givenKey != 35 && givenKey != 42 ) {
    
    givenKey -= 48;
    
    bigNum.displayLargeNumber(givenKey, x_pos, (byte)0);
    Data[current_position] = givenKey;

    x_pos += 4;
    
    current_position += 1;
    
    if(current_position == 4) {
      
      if(compare_passwords(Data, Arm_Password)) {

           arm_lock();
          
      } else if(compare_passwords(Data, Master_Password)) {

           access_granted(); 
           
      } else {

        access_denied();
        
      }
      
      x_pos = 0;
      current_position = 0;
      lcd.clear();

   
     
    }


  
  
    
}
  delay(50);            //don't ned to run this at full speed.
}
