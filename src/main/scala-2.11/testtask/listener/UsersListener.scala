package testtask.listener

import testtask.entity.UserEvent

trait UsersListener {

  def fire(event: UserEvent)
}