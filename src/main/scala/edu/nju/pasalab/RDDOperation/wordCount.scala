package edu.nju.pasalab.RDDOperation

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SaveMode, DataFrame, SparkSession}

/**
  * Created by YWJ on 2016.12.30.
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  */
object wordCount {
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    val spark = SparkSession.builder().master("local[4]")
      //.config("spark.kryo.registrator", "edu.nju.pasalab.mt.wordAlignment.usemgiza.MyKryoRegistrator")
      .appName("test").getOrCreate()

    /**
      * Use DataSet
      */
    val ds  = spark.read.textFile("data/test1k.txt")
    import spark.implicits._
    val sample = ds.flatMap(e => e.split("\\s+")).groupByKey(x => x)
      .mapGroups((k, v) => (k ,v.length)).alias("A")
    sample.show(20)
    sample.write.mode(SaveMode.Overwrite).save("data/wordCount")
  }

  def withStopWordFilter(rdd : RDD[String], illegalTokens : Array[Char], stopWords : Set[String]) : RDD[(String, Int)] = {
    val tokens = rdd.flatMap(_.split(illegalTokens ++ Array[Char](' '))).map(_.trim.toLowerCase())

    val words = tokens.filter(token => !stopWords.contains(token) && (token.length > 0))
      .map((_, 1))
      .reduceByKey(_+_)

    words
  }

  import org.apache.spark.sql.functions._
  def minMeanSizePerZip(pandas : DataFrame) : DataFrame = {
    pandas.groupBy(pandas("zip")).agg(min(pandas("pandaSize")), mean(pandas("pandaSize")))
  }
}
