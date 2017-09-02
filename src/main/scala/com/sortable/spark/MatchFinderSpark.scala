package com.sortable.spark

import com.sortable.env.SparkEnv.sparkSession
import com.sortable.models.Listing
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.functions.lower
import org.apache.spark.sql.functions.{array, collect_list}


/**
  * Created by shiv4nsh on 31/8/17.
  */
class MatchFinderSpark(sparkSession: SparkSession) {

  def findmatch(listingFilepath: String, productFilepath: String, resultFilePath: String) = {

    import sparkSession.implicits._
    val listingsDF = sparkSession.read.json(listingFilepath).withColumn("title", lower($"title"))
    //Cache the data for faster processing
    listingsDF.cache()

    // Taking out the product list
    val productList = sparkSession.read.json(productFilepath)
      .select("product_name")
      .rdd.map(_.getString(0))
      .collect()

    val dataTobeStored = productList.map { product =>
      //Taking out all the values to be matched
      val valuesTobeMatched = product
        .split("_|\\-")
        .map(_.toLowerCase)

      val sql = valuesTobeMatched
        .map(a => s"title rlike '$a'")
        .mkString(" AND ")

      listingsDF.where(sql).rdd.map { row =>
        val title = row.getAs[String]("title")
        val manufacturer = row.getAs[String]("manufacturer")
        val currency = row.getAs[String]("currency")
        val price = row.getAs[String]("price")
        (product, Listing(title, manufacturer, currency, price))
      }
    }

    val dataTobeStoredDF = dataTobeStored.map(_.toDF("product_name", "listings"))
    //Creating the Dataframe for restoring the data
    val dfToBeStored = dataTobeStoredDF.reduce(_ union _).coalesce(1).groupBy("product_name").agg(collect_list("listings").as("listings"))
    //Wrtiting the data to a file
    dfToBeStored.coalesce(1).write.mode(SaveMode.Overwrite).json(resultFilePath)

  }
}
