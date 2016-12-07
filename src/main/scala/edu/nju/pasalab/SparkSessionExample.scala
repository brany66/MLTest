package edu.nju.pasalab

import org.apache.spark.sql.SparkSession

/**
  * Created by YWJ on 2016.11.19.
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  */
object SparkSessionExample {

  def main(args: Array[String]) {

    val sparkSession = SparkSession.builder.
      master("local")
      .appName("spark session example")
      .getOrCreate()

    val df = sparkSession.read.option("header","true").csv("data/sales.csv")

    df.show()

  }

}
