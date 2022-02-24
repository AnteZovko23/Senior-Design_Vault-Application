#include <Keypad.h>

// Constants for rows and column sizes
const byte ROWS = 4;
const byte COLS = 3;

// Arduino pins
byte rowPins[ROWS] = {22, 23, 24, 25};
byte colPins[COLS] = {26, 27, 28};
char keys[ROWS][COLS] = {

  {'1', '2', '3'},
  {'4', '5', '6'},
  {'7', '8', '9'},
  {'*', '0', '#'}
  
};

Keypad customKeypad = Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);

void setup() {


  Serial.begin(9600);
}

void loop() {

  char customKey = customKeypad.getKey();

  if(customKey) {

    Serial.println(customKey);
    
  }
  
}
