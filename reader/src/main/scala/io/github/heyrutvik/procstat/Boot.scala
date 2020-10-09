package io.github.heyrutvik.procstat

import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import com.typesafe.config.ConfigFactory
import io.github.heyrutvik.procstat.producers.{CpuProducer, Producer}
import io.github.heyrutvik.procstat.readers.{Cores, CpuStateReader, StateReader}
import org.apache.kafka.common.serialization.StringSerializer

import scala.language.postfixOps

object Boot extends App with JsonSupport {
  implicit val system: ActorSystem = ActorSystem("ProcStat")
  val coreReader: StateReader[Cores] = new CpuStateReader
  val cpuProducer: Producer[String, String, Cores] = new CpuProducer(coreReader)
  cpuProducer.run {
    val appConfig = ConfigFactory.load
      ProducerSettings(appConfig.getConfig("akka.kafka.producer"), new StringSerializer, new StringSerializer)
        .withBootstrapServers(appConfig.getString("kafka.bootstrap-server"))
  }
}
