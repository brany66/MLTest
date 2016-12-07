package edu.nju.pasalab.SQL

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession


/**
  * Created by YWJ on 2016.12.7.
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  * Catalogue Example
  */
object CatalogExample {

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    val sparkSession = SparkSession.builder.
           master("local")
           .appName("example")
           .getOrCreate()


    val df = sparkSession.read.csv("data/sales.csv")
    df.createTempView("sales")

    //interacting with catalogue

    val catalog = sparkSession.catalog

    //print the databases

    catalog.listDatabases().select("name").show()

    // print all the tables

    catalog.listTables().select("name").show()

    // is cached
    println(catalog.isCached("sales"))
    df.cache()
    println(catalog.isCached("sales"))

    // drop the table
    catalog.dropTempView("sales")
    catalog.listTables().select("name").show()

    // list functions
    catalog.listFunctions().select("name","description","className","isTemporary").show(100)
  }

}
