import bcrypt


def checker(message, hashed):

    # hashed
    print(message)
    print(hashed)
    hashed = hashed.encode(encoding='UTF-8')
    message = message.encode(encoding='UTF-8')

    print(hashed)
    print(message)

    if bcrypt.checkpw(message, hashed):
        return 1
    else:
        return 0
