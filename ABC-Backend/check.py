import pymongo;
from pymongo import MongoClient;


def login(name, post):
    cluster = MongoClient('mongodb://localhost:27017')
    passwordHash = "NotFound"

    db = cluster["ABC"]
    if post == 'admin':
        collection = db["Admin"]
    if post == 'manager':
        collection = db["Manager"]
    if post == 'worker':
        collection = db["Worker"]

    results = collection.find_one({"name": name})
    if results:
        passwordHash = results["password"]
        id = results["_id"]

    print(id)
    return id