from math import acos, sin, cos

delimiter = ','
earthRadius = 6371000

def sphereDist(lat1, lat2, lon1, lon2):
    return earthRadius * acos(sin(lat1) * sin(lat2) + cos(lat1) * cos(lat2) * cos(lon1 - lon2))

with open('..\data\zones.csv', 'w') as file:
    for lon in xrange(-180, 180):
        for lat in xrange(0, 90):
            distanceError = sphereDist(lat, lat + 1, lon, lon + 1)
            northLatRecord = str(lat) + delimiter + str(lon) + delimiter + str(distanceError) + '\n'
            file.write(northLatRecord)
            southLatRecord = '-' + northLatRecord
            file.write(southLatRecord)