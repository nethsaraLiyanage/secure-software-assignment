# Importing flask module in the project is mandatory
# An object of Flask class is our WSGI application.
from flask import Flask, request

import addMessage
import enc
import keys2
import login_mongo
import addUser
import check
import keys
import readmessage
import downloaded

# Flask constructor takes the name of
# current module (__name__) as argument.
app = Flask(__name__)


# The route() function of the Flask class is a decorator,
# which tells the application which URL should call
# the associated function.
@app.route('/login', methods=['POST'])
def login():
    name = request.form['username']
    post = request.form['post']

    print(post)
    print(name)
    result = login_mongo.login(name, post)
    print(result)
    return result.encode("UTF-8")


@app.route('/check', methods=['POST'])
def check():
    name = request.form['username']
    post = request.form['post']
    hash = request.form['hash']

    print(post)
    print(name)
    result = check.login(name, post)
    print(hash)
    return result.encode("UTF-8")


# the associated function.
@app.route('/adduser', methods=['POST'])
def adduser():
    name = request.form['username']
    post = request.form['post']
    password = request.form['password']

    print(post)
    print(name)
    result = addUser.addUser(name, password, post)
    print(result.encode("UTF-8"))
    return result.encode("UTF-8")


# the associated function.
@app.route('/read')
def read():
    result = readmessage.login()
    print(result)
    return result.encode("UTF-8")


# the associated function.
@app.route('/addmsg', methods=['POST'])
def addmsg():
    name = request.form['name']
    message = request.form['message']
    post = request.form['post']
    checker = request.form['checker']

    result = "not valid"
    ret = keys.checker(message, checker)
    if ret == "1":
        result = addMessage.login(name, message, post)

    return result.encode("UTF-8")


# the associated function.
@app.route('/authserver', methods=['POST'])
def auth():
    salt = request.form['salt']
    print(salt)
    checker = request.form['checker']
    password = "tYlygCrP4MT3I0cMoqcIDxMAuFXwQuPy"
    pass2 = "tYlygCrP4MT3I0cMoqcIDxMAuFXwQuP4"
    returned = salt + pass2
    salt = salt + password
    print(salt)
    ret = keys2.checker(salt, checker)
    if ret == 1:
        print("done")
        results = enc.checker(returned)
    return results.encode("UTF-8")
    print(results)


@app.route('/download', methods=['POST'])
def download():
    name = request.form['name']
    post = request.form['post']
    file = request.form['filename']
    downloaded.downloader(name, post, file)
    return "done".encode("UTF-8")


# main driver function
if __name__ == '__main__':
    # run() method of Flask class runs the application
    # on the local development server.
    app.run(host="0.0.0.0", ssl_context='adhoc')
