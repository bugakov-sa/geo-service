usersAmount = 1000000
delimiter = ','
equatorLength = 40075000
distanceError = equatorLength /  360.
with open('..\data\zones.csv', 'w') as file:
    for lon in xrange(-180, 180):
        for lat in xrange(-89, 90):
            file.write(str(lat) + delimiter + str(lon) + delimiter + str(distanceError) + '\n')