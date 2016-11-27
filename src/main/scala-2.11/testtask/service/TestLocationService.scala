package testtask.service

import testtask.dao.UsersDao
import testtask.entity.{Point, ZoneKey}
import testtask.ref.ZonesRef

import math._

class TestLocationService(usersDao: UsersDao, zonesRef: ZonesRef) {
  def test(userId: Long, point: Point) = {
    val userPoint = usersDao.select(userId)
    val zoneData = zonesRef.select(ZoneKey(point.lat.toInt, point.lon.toInt))
    if (userPoint.isEmpty || zoneData.isEmpty)
      None
    else
      Some(sphereDist(userPoint.get, point) <= zoneData.get.distanceError)
  }

  private val EARTH_RADIUS = 6371000

  private def sphereDist(p1: Point, p2: Point) =
    EARTH_RADIUS * acos(sin(p1.lat) * sin(p2.lat) + cos(p1.lat) * cos(p2.lat) * cos(p1.lon - p2.lon))
}