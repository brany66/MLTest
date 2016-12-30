package edu.nju.pasalab.RDDOperation

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * Created by YWJ on 2016.12.30.
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  */
object joinOperation {
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    val spark = SparkSession.builder().master("local[4]")
      //.config("spark.kryo.registrator", "edu.nju.pasalab.mt.wordAlignment.usemgiza.MyKryoRegistrator")
      .appName("test").getOrCreate()

    /**
      * DataSet Join
      */
    val ds  = spark.read.textFile("data/test1k.txt")
    val sample = ds.flatMap(e => e.split("\\s+")).groupByKey(x => x)
      .mapGroups((k, v) => (k ,v.length)).alias("A")


    sample.printSchema()
    import spark.implicits._
    spark.sqlContext.sql("set spark.sql.caseSensitive=false")
    val tb = sample.map(x => (x._1, (x._2 + 10)/ 5.0)).alias("B")
    println(tb.schema)
    val ja = sample.joinWith(tb, $"A._1" === $"B._1", "inner").map(elem => (elem._1._1, (elem._1._2, elem._2._2)))

    ja.show(20)
  }

  /**
    * RDD Join
    * scoreRDD : id, score
    * addressRDD : id, address
    * Spark default join is a shuffled hash join, shuffled hash join ensures that data on each partition will contain
    * the same key by partitioning the second dataset with the same default partitioner as the first, it needs shuffle, and can be avoid if:
    *
    * 1. Both RDD have a known partitioner
    * 2. One of the datasets is small enough to fit in memory, in this case we can do a broadcast join
    */
  def JoinScoreWithAddress1(scoreRDD : RDD[(Long, Double)], addressRDD : RDD[(Long, String)]) : RDD[(Long, (Double, String))] = {
    scoreRDD.join(addressRDD)
      .reduceByKey((x, y) => if (x._1 > y._1) x else y)
  }
  def JoinScoreWithAddressFilter(scoreRDD : RDD[(Long, Double)], addressRDD : RDD[(Long, String)]) : RDD[(Long, (Double, String))] = {
    scoreRDD.reduceByKey((x, y) => if(x > y) x else y).join(addressRDD)
  }
}
