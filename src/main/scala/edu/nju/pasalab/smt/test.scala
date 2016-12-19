package edu.nju.pasalab.smt

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * Created by YWJ on 2016.12.18.
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  */
object test {
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    val spark = SparkSession.builder().master("local[4]")
      //.config("spark.kryo.registrator", "edu.nju.pasalab.mt.wordAlignment.usemgiza.MyKryoRegistrator")
      .appName("smt test").getOrCreate()

   // val f = new File("data")
    val rdd = spark.sparkContext.textFile("data/step.final.part00000001.align")
  }
}
