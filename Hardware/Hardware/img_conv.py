import cv2

# Get image
img = cv2.imread('img/Ante.jpg')

# Convert to byte array
ret, jpeg = cv2.imencode('.jpg', img)

# Convert to image
jpeg = cv2.imdecode(jpeg, cv2.IMREAD_COLOR)

# Save
cv2.imwrite('img/Ante.jpg', jpeg)