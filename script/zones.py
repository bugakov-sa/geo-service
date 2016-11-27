from math import acos, sin, cos, radians

delimiter = ','
earthRadius = 6371000

def sphereDist(lat1, lat2, lon1, lon2):
    return earthRadius * acos(sin(radians(lat1)) * sin(radians(lat2)) +
                              cos(radians(lat1)) * cos(radians(lat2)) * cos(radians(lon1) - radians(lon2)))

with open('..\data\zones.csv', 'w') as file:
    for lon in xrange(-180, 180):
        for northLat in xrange(0, 90):
            distanceError = sphereDist(northLat, northLat + 1, lon, lon + 1)
            file.write(str(northLat) + delimiter + str(lon) + delimiter + str(distanceError) + '\n')