package edu.nju.pasalab.test

import java.util

import edu.nju.pasalab.Person
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Encoders, SparkSession}
/**
  * Created by YWJ on 2016.11.19.
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  */

object testEncoder {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    val spark = SparkSession.builder().master("local[4]")
      .config("spark.kryo.registrationRequired", "true")
      //.config("spark.kryo.registrator", "edu.nju.pasalab.mt.wordAlignment.usemgiza.MyKryoRegistrator")
      .appName("test").getOrCreate()


    //lo.info("\n*************************ABC******************\n")
    import spark.implicits._
    val list :util.ArrayList[Person] = new util.ArrayList[Person]()
    list.add(new Person("A", 1))
    list.add(new Person("B", 2))

    val ds  = spark.read.textFile("data/data.txt")


    val sample = ds.flatMap(e => e.split("\\s+")).groupByKey(x => x).mapGroups((k, v) => (k ,v.length)).alias("A")

    sample.show(100)

    import spark.implicits._
    //spark.sqlContext.sql("set spark.sql.caseSensitive=false")
    val tb = sample.map(x => (x._1, (x._2 + 10)/ 5.0)).alias("B")
    println(tb.schema)
    val ja = sample.joinWith(tb, $"A._1" === $"B._1", "inner").map(elem => (elem._1._1, (elem._1._2, elem._2._2)))

    ja.show(20)

//    val encoder2 = Encoders.tuple(Encoders.INT, Encoders.STRING)
//    //val xxx = Encoders.kryo[JDK8Test.Person]
//    println(encoder2.clsTag + " *** " + encoder2.schema)
//    val encoder  = Encoders.product[JDK8Test.Person]
//    println(encoder.schema)
//    val t = spark.createDataset(list)(encoder)


    //t.show()


  }
}
