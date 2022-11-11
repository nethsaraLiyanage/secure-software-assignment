import bcrypt


def checker(message):


    # converting password to array of bytes
    bytes = message.encode('utf-8')

    # generating the salt
    salt = bcrypt.gensalt()

    # Hashing the password
    hash = bcrypt.hashpw(bytes, salt)

    hash = hash.decode('utf-8')

    print("type:" + str(type(hash)))
    print("hash:" + hash)
    return hash
