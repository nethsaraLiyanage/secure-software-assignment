import pymongo
from datetime import datetime
from pymongo import MongoClient


def login(name, message, post):
    cluster = MongoClient('mongodb://localhost:27017')
    print("--------------")
    print(post)
    print("--------------")
    db = cluster["ABC"]
    if post == 'admin':
        collection = db["Admin"]
    if post == 'manager':
        collection = db["Manager"]
    if post == 'worker':
        collection = db["Worker"]

    results = collection.find_one({"name": name})
    id = results["_id"]
    db = cluster["ABC"]
    collection = db["Messages"]

    now = datetime.now()

    time = now.strftime("%H:%M:%S")

    post = {"name": name, "post": post, "message": message, "time": time, "id": id}



    collection.insert_one(post)

    results = collection.find_one({"name": name, "time": time})
    messages = results["message"]

    if messages == message:
        return "1"
    else:
        return "0"
