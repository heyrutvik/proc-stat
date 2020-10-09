package io.github.heyrutvik.procstat.producers
import akka.NotUsed
import akka.actor.Cancellable
import akka.stream.scaladsl.Source
import io.github.heyrutvik.procstat.JsonSupport
import io.github.heyrutvik.procstat.readers.{CorePerc, Cores, StateReader}
import org.apache.kafka.clients.producer.ProducerRecord
import spray.json._

import scala.concurrent.duration._
import scala.language.postfixOps

class CpuProducer(cpuStateReader: StateReader[Cores]) extends Producer[String, String, Cores](cpuStateReader) with JsonSupport {
  override def pipeline: Source[ProducerRecord[String, String], Cancellable] = {
    Source
      .tick(0 second, 0.25 second, NotUsed)
      .map(_ => cpuStateReader.read)
      .sliding(2)
      .map { case Seq(prev, curr) =>
        prev.stats.zip(curr.stats).flatMap { case (prev, curr) => CorePerc(curr, prev) }
      }
      .throttle(1, 0.5 second)
      .map(cores => new ProducerRecord[String, String]("cpu","cores", cores.toJson.compactPrint))
  }
}
