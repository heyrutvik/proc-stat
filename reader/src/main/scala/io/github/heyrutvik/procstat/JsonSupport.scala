package io.github.heyrutvik.procstat

import io.github.heyrutvik.procstat.readers.CorePerc
import spray.json.DefaultJsonProtocol

trait JsonSupport extends DefaultJsonProtocol {
  implicit val corePercFormat = jsonFormat3(CorePerc.apply)
}
