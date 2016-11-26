import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.Logger

import scala.io.StdIn

object Application extends App {

  private val log = Logger("Application")

  val conf = Configuration.read

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val usersDao = new InMemoryUsersDao()
  val controller = new Controller(usersDao)
  val bindingFuture = Http().bindAndHandle(controller.route, conf.host, conf.port)

  log.info("Server online at http://{}:{}/", conf.host, conf.port.toString)
  log.info("Press ENTER to stop...")

  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}