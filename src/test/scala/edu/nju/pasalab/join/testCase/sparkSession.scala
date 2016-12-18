package edu.nju.pasalab.join.testCase

import org.apache.spark.sql.SparkSession

/**
  * Created by YWJ on 2016.12.18.
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  */
object sparkSession {

  val spark = SparkSession.builder().master("local[4]")
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .appName("join test").getOrCreate()
}
