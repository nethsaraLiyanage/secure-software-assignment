import string

import pymongo;
from pymongo import MongoClient;


def login():
    cluster = MongoClient('mongodb://localhost:27017')

    db = cluster["ABC"]
    collection = db["Messages"]
    messages = ""

    results = collection.find({})
    if results is None:
        print("---")
    else:
        for x in results:
            id = str(x["id"])
            name = x["name"]
            post = x["post"]
            message = x["message"]
            time = x["time"]
            print(type(id))
            messages = messages + "________________________________________________________\n\t" +name + "(" + post + ")\n\t[" + id + "]\n\n\t" + message + "\n\t" + time +"\n"
            print(messages)

    messages = messages + "\n________________________________________________________\n\t"
    print(type(messages))
    return messages
