import pymongo;
from pymongo import MongoClient;


def addUser(name, password, post):
    cluster = MongoClient('mongodb://localhost:27017')

    db = cluster["ABC"]
    if post == 'Manager':
        collection = db["Manager"]
    if post == 'Worker':
        collection = db["Worker"]

    post = {"name": name, "password": password}

    collection.insert_one(post)

    results = collection.find_one({"name": name})
    passwordHash = results["password"]

    if passwordHash == password:
        return "added"
    else:
        return "error"

