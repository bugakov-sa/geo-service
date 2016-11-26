trait ListenableUsersDao extends UsersDao {

  import Entity._

  protected val listeners: Seq[UsersListener]

  private def fire(event: UserEvent) = listeners.foreach(_.fire(event))

  override def create(user: UserData): Long = {
    val userId = super.create(user)
    fire(UserCreatedEvent(userId, user))
    userId
  }

  override def update(id: Long, newData: UserData): Option[UserData] = {
    val option = super.update(id, newData)
    option.foreach(oldData => fire(UserUpdatedEvent(id, oldData, newData)))
    option
  }

  override def delete(id: Long): Option[UserData] = {
    val option = super.delete(id)
    option.foreach(userData => fire(UserDeletedEvent(id, userData)))
    option
  }

  override def select(id: Long): Option[UserData] = super.select(id)
}
