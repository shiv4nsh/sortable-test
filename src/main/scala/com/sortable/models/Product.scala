package com.sortable.models

/**
  * Created by shiv4nsh on 31/8/17.
  */
case class Product(
                     product_name: String, // A unique id for the product
                     manufacturer: String,
                     family: String, // optional grouping of products
                     model: String,
                     `announced-date`: String // ISO-8601 formatted date string, e.g. 2011-04-28T19:00:00.000-05:00
                   )
