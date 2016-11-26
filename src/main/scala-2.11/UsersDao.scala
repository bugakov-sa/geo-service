trait UsersDao {

  import Entity._

  def create(user: UserData): Long = -1

  def update(id: Long, newData: UserData): Option[UserData] = None

  def delete(id: Long): Option[UserData] = None

  def select(id: Long): Option[UserData] = None
}