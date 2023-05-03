import PIL
import tensorflow as tf
import numpy as np

TF_MODEL_FILE_PATH = 'model.tflite'
interpreter = tf.lite.Interpreter(model_path=TF_MODEL_FILE_PATH)
interpreter.get_signature_list()
classify_lite = interpreter.get_signature_runner('serving_default')

class_names = ['Left Bundle Branch Block', 'Normal', 'Premature Atrial Contraction', 'Premature Ventricular Contractions', 'Right Bundle Branch Block', 'Ventricular Fibrillation']

def capture_status(ecg_image_stream):

    img = tf.keras.utils.load_img(ecg_image_stream, target_size=(128, 192))
    img_array = tf.keras.utils.img_to_array(img)
    img_array = tf.expand_dims(img_array, 0)

    predictions_lite = classify_lite(sequential_1_input=img_array)['outputs']
    score_lite = tf.nn.softmax(predictions_lite)

    prediction = class_names[np.argmax(score_lite)]

    print(
        "Prediction= {} with a {:.2f} percent confidence."
        .format(prediction, 100 * np.max(score_lite))
    )

    return prediction