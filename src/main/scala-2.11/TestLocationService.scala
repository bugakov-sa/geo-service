import Entity.{Point, ZoneKey}

class TestLocationService(usersDao: UsersDao, zonesRef: ZonesRef) {
  def test(userId: Long, point: Point) = {
    val userPoint = usersDao.select(userId)
    val zoneData = zonesRef.select(ZoneKey(point.lat.toInt, point.lon.toInt))
    if (userPoint.isEmpty || zoneData.isEmpty)
      None
    else
      Some(sphereDist(userPoint.get, point) <= zoneData.get.distanceError)
  }

  private def sphereDist(p1: Point, p2: Point) = {
    0.0
  }
}