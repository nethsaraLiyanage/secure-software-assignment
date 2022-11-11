import bcrypt


def checker(message, hashed):

    message = message + "M5SO8Gt0X9UHSg5YpThB0nG7hXsrpQ2v"
    # hashed
    print(message)
    print(hashed)
    hashed = hashed.encode(encoding='UTF-8')
    message = message.encode(encoding='UTF-8')

    print(hashed)
    print(message)

    if bcrypt.checkpw(message, hashed):
        return "1"
    else:
        return "0"
