#include <SoftwareSerial.h>                        
SoftwareSerial esp8266(11,10); 
               
#define serialCommunicationSpeed 115200               
#define DEBUG true                                 

void setup()

{
  esp8266.begin(9600); 
  Serial.begin(9600);  

//  Serial.begin(serialCommunicationSpeed);           
//  esp8266.begin(serialCommunicationSpeed);   
  sendData("AT+RST\r\n", 2000, DEBUG);                                
}

void loop() {
  // put your main code here, to run repeatedly:

}

String sendData(String command, const int timeout, boolean debug)
{
    String response = "";                                             
    esp8266.print(command);                                          
    long int time = millis();                                      
    while( (time+timeout) > millis())                                 
    {      
      while(esp8266.available())                                      
      {
        char c = esp8266.read();                                     
        response+=c;                                                  
      }  
    }    
    if(debug)                                                        
    {
      Serial.print(response);
    }    
    return response;                                                  
}
