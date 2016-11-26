import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import java.util.function.BiFunction

trait InMemoryUsersDao extends UsersDao {

  import Entity._

  private val data = new ConcurrentHashMap[Long, Point]()
  private val idSeq = new AtomicLong(0)

  override def create(point: Point): Long = {
    val id = idSeq.incrementAndGet()
    data.put(id, point)
    id
  }

  override def update(id: Long, newPoint: Point): Option[Point] = {
    var oldPoint: Point = null
    data.computeIfPresent(id, new BiFunction[Long, Point, Point] {
      override def apply(t: Long, u: Point): Point = {
        oldPoint = u
        newPoint
      }
    })
    if (oldPoint != null) Some(oldPoint) else None
  }

  override def delete(id: Long): Option[Point] = {
    val point = data.remove(id)
    if (point != null) Some(point) else None
  }

  override def select(id: Long): Option[Point] = {
    val point = data.get(id)
    if (point != null) Some(point) else None
  }
}