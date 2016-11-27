from random import uniform
from math import degrees, asin

usersAmount = 1000000
delimiter = ','

with open('..\data\users.csv', 'w') as file:
    for userId in xrange(0, usersAmount):
        file.write(str(userId) + delimiter +
                   str(degrees(asin(uniform(0, 1))) * ((-1) ** (userId % 2))) + delimiter +
                   str(uniform(-180, 180)) + '\n')