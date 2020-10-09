package io.github.heyrutvik

import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Directives.{path, _}
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.scaladsl.{Flow, Sink}
import com.typesafe.config.Config
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer

class StatRoutes(appConfig: Config) {

  private val bootstrapServer = appConfig.getString("kafka.bootstrap-server")
  private val config = appConfig.getConfig("akka.kafka.consumer")
  private val consumerSettings =
    ConsumerSettings(config, new StringDeserializer, new StringDeserializer)
      .withBootstrapServers(bootstrapServer)
      .withGroupId("cpu")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")

  val routes = path("cpu-stats") {
    val in = Sink.ignore
    val out = Consumer.plainSource(consumerSettings, Subscriptions.assignment(
      new TopicPartition("cpu",0)
    )).map(record => TextMessage(record.value))

    handleWebSocketMessages(Flow.fromSinkAndSource(in, out))
  }
}
