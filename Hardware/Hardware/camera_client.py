import cv2
import threading
import time
import cv2
import requests
import argparse

###
# Author: Ante Zovko
# Version: February 2022
# Description: Using OpenCV and Python, this script will send a video frame using a POST request to a Flask server
###

# Command line argument for the ip address of the Flask server
parser = argparse.ArgumentParser()

parser.add_argument('--ip', type=str, default='localhost', help='ip of the server')
parser.add_argument('--port', type=int, default=5000, help='port of the server')

# Get the arguments
ip = parser.parse_args().ip
port = parser.parse_args().port

outputFrame = None
lock = threading.Lock()

# Sleep for a little so warmup
video = cv2.VideoCapture(0)
time.sleep(2.0)


# Go forever
while True:
    # Create separate thread for sending frames
	with lock:
		
		# Get frame from camera
		success, image = video.read()
		
        # If not success, try again
		if not success:
			continue
		
		# Some overhead
		content_type = 'image/jpg'
		headers = {'content-type': content_type}
  
		# This prepares the image to be sent
		ret, encoded = cv2.imencode('.jpg', image)

		
		# Send the frame to the sever endpoint with a POST request as a byte array
		r2 = requests.post('http://{}:{}/video_feed'.format(ip, port), data=encoded.tobytes(), headers=headers)



