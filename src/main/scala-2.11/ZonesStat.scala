import java.util.concurrent.atomic.AtomicLong

import Entity._

class ZonesStat(zones: ZonesRef) extends UsersListener {

  private val stat = init(zones)

  private def init(zones: ZonesRef) = {
    var res = Map[ZoneKey, AtomicLong]()
    for (key <- zones.keys) {
      res = res + (key -> new AtomicLong(0))
    }
    res
  }

  private def zoneKey(point: Point) = ZoneKey(point.lat.toInt, point.lon.toInt)

  override def fire(event: UserEvent): Unit = event match {
    case UserCreatedEvent(userId, userData) =>
      stat.get(zoneKey(userData)).foreach(_.incrementAndGet())
    case UserUpdatedEvent(userId, oldData, newData) =>
      stat.get(zoneKey(oldData)).foreach(_.decrementAndGet())
      stat.get(zoneKey(newData)).foreach(_.incrementAndGet())
    case UserDeletedEvent(userId, oldData) =>
      stat.get(zoneKey(oldData)).foreach(_.decrementAndGet())
  }

  def count(point: Point) = stat.get(zoneKey(point)) match {
    case None => None
    case Some(stat) => Some(stat.get())
  }
}