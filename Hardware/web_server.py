from flask import Flask, Response, request
from flask_cors import CORS, cross_origin

###
# Author: Ante Zovko
# Version: February 2022
# Description: A web server that takes a frame from a camera and sends it to an html page
###

app = Flask(__name__)


CORS(app, support_credentials=True)
app.config['CORS_HEADERS'] = 'Content-Type'

frame = None

# Post endpoint that receives a byte array
@app.route('/video_feed', methods=['POST'])
@cross_origin(origin='*')
def video_frames():
        
    global frame
    
    # Get the byte array from the request
    frame = request.data
    
    # Return success
    return "OK"

# Update frames for rendering
def render():
    while True:
        global frame
        yield (b'--frame\r\n'b'Content-Type: image/jpg\r\n\r\n' + frame + b'\r\n\r\n')
    
    
@app.route('/')
@cross_origin(origin='*')
def success():
    return "Success"

@app.route('/get_video_frames')
@cross_origin(origin='*')
def get_video_frames():
    # Return the frame as a response
    global frame
    return frame

@app.route('/video_stream')
@cross_origin(origin='*')
def video_feed():
    global frame
    # Keep rendering based on the frame forever
    return Response(render(), mimetype='multipart/x-mixed-replace; boundary=frame')
    


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)