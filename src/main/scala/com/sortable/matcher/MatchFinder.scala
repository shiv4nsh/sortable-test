package com.sortable.matcher

import java.io.PrintWriter

import com.sortable.models.{Listing, Product, Results}
import org.json4s._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.write
import org.json4s.native.JsonMethods._

import scala.io.Source


/**
  * Created by shiv4nsh on 31/8/17.
  */

trait MatchFinder {

  implicit val formats = Serialization.formats(NoTypeHints)

  /**
    *
    * @param listingFilepath filepath of The Listing file
    * @param productFilepath filepath of the product file
    * @param resultFilePath  filepath of the result file.
    */
  def findmatch(listingFilepath: String, productFilepath: String, resultFilePath: String): Unit = {

    val listingAsString = Source.fromFile(listingFilepath).getLines.toList
    val listingList = listingAsString.flatMap(line => parse(line).extractOpt[Listing])

    val productsAsString = Source.fromFile(productFilepath).getLines().toList

    val productList = productsAsString.flatMap(line => parse(line).extractOpt[Product])

    val results = findmatch(listingList, productList)

    val resultsAsJson = results.map(write(_))
    val emptyList = results.filter(_.listings.isEmpty)
    val out = new PrintWriter(resultFilePath, "UTF-8")
    try {
      resultsAsJson.map(out.println(_))
    }
    finally {
      out.close
    }
  }

  /**
    *
    * Contains the main logic behind the code
    *
    * @param listings it represents the List[[Listing]]
    * @param products it represents the List[[Product]]
    * @return returns the List[[Results]]
    */
  private def findmatch(listings: List[Listing], products: List[Product]): List[Results] = {
    var changableListing = listings
    val splitTheData = (a: String) => a.toLowerCase.replaceAll("[,\\-\\(\\)]", "").split("[ .]")
    val data = products.map { product =>
      val listToBeMatched = List(
        splitTheData(product.manufacturer.toLowerCase.replaceAll("fuji", "fuji-")),
        splitTheData(product.family),
        splitTheData(product.model).flatten)

      val func = (listing: Listing) => {
        val titleAsList = splitTheData(listing.title)
        listToBeMatched.map(titleAsList.contains(_)).reduce(_ && _)
      }
      val filteredList = listings.filter(_.manufacturer.toLowerCase().contains(listToBeMatched.head)).filter(func)
      changableListing = changableListing.diff(filteredList)
      (product, filteredList)
    }.distinct

    data.groupBy(_._1).mapValues(_.map(_._2)).map { a =>
      Results(a._1.product_name, a._2.flatten)
    }.toList
  }
}
