package edu.nju.pasalab

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Encoders, SparkSession}

/**
  * Created by YWJ on 2016.12.13.
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  */
object statisticalInfo {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    val spark = SparkSession.builder().master("local")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .config("spark.kryoserializer.buffer", "128")
      .getOrCreate()

    import spark.implicits._
    val data = spark.read.text("data/test1k.txt").as[String]
    val maxLen = data.map(x => x.split("\\s+")).map(x => x.size).collect()

    println("maxLength " + maxLen.max)
    val sample = data.flatMap(e => e.split("\\s+"))(Encoders.STRING).groupByKey(x => x)
      .count()
    println("word number   " + sample.count())

    spark.stop()
  }
}
