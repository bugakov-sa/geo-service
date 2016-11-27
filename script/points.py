import random
import math
pointsAmount = 300
delimiter = ','
with open('..\data\points.csv', 'w') as file:
    for i in xrange(0, pointsAmount):
        file.write(
            str(math.degrees(math.asin(random.uniform(0, 1))) * ((-1) ** (i % 2))) + delimiter +
            str(random.uniform(-180, 180)) + '\n')