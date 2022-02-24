### 
# Author: Ante Zovko
# Version: February 2022
# Description: Test camera using OpenCV
###
import cv2

# Capture video from camera
camera = cv2.VideoCapture(0)

while True:
    
    # Read the frame
    success, image = camera.read()
    
    # Resize the image if needed
    # smaller_frame = cv2.resize(image, (0, 0), fx=0.5, fy=0.5)
    
    # If not success, try again
    if not success:
        continue
    
    # Show the frame
    cv2.imshow("Frame", image)
    
    # Exit if q is pressed
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break
    
# Release the camera
camera.release()