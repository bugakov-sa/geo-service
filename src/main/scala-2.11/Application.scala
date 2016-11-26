import Entity.{UserEvent, ZoneData, ZoneKey}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.Logger

import scala.io.StdIn

object Application extends App {

  private val log = Logger("Application")

  val conf = Configuration.read
  val zonesRef = InputData.loadZones
  val zonesStat = new ZonesStat(zonesRef)
  val usersDao = new InMemoryUsersDao with ListenableUsersDao {
    override protected val listeners = List(zonesStat)
  }
  InputData.loadUsers(usersDao)
  val controller = new Controller(usersDao, zonesStat)

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  val bindingFuture = Http().bindAndHandle(controller.route, conf.host, conf.port)

  log.info("Server online at http://{}:{}/", conf.host, conf.port.toString)
  log.info("Press ENTER to stop...")

  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}