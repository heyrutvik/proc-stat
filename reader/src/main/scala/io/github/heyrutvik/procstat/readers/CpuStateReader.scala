package io.github.heyrutvik.procstat.readers

import java.util.concurrent.atomic.AtomicInteger

import com.typesafe.scalalogging.StrictLogging

import scala.io.Source
import scala.util.Using

class CpuStateReader extends StateReader[Cores] with StrictLogging {

  private val counter = new AtomicInteger()

  override def read: Cores = {
    logger.info(s"[${counter.incrementAndGet()}] reading cpu stats...")
    Using(Source.fromFile("/proc/stat")) { source =>
      source
        .getLines()
        .filter(_.matches("^cpu[0-9]+.*"))
        .map(CoreStat(_))
        .toList
    }.fold(ex => {
      logger.error("couldn't find info", ex)
      Cores(List.empty[CoreStat])
    }, stats => {
      logger.info(s"found info of ${stats.length} cores")
      Cores(stats)
    })
  }
}

case class Cores(stats: List[CoreStat])
case class CoreStat(name: String, active: Int, idle: Int) // raw numbers
case class CorePerc(name: String, active: Double, idle: Double) // calculated percentage

object CoreStat {
  // `coreInfo` will be single line of /proc/stat which starts with "cpuN ..."
  // 0      1     2     3       4       5       6   7       8     9     10
  // name   user  nice  system  idle    iowait  irq softirq steal guest guest_nice
  // cpu0   5222  4     720     137496  54      0   2566    0     0     0
  def apply(coreInfo: String): CoreStat = {
    val info = coreInfo.split(" ")
    val name = info(0)
    val active = info(1).toInt + info(2).toInt + info(3).toInt + info(6).toInt + info(7).toInt + info(8).toInt + info(9).toInt + info(10).toInt
    val idle = info(4).toInt + info(5).toInt
    new CoreStat(name, active, idle)
  }
}

object CorePerc extends StrictLogging {
  def apply(prev: CoreStat, curr: CoreStat): Option[CorePerc] = {
    if (prev.name == curr.name) {
      val active = curr.active - prev.active
      val idle = curr.idle - prev.idle
      val total = active + idle
      Some(new CorePerc(
        prev.name, ceil(100.0 * active / total), ceil(100.0 * idle / total)))
    } else {
      logger.error(s"Core stat mismatch, found ${prev.name} and ${curr.name}")
      None
    }
  }

  private def ceil(value: Double): Double = BigDecimal(value).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
}