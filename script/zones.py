usersAmount = 1000000
delimiter = ','
with open('..\data\zones.csv', 'w') as file:
    for lon in xrange(-180, 180):
        for lat in xrange(-89, 90):
            file.write(str(lat) + delimiter + str(lon) + delimiter + str(111197) + '\n')