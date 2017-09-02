package com.sortable


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
    * @param listingFilepath
    * @param productFilepath
    * @param resultFilePath
    */
  def findmatch(listingFilepath: String, productFilepath: String, resultFilePath: String): Unit = {

    val listingAsString = Source.fromFile(listingFilepath).getLines.toList
    val listingList = listingAsString.flatMap { line =>
      parse(line).extractOpt[Listing]
    }

    val productsAsString = Source.fromFile(productFilepath).getLines().toList

    val productList = productsAsString.flatMap { line =>
      parse(line).extractOpt[Product]
    }

    val results = findmatch(listingList, productList)

    val resultsAsJson = results.map(write(_))
    val emptyList = results.filter(_.listings.length == 0)
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
    * @param listings it represents the List[[Listing]]
    * @param products it represents the List[[Product]]
    * @return returns the List[[Results]]
    */
  private def findmatch(listings: List[Listing], products: List[Product]): List[Results] = {
    var changableListing = listings
    val data = products.map { product =>
      val listToBeMatched = List(
        product.manufacturer.toLowerCase.replaceAll("fuji", "fuji-").replaceAll("[,\\-\\(\\)]", "").split("[ .]"),
        product.family.toLowerCase.replaceAll("[,\\-\\(\\)]", "").split("[ .]"),
        product.model.toLowerCase.replaceAll("[,\\-\\(\\)]", "").split("[ .]")).flatten

      val func = (listing: Listing) => {
        val titleAsList = listing
          .title
          .toLowerCase
          .replaceAll("[,\\-\\(\\)]", "")
          .split("[ .]")
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
