package testtask {

  import scala.math.{abs, floor}
  package entity {

    case class Point(lat: Double, lon: Double)

    case class ZoneKey(tileX: Int, tileY: Int, isSouth: Boolean)

    case class ZoneData(distanceError: Double)

    case class ZoneStat(count: Int)

    abstract trait UserEvent

    case class UserUpdatedEvent(userId: Long, oldPoint: Option[Point], newPoint: Point) extends UserEvent

    case class UserDeletedEvent(userId: Long, point: Point) extends UserEvent

  }

  package object entity {
    def zoneKey(point: Point) = ZoneKey(
      floor(abs(point.lat)).toInt,
      floor(point.lon).toInt,
      point.lat < 0
    )
  }

}