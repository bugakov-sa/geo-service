import random
import math
usersAmount = 1000000
delimiter = ','
with open('..\data\users.csv', 'w') as file:
    for userId in xrange(0, usersAmount):
        file.write(str(userId) + delimiter +
                   str(math.degrees(math.asin(random.uniform(0, 1))) * ((-1) ** (userId % 2))) + delimiter +
                   str(random.uniform(-180, 180)) + '\n')