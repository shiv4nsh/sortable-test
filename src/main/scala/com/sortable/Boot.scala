package com.sortable


import com.sortable.config.ServiceConfig
import com.sortable.matcher.MatchFinder

object Boot extends App {

  val serviceConfig = ServiceConfig
  MatchFinder.findmatch(
    serviceConfig.listingFilepath,
    serviceConfig.productFilepath,
    serviceConfig.resultFilePath)
}