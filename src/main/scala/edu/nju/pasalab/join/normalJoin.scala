package edu.nju.pasalab.join

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * Created by YWJ on 2016.12.18.
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  */
/**
  * http://dmtolpeko.com/2015/02/20/map-side-join-in-spark/
  * Join of two or more data sets is one of the most widely used operations you do with your data,
  * but in distributed systems it can be a huge headache. In general, since your data are distributed among many nodes,
  * they have to be shuffled before a join that causes significant network I/O and slow performance.
  *
  * Fortunately, if you need to join a large table (fact) with relatively small tables (dimensions) i.e.
  * to perform a star-schema join you can avoid sending all data of the large table over the network.
  * This type of join is called map-side join in Hadoop community. In other distributed systems,
  * it is often called replicated or broadcast join.
  */
object normalJoin {
  def main(args: Array[String]) {
    Logger.getLogger("akka").setLevel(Level.OFF)
    val spark = SparkSession.builder().master("local[4]")
      //.config("spark.kryo.registrator", "edu.nju.pasalab.mt.wordAlignment.usemgiza.MyKryoRegistrator")
      .appName("test").getOrCreate()

    // Fact table
    val flights = spark.sparkContext.parallelize(List(
      ("SEA", "JFK", "DL", "418",  "7:00"),
      ("SFO", "LAX", "AA", "1250", "7:05"),
      ("SFO", "JFK", "VX", "12",   "7:05"),
      ("JFK", "LAX", "DL", "424",  "7:10"),
      ("LAX", "SEA", "DL", "5737", "7:10")))

    // Dimension table
    val airports = spark.sparkContext.parallelize(List(
      ("JFK", "John F. Kennedy International Airport", "New York", "NY"),
      ("LAX", "Los Angeles International Airport", "Los Angeles", "CA"),
      ("SEA", "Seattle-Tacoma International Airport", "Seattle", "WA"),
      ("SFO", "San Francisco International Airport", "San Francisco", "CA")))

    // Dimension table
    val airlines = spark.sparkContext.parallelize(List(
      ("AA", "American Airlines"),
      ("DL", "Delta Airlines"),
      ("VX", "Virgin America")))

    val airportsMap = spark.sparkContext.broadcast(airports.map{case(a, b, c, d) => (a, c)}.collectAsMap)
    val airlinesMap = spark.sparkContext.broadcast(airlines.collectAsMap)

    val xx = flights.map{case(a, b, c, d, e) =>
      (airportsMap.value.get(a).get,
        airportsMap.value.get(b).get,
        airlinesMap.value.get(c).get, d, e)}


    xx.collect().foreach(elem => {
      println(elem._1 + " " + elem._2 + " " + elem._3 + " " + elem._4 + " " + elem._5  )
    })
    spark.stop
  }
}
