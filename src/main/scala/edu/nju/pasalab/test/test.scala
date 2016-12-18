package edu.nju.pasalab.test

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * Created by YWJ on 2016.12.17.
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  */
object test {
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    //val lo = LoggerFactory.getLogger(test.getClass)
    val spark = SparkSession.builder().master("local[4]")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .config("spark.kryoserializer.buffer", "128")
      //.config("spark.kryo.registrator", "edu.nju.pasalab.mt.wordAlignment.usemgiza.MyKryoRegistrator")
      //.config("spark.storage.memoryFraction", args(8))
      //.config("spark.shuffle.memoryFraction", args(9))
      .appName("test").getOrCreate()
  }
}
