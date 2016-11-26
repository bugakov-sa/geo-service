trait UsersListener {

  import Entity._

  def fire(event: UserEvent)
}
