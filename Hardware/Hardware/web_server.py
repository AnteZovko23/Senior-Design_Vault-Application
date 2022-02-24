from flask import Flask, Response, request
from flask_cors import CORS, cross_origin
import requests
import argparse
import image_processing
import time
import face_recognition
import cv2
import numpy as np
import os

###
# Author: Ante Zovko
# Version: February 2022
# Description: A web server that takes a frame from a camera and sends it to an html page
###

app = Flask(__name__)
parser = argparse.ArgumentParser()

parser.add_argument('--ip', type=str, default='localhost', help='ip of the camera')
parser.add_argument('--port', type=int, default=5000, help='port of the camera')


CORS(app, support_credentials=True)
app.config['CORS_HEADERS'] = 'Content-Type'

frame = None

#Loop to add images in friends folder
# Save 
#Store objects in array
known_person=[] #Name of person string
known_image=[] #Image object
known_face_encodings=[] #Encoding object

# Print how long it took to process the frame
# Initialize some variables
face_locations = []
face_encodings = []
face_names = []
process_this_frame = True

for file in os.listdir("img"):
    # try:
        #Extracting person name from the image filename eg: david.jpg
        start = time.time()
        
        known_person.append(file.replace(".jpg", ""))
        file=os.path.join("img/", file)
        known_image = face_recognition.load_image_file(file)
        #print(face_recognition.face_encodings(known_image)[0])
        known_face_encodings.append(face_recognition.face_encodings(known_image)[0])
        #print(known_face_encodings)

@app.route('/video_feed', methods=['POST'])
@cross_origin(origin='*')
def video_frames():
        
    global frame
    
    # Get the byte array from the request
    frame = request.data
    
    # Time how long it takes to process the frame

    # Send to image_processing for processing
    frame = process_image(frame)
    
    
    
    # Return success
    return "OK"

# Update frames for rendering
def render():
    while True:
        global frame
        yield (b'--frame\r\n'b'Content-Type: image/jpg\r\n\r\n' + frame + b'\r\n\r\n')
    


@app.route('/video_stream')
@cross_origin(origin='*')
def video_feed():
    global frame
    # Keep rendering based on the frame forever
    return Response(render(), mimetype='multipart/x-mixed-replace; boundary=frame')

@app.route('/')
@cross_origin(origin='*')
def success():
    return "Successfully connected to main server!"


@app.route('/start_feed')
@cross_origin(origin="*")
def start_feed():
    # Get Request
    requests.get('https://{}:{}/start_feed'.format(parser.parse_args().ip, parser.parse_args().port), verify=False).content

    return "OK"


@app.route('/get_test1')
@cross_origin(origin="*")
def get_test1():
    # Get Request
    
    return requests.get('https://{}:{}/'.format(parser.parse_args().ip, parser.parse_args().port), verify=False).content

    

@app.route('/get_json1')
@cross_origin(origin="*")
def get_json1():
    # Get Request
    return requests.get('https://{}:{}/test'.format(parser.parse_args().ip, parser.parse_args().port), verify=False).content


def process_image(frame):
    global process_this_frame, face_distances, face_locations, face_encodings, face_names, known_face_encodings, face_locations, face_encodings, face_names
    frame = np.asarray(bytearray(frame), dtype="uint8")
    # Convert given byte array to image
    frame = cv2.imdecode(frame, cv2.IMREAD_COLOR)

        # except Exception as e:
        #     print(e)
        #     pass
        
    #print(len(known_face_encodings))
    #print(known_person)


    # while True:
    # Grab a single frame of video
    # ret, frame = video_capture.read()

    # Resize frame of video to 1/4 size for faster face recognition processing
    small_frame = cv2.resize(frame, (0, 0), fx=0.25, fy=0.25)

    # Convert the image from BGR color (which OpenCV uses) to RGB color (which face_recognition uses)
    rgb_small_frame = small_frame[:, :, ::-1]

    # Only process every other frame of video to save time
    # if process_this_frame:
    # Find all the faces and face encodings in the current frame of video
    face_locations = face_recognition.face_locations(rgb_small_frame)
    face_encodings = face_recognition.face_encodings(rgb_small_frame, face_locations)

    global name_gui;
    #face_names = []
    for face_encoding in face_encodings:
        # See if the face is a match for the known face(s)
        matches = face_recognition.compare_faces(known_face_encodings, face_encoding)
        name = "Unknown"
        
        #print(face_encoding)
        #print(matches)

        face_distances = face_recognition.face_distance(known_face_encodings, face_encoding)
        best_match_index = np.argmin(face_distances)
        if matches[best_match_index]:
            name = known_person[best_match_index]

        #print(name)
        #print(face_locations)
        face_names.append(name)

        name_gui = name

    process_this_frame = not process_this_frame


    # Display the results
    for (top, right, bottom, left), name in zip(face_locations, face_names):
        # Scale back up face locations since the frame we detected in was scaled to 1/4 size
        top *= 4
        right *= 4
        bottom *= 4
        left *= 4

        # Draw a box around the face
        cv2.rectangle(frame, (left, top), (right, bottom), (255, 255, 255), 2)

        # Draw a label with a name below the face
        cv2.rectangle(frame, (left, bottom - 35), (right, bottom), (255, 255, 255), cv2.FILLED)
        font = cv2.FONT_HERSHEY_DUPLEX
        cv2.putText(frame, name_gui, (left + 10, bottom - 10), font, 1.0, (0, 0, 0), 1)


    # Convert back to byte array
    frame = cv2.imencode('.jpg', frame)[1].tobytes()

    return frame

    #     # Display the resulting image
    #     cv2.imshow('Video', frame)

    #     # Hit 'q' on the keyboard to quit!
    #     if cv2.waitKey(1) & 0xFF == ord('q'):
    #         break

    # # Release handle to the webcam
    # video_capture.release()
    # cv2.destroyAllWindows()



if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, ssl_context=('server.crt', 'server.key'))
    
