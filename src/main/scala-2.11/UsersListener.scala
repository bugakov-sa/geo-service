import Entity.UserEvent

trait UsersListener {

  def fire(event: UserEvent)
}