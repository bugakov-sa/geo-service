package testtask.dao.impl

import testtask.entity._
import testtask.dao.UsersDao
import testtask.listener.UsersListener

trait ListenableUsersDao extends UsersDao {

  protected val listeners: Seq[UsersListener]

  private def fire(event: UserEvent) = listeners.foreach(_.fire(event))

  override def update(id: Long, newPoint: Point): Option[Point] = {
    val option = super.update(id, newPoint)
    fire(UserUpdatedEvent(id, option, newPoint))
    option
  }

  override def delete(id: Long): Option[Point] = {
    val option = super.delete(id)
    option.foreach(point => fire(UserDeletedEvent(id, point)))
    option
  }
}