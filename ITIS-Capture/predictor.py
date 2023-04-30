import PIL
import tensorflow as tf
import numpy as np

TF_MODEL_FILE_PATH = 'resources/model.tflite'
class_names = ['F', 'N', 'Q', 'S', 'V']
label = {
    "N" : "Normal",
    "S" : "Supraventricular ectopic",
    "V" : "Ventricular ectopic",
    "F" : "Fusion",
    "Q" : "Unknown"
}

interpreter = tf.lite.Interpreter(model_path=TF_MODEL_FILE_PATH)
classify_lite = interpreter.get_signature_runner('serving_default')

def capture_status(ecg_image_stream):
    img = tf.keras.utils.load_img(ecg_image_stream, target_size=(256, 256))
    img_array = tf.keras.utils.img_to_array(img)
    img_array = tf.expand_dims(img_array, 0)

    predictions_lite = classify_lite(sequential_1_input=img_array)['outputs']
    score_lite = tf.nn.softmax(predictions_lite)

    print("Prediction : '{}' with a {:.2f} percent confidence."
    .format(label[class_names[np.argmax(score_lite)]], 100 * np.max(score_lite)))

    return label[class_names[np.argmax(score_lite)]]