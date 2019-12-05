# Memes-Classification-Model-End-to-End-Solution

<img src="images/FileHeader.jpg" style="width:100px;height:200px;" >

## Introduction

WhatsApp has over 1.2 billion daily active users today. WhatsApp has become an indispensible part of our lives. The app has made it fast and easier to stay connected with our friends.
However, it can be little annoying at times when the chat app automatically download all those stupid memes, audio files, videos and photos. This not just makes the phone's gallery look like a complete mess, it also eats up you mobile data. Here is a fix to this to this problem.
Too many WhatsApp images, videos making your phone slow down. Deleting whole WhasApp Images folder is bad option because it may contain our important personal images.
So we need to somehow seperate out unnecessary memes picture and our personal images.

**Can we build a model which can identify memes images and real images**?


Google's File app is there to help you out it is finding memes images and listed those images to user, then user will decide whichone he/she want to keep in phone and whichone he/she would like to delete.
Memes detection app is similar small demo version app.


## Problem Statement :
Classify Memes images from phone gallery without touching personal images. Find out memes images from gallery.

## Real world/Business Objectives and Constraints :
- The cost of a mis-classification can be very high. Because suppose if image is not meme and we classified it is as meme then it not good for app.
- No strict latency concerns.

## Performance Metric:
- Binary cross entropy

## Deploy Model

1. deploy model on andriod environment
2. deploy moodel on server using flask

### 1. Deploy model on andriod environment
We fine-tune couple of models. **MOBILE-NET** model on fine tuned and convert to .tflite using **TFLite** library. Full code can be found
- https://github.com/pranaysawant/Memes-Classification-Model-End-to-End-Solution/tree/master/AndroidApp

### 2. Deploy model on server using flask
We fine-tune **VGG19** model and deployed on GCP server using **Flask** framework. We have written REST API for the same. code can be found 
- https://github.com/pranaysawant/Memes-Classification-Model-End-to-End-Solution/tree/master/FlaskApp


## Result:

### Below is "Files" app provided by Google.


| | | |
|:-------------------------:|:-------------------------:|:-------------------------:|
|<img src="images/FileApp_Header.png" style="width:350px;height:600px;">    | <img src="images/filesApp__1.png" style="width:350px;height:600px;"> |<img src="images/filesApp_2.png" style="width:350px;height:600px;"> |


### Below is Memes Classification App

##### 1. Landing Page


| | |
|:-------------------------:|:-------------------------:|
| <img src="images/landing_page.png"  style="width:350px;height:600px;"> | <img src="images/landing_page2.png" style="width:350px;height:600px;">|




1. Check single Images Locally
2. Check Multiple Images Locally
3. Check Single Image on Server(Flask REST API)
4. Check Multiple Image on Server(Flask REST API)
5. Ping



We have built 2 models. One is keras **VGG19** fine tune model and other is **MobileNet** Model. So we VGG19 model is deployed using **Flask framework** and Mobilenet Keras model convert to .tflite version using **TFLite** library.


Locally means we are testing on Mobilenet Model

Server means we are testing over VGG19 model.



### 1. Check single Images Locally

It means .tflite model is running locally and test only single image.

| | | | |
|:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------:|
|<img src="images/slocal_meme_1.png" style="width:350px;height:600px;">    | <img src="images/slocal_meme_2.png" style="width:350px;height:600px;"> |<img src="images/slocal_n_meme_1.png" style="width:350px;height:600px;"> |  <img src="images/slocal_no_meme_2.png" style="width:350px;height:600px;"> |


### 2. Check Multiple Images Locally
It means .tflite model is running locally and test multiple images.


| | | |
|:-------------------------:|:-------------------------:|:-------------------------:|
|<img src="images/bulk_lcal3.png" style="width:350px;height:600px;"> | <img src="images/bulk_local1.png" style="width:350px;height:600px;"> |<img src="images/bulk_local3.png" style="width:350px;height:600px;"> | 

### 3. Check Single Image on Server(Flask REST API)
It means **VGG19** model is running flask GCP server and test only single image.


| | | |
|:-------------------------:|:-------------------------:|:-------------------------:|
|<img src="images/s_1.png" style="width:350px;height:600px;"> | <img src="images/s_2.png" style="width:350px;height:600px;"> |<img src="images/s_3.png" style="width:350px;height:600px;"> | 


## 4. Check Multiple Image on Server(Flask REST API)
It means **VGG19** model is running flask GCP server and test multiple images.


| | | |
|:-------------------------:|:-------------------------:|:-------------------------:|
|<img src="images/server_bulk1.png" style="width:350px;height:600px;"> | <img src="images/server_bulk2.png" style="width:350px;height:600px;"> |<img src="images/server_bulk3.png" style="width:350px;height:600px;"> | 
