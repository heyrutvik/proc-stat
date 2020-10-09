package io.github.heyrutvik.procstat.producers

import akka.Done
import akka.actor.{ActorSystem, Cancellable}
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.{Producer => KafkaProducer}
import akka.stream.scaladsl.Source
import io.github.heyrutvik.procstat.readers.StateReader
import org.apache.kafka.clients.producer.ProducerRecord

import scala.concurrent.Future

abstract class Producer[K, V, T](stateReader: StateReader[T]) {
  def pipeline: Source[ProducerRecord[K, V], Cancellable]
  def run(producerSettings: ProducerSettings[K, V])(implicit system: ActorSystem): Future[Done] = {
    pipeline.runWith(KafkaProducer.plainSink(producerSettings))
  }
}
