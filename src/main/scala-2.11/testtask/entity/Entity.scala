package testtask.entity

case class Point(lat: Double, lon: Double)

case class ZoneKey(tileX: Int, tileY: Int)

case class ZoneData(distanceError: Double)

case class ZoneReport(count: Int)

abstract trait UserEvent

case class UserUpdatedEvent(userId: Long, oldPoint: Option[Point], newPoint: Point) extends UserEvent

case class UserDeletedEvent(userId: Long, point: Point) extends UserEvent