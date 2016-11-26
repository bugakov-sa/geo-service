abstract trait UsersDao {

  import Entity._

  def create(user: UserData): Long

  def update(id: Long, user: UserData): Option[UserData]

  def delete(id: Long)

  def select(id: Long): Option[UserData]
}