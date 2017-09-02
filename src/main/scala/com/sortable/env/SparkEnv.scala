package com.sortable.env

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * Created by shiv4nsh on 31/8/17.
  */
object SparkEnv {
  val conf = new SparkConf().setAppName("SortableTest")
    .setMaster("local[*]")
  val sparkSession = SparkSession.builder().config(conf).getOrCreate()
}
