object Entity {

  case class Point(lat: Float, lon: Float)

  case class ZoneKey(tileX: Int, tileY: Int)

  case class ZoneData(distanceError: Float)

  case class ZoneReport(count:Int)

  abstract trait UserEvent

  case class UserCreatedEvent(userId: Long, point: Point) extends UserEvent

  case class UserUpdatedEvent(userId: Long, oldPoint: Point, newPoint: Point) extends UserEvent

  case class UserDeletedEvent(userId: Long, point: Point) extends UserEvent

}
