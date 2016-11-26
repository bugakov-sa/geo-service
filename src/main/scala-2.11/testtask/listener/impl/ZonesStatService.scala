package testtask.listener.impl

import java.util.concurrent.atomic.AtomicLong

import testtask.entity._
import testtask.listener.UsersListener
import testtask.ref.ZonesRef

class ZonesStatService(zones: ZonesRef) extends UsersListener {

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
    case UserUpdatedEvent(userId, oldPoint, newPoint) =>
      stat.get(zoneKey(oldPoint)).foreach(_.decrementAndGet())
      stat.get(zoneKey(newPoint)).foreach(_.incrementAndGet())
    case UserDeletedEvent(userId, point) =>
      stat.get(zoneKey(point)).foreach(_.decrementAndGet())
  }

  def count(point: Point) = stat.get(zoneKey(point)) match {
    case None => None
    case Some(stat) => Some(stat.get())
  }
}