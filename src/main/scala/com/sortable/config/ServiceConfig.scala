package com.sortable.config

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by shiv4nsh on 31/8/17.
  */
object ServiceConfig {
  private val config: Config = ConfigFactory.load()

  val listingFilepath = config.getString("filepath.listings")
  val productFilepath = config.getString("filepath.products")
  val resultFilePath = config.getString("filepath.results")
}
