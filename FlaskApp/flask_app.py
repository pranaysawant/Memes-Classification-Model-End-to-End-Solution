# import os
# from keras.preprocessing import image
# from keras.applications.imagenet_utils import preprocess_input, decode_predictions
# from keras.preprocessing.image import img_to_array
# from keras.applications import imagenet_utils
# from PIL import Image
# import numpy as np
# import io
# from keras.models import load_model,Sequential

# import urllib.request
# import flask
# from flask import Flask, request, redirect, jsonify
# from werkzeug.utils import secure_filename
# from os import makedirs
# from os.path import exists, join


# from tensorflow import Graph
# import tensorflow as tf
# import keras
# from keras import backend as K


import os
from PIL import Image
import numpy as np
import io

import urllib.request
import flask
from flask import Flask, request, redirect, jsonify
from werkzeug.utils import secure_filename
from os import makedirs
from os.path import exists, join

import keras as keras_lib
import tensorflow
from tensorflow import Graph, keras
from tensorflow.keras import backend as K
from tensorflow.keras.preprocessing import image
from tensorflow.keras.preprocessing.image import img_to_array
from tensorflow.keras.applications.imagenet_utils import preprocess_input
from tensorflow.python.keras.models import load_model

print("Tensorflow Version ",tensorflow.__version__)
print("Tensorflow Keras version ",tensorflow.keras.__version__)
print("Keras Version ",keras_lib.__version__)

K.clear_session()
tensorflow.keras.backend.clear_session()

## Initialise Flask
app = flask.Flask(__name__)

@app.before_first_request
def load_model_keras_model():
    global model
    model = load_model('/home/pranay/Image Upload/model.v1.h5')
    print("=============================Model Loaded==========================")


# Maximum Image Uploading size
app.config['MAX_CONTENT_LENGTH'] = 2 * 1024 * 1024

# Image extension allowed
ALLOWED_EXTENSIONS = set(['png', 'jpg', 'jpeg', 'gif'])

def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


print("Loading keras model")
load_model_keras_model()


## ping server request
@app.route('/pingserver',methods=['GET'])
def pingServer():

    pingResp = {'response_message': "Pong from server", 'response_code': "200", "response_root": "Success"}

    return jsonify(pingResp)

## For multiple files upload..
@app.route('/predict', methods=['POST'])
def predict():

    responseFileList = []

    if 'file' not in request.files:
        fileResp = {'response_message': "No file part in the request", 'response_code': "400", "response_root": "Error"}
        responseFileList.append(fileResp)

    uploaded_files = request.files.getlist("file")

    for file in uploaded_files:

        try:

            if file.filename == '':
                fileResp = {'response_message': "No file part in the request", 'response_code': "400",
                            "response_root": "Error"}
                fileResp = {'file': filename}
                responseFileList.append(fileResp)

            # Check if the file is one of the allowed types/extensions
            elif file and allowed_file(file.filename):
                filename = secure_filename(file.filename)

                class_lable = predict_label(file)

                if (class_lable == ''):

                    fileResp = {'response_message': "Something went wrong", 'response_code': "500",
                                "response_root": "Error", 'imagePath': filename, 'memeStatus': "Not Defined"}
                    responseFileList.append(fileResp)
                else:
                    fileResp = {'response_message': "Valid File", 'response_code': "200",
                                "response_root": "Success", 'imagePath': filename, 'memeStatus': class_lable}
                    responseFileList.append(fileResp)

            else:
                fileResp = {'response_message': "incompatible file extension part in the request",
                            'response_code': "400",
                            "response_root": "Error"}
                responseFileList.append(fileResp)

        except Exception as e:  # in the interest in keeping the output clean...
            filename = secure_filename(file.filename)
            fileResp = {'response_message': "File not uploaded", 'response_code': "500",
                        "response_root": "Error", 'file': filename}
            responseFileList.append(fileResp)
            print(e)
        pass

    return jsonify(responseFileList)




######################################################

def predict_label(f):

    CATEGORIES = ['Not Meme', 'Meme']
    class_lable = ''

    try:
        img = image.load_img(f, target_size=(224, 224))
        x = image.img_to_array(img)
        x = np.expand_dims(x, axis=0)
        x = preprocess_input(x)

        prediction = model.predict(x)
        class_lable = CATEGORIES[int(prediction[0][0])]
    except Exception as e:
        pass
        print("Exception---->", e)

    return class_lable


if __name__ == "__main__":
    print(("* Loading Keras model and Flask starting server..."
           "please wait until server has fully started"))
    app.run(host='192.168.1.104', port=5001, debug=True, threaded=True)


