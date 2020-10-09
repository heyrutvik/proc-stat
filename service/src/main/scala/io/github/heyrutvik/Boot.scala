package io.github.heyrutvik

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Boot extends App {
  implicit val system: ActorSystem = ActorSystem("proc-stat-service")
  val stats = new StatRoutes(ConfigFactory.load())
  Http()
    .newServerAt("localhost", 9000)
    .bind(stats.routes)
    .onComplete {
      case Success(value) => println(value)
      case Failure(err) => println(err)
    }
}
