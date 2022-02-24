/*

Keypad Demo

*/

#define Password_Length 4


#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <BigNumbers_I2C.h>
#include <Keypad.h>


byte givenKey = 0;
byte x_pos = 0;
byte current_position = 0;

// Constants for rows and column sizes
const byte ROWS = 4;
const byte COLS = 3;

byte Data[Password_Length];
byte Master_Password[Password_Length] = {0,8,2,4};

int green_pin = 40;

// Keypad Matrix

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

void setup() {
  pinMode(green_pin, OUTPUT);
  lcd.clear();
  lcd.backlight();
  lcd.init();
  bigNum.begin();
  Serial.begin(9600);
 

  
}

void loop() {

//  givenKey = myKeypad.getKey();
  char givenKey2 = myKeypad.getKey();
  if(givenKey2)
    Serial.println(givenKey2);
  if(givenKey && givenKey != 65 && givenKey != 66 && givenKey != 67 && givenKey != 68 && givenKey != 35 && givenKey != 42 ) {
  //Serial.println(givenKey);
    givenKey -= 48;
    
    bigNum.displayLargeNumber(givenKey, x_pos, (byte)0);
    Data[current_position] = givenKey;

    x_pos += 4;
    
    current_position += 1;
    
    if(current_position == 4) {
      
      if(compare_passwords(Data)) {
          x_pos = 0;
          digitalWrite(green_pin, HIGH);
          lcd.clear();
          
      } else {

        digitalWrite(38, HIGH);
        
      }

      delay(1500);

      x_pos = 0;
      current_position = 0;
      
      lcd.clear();

      digitalWrite(green_pin, LOW);
      digitalWrite(38, LOW);
     
    }
  }
  
}

boolean compare_passwords(byte given_array[]) {
  
  for (int i = 0;  i < 4; i++){
    
    if(given_array[i] != Master_Password[i] ) {
      
      return false;
    
    }
  }

  return true;

}
