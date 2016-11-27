package testtask.service

import testtask.dao.UsersDao
import testtask.entity._
import testtask.ref.ZonesRef

import math._

class TestLocationService(usersDao: UsersDao, zonesRef: ZonesRef) {

  def test(userId: Long, point: Point) = {
    for {
      userPoint <- usersDao.select(userId)
      zoneData <- zonesRef.select(zoneKey(point))
    } yield sphereDist(userPoint, point) <= zoneData.distanceError
  }

  private val EARTH_RADIUS = 6371000

  private def sphereDist(p1: Point, p2: Point) = {
    val lat1Radians = toRadians(p1.lat)
    val lat2Radians = toRadians(p2.lat)
    val lon1Radians = toRadians(p1.lon)
    val lon2Radians = toRadians(p2.lon)
    EARTH_RADIUS * acos(sin(lat1Radians) * sin(lat2Radians) +
      cos(lat1Radians) * cos(lat2Radians) * cos(lon1Radians - lon2Radians))
  }
}