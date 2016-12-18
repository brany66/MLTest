package edu.nju.pasalab.test

import java.util

import edu.nju.pasalab.Person
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Encoders, SparkSession}

import scala.collection.Iterator
/**
  * Created by YWJ on 2016.11.19.
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  */

object testEncoder {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    //val lo = LoggerFactory.getLogger(test.getClass)
    val spark = SparkSession.builder().master("local[4]")
      //.config("spark.kryo.registrator", "edu.nju.pasalab.mt.wordAlignment.usemgiza.MyKryoRegistrator")
      //.config("spark.storage.memoryFraction", args(8))
      //.config("spark.shuffle.memoryFraction", args(9))
      .appName("test").getOrCreate()


    //lo.info("\n*************************ABC******************\n")
    import spark.implicits._
    val list :util.ArrayList[Person] = new util.ArrayList[Person]()
    list.add(new Person("A", 1))
    list.add(new Person("B", 2))

    //val strEncoder = Encoders.product[String]

    val ds  = spark.read.text("data/data.txt").as[String]
    val gp =ds.flatMap(e => e.split("\\s+")).map(x => (x, 1.0)).groupByKey(elem => elem._1)
    val gp1 = gp.mapGroups((k, v) => (k, v.map(x => x._2).sum))
    gp1.show()

    val sample = ds.flatMap(e => e.split("\\s+")).groupByKey(x => x)
      .count()


    import spark.implicits._
    val ta = sample.as("A")
    val tb = sample.map(x => (x._1, (x._2 + 10)/ 5.0)).as("B")
    val ja = ta.joinWith(tb, $"value" === $"value","inner")

    ja.show()
//   import org.apache.spark.util.collection.AppendOnlyMap
//    val  xx = new AppendOnlyMap[String, Int]()
//    /** **/
//    val encoder2 = Encoders.tuple(Encoders.INT, Encoders.STRING)
//    //val xxx = Encoders.kryo[Person]
//    println(encoder2.clsTag + " *** " + encoder2.schema)
//    val encoder  = Encoders.product[Person]
//    println(encoder.schema)
//    val t = spark.createDataset(list)(encoder)
//
//
//    t.show()
//
//    /** **/
//    val df = Seq(("a", 10), ("a", 20), ("b", 1), ("b", 2), ("c", 1)).toDS()
//    val da = df.groupByKey(v => (v._1, "word"))
//
//    val xy = da.mapGroups{case (g, iter) =>(g._1, iter.map(_._2).sum)}
//
//    xy.show()
//    val res = da.flatMapGroups {
//      case (g, iter) => Iterator(g._1 + " " + iter.map(_._2).sum.toString)
//    }
//    res.show()

  }
}
