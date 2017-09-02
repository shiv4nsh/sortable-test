package com.sortable


import com.sortable.config.ServiceConfig
import com.sortable.matcher.MatchFinder

object Boot extends App with MatchFinder {

  val serviceConfig = ServiceConfig
  findmatch(
    serviceConfig.listingFilepath,
    serviceConfig.productFilepath,
    serviceConfig.resultFilePath)
}