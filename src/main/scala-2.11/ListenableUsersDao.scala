trait ListenableUsersDao extends UsersDao {

  import Entity._

  protected val listeners: Seq[UsersListener]

  private def fire(event: UserEvent) = listeners.foreach(_.fire(event))

  override def update(id: Long, newPoint: Point): Option[Point] = {
    val option = super.update(id, newPoint)
    option.foreach(oldPoint => fire(UserUpdatedEvent(id, oldPoint, newPoint)))
    option
  }

  override def delete(id: Long): Option[Point] = {
    val option = super.delete(id)
    option.foreach(point => fire(UserDeletedEvent(id, point)))
    option
  }
}
