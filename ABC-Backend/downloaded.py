import pyrebase
from datetime import datetime


def downloader(name, post, file):
    config = {
        "apiKey": "AIzaSyApKiO_haVc62LyFbdy1shtRELYLPW3OyI",

        "authDomain": "abc-company-3b427.firebaseapp.com",

        "databaseURL": "https://abc-company-3b427.firebaseio.com",

        "projectId": "abc-company-3b427",

        "storageBucket": "abc-company-3b427.appspot.com",

        "messagingSenderId": "837766685353",

        "appId": "1:837766685353:web:af8763786255bc20b23fb8",

        "measurementId": "G-TF8G4L6MV1"

    }
    now = datetime.now()
    dt_string = now.strftime("%d-%m-%Y_%H:%M:%S")

    firebase = pyrebase.initialize_app(config)
    storage = firebase.storage()

    path_OC = "files/"+file+".txt"
    print(path_OC)
    path_L = "files/"
    path_L2 = "files/" + name + "[" + post + "]" + dt_string + ".txt"
    print(path_L2)
    storage.child(path_OC).download(path_L, path_L2,"files/" + name + "[" + post + "]" + dt_string + ".txt")
    print("done")
    return "done"


