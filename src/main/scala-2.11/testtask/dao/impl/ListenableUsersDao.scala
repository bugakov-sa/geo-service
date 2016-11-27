package testtask.dao.impl

import java.util.function.Consumer

import testtask.dao.UsersDao
import testtask.entity._

trait ListenableUsersDao extends UsersDao {

  protected val listeners: Seq[Consumer[UserEvent]]

  private def fire(event: UserEvent) = listeners.foreach(_.accept(event))

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