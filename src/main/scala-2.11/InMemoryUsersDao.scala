import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import java.util.function.BiFunction

trait InMemoryUsersDao extends UsersDao {

  import Entity._

  private val data = new ConcurrentHashMap[Long, UserData]()
  private val idSeq = new AtomicLong(0)

  override def create(user: UserData): Long = {
    val id = idSeq.incrementAndGet()
    data.put(id, user)
    id
  }

  override def update(id: Long, newUser: UserData): Option[UserData] = {
    var oldUser: UserData = null
    data.computeIfPresent(id, new BiFunction[Long, UserData, UserData] {
      override def apply(t: Long, u: UserData): UserData = {
        oldUser = u
        newUser
      }
    })
    if (oldUser != null) Some(oldUser) else None
  }

  override def delete(id: Long): Option[UserData] = {
    val oldUser = data.remove(id)
    if (oldUser != null) Some(oldUser) else None
  }

  override def select(id: Long): Option[UserData] = {
    val user = data.get(id)
    if (user != null) Some(user) else None
  }
}