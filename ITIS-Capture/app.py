from flask import Flask, request
import io
from predictor import capture_status

app = Flask(__name__)

@app.route('/', methods=['GET'])
def test_connection():
    return "Success"

@app.route('/predict', methods=['POST'])
def capture():
    ecg_image = request.files.get("image")
    return capture_status(io.BytesIO(ecg_image.read()))