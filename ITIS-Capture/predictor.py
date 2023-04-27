from keras.models import load_model
from keras.preprocessing import image
from PIL import Image
import base64
import io
import numpy as np

MODEL = {}

index = ['Left Bundle Branch block',
         'Normal',
         'Premature Atrial Contraction',
         'Premature Ventricular Contraction',
         'Right Bundle Branch Block',
         'Ventricular Fibrillation']


def load_trained_model():
    MODEL['trained'] = load_model(r'resources/model/ECG.h5')


def capture_status(ecg_image_stream):
    img = image.image_utils.load_img(ecg_image_stream, target_size=(64, 64))
    x = image.image_utils.img_to_array(img)
    x = np.expand_dims(x, axis=0)

    pred = MODEL['trained'].predict(x)
    y_pred = np.argmax(pred)

    return str(index[y_pred])
