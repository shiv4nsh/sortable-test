package com.sortable


import com.sortable.config.ServiceConfig
import com.sortable.spark.MatchFinderSpark


object BootSpark {

  import com.sortable.env.SparkEnv._

  val serviceConfig = ServiceConfig

  def main(args: Array[String]): Unit = {

    new MatchFinderSpark(sparkSession).findmatch(
      serviceConfig.listingFilepath,
      serviceConfig.productFilepath,
      serviceConfig.resultFilePath)
    sparkSession.stop()

  }

}

object BootSimple extends App with MatchFinder {

  val serviceConfig = ServiceConfig
  findmatch(
    serviceConfig.listingFilepath,
    serviceConfig.productFilepath,
    serviceConfig.resultFilePath)
}