from flask import Flask, request
import io
from predictor import MODEL, load_trained_model, capture_status

app = Flask(__name__)


@app.before_first_request
def my_func():
    load_trained_model()


@app.route('/', methods=['GET'])
def test_connection():
    return "Success"


@app.route('/predict', methods=['POST'])
def capture():
    ecg_image = request.files.get("image")
    return capture_status(io.BytesIO(ecg_image.read()))
