package edu.nju.pasalab

import java.util

import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Encoders
import scala.collection.Iterator

//import org.apache.spark.sql.Encoders
import java.util._
/**
  * Created by YWJ on 2016.11.19.
  * Contact at wyang@smail.nju.edu.cn
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  */
object testEncoder {
  def main(args: Array[String]): Unit = {
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


    //lo.info("\n*************************ABC******************\n")
    import spark.implicits._
    val list :util.ArrayList[Person] = new util.ArrayList[Person]()
    list.add(new Person("A", 1))
    list.add(new Person("B", 2))

    //val strEncoder = Encoders.product[String]

    val ds  = spark.read.text("data/test1k.txt").as[String]
    //val A = spark.createDataset(List("A"))(Encoder[])
    val sample = ds.flatMap(e => e.split("\\s+"))(Encoders.STRING).groupByKey(x => x)
      .count()
    sample.show()

   import  org.apache.spark.util.collection.AppendOnlyMap
    val  xx = new AppendOnlyMap[String, Int]()
    /** **/
    val encoder2 = Encoders.tuple(Encoders.INT, Encoders.STRING)
    //val xxx = Encoders.kryo[Person]
    println(encoder2.clsTag + " *** " + encoder2.schema)
    val encoder  = Encoders.product[Person]
    println(encoder.schema)
    val t = spark.createDataset(list)(encoder)
    t.show()
    /** **/
    val df = Seq(("a", 10), ("a", 20), ("b", 1), ("b", 2), ("c", 1)).toDS()
    val da = df.groupByKey(v => (v._1, "word"))
    val res = da.flatMapGroups {
      case (g, iter) => Iterator(g._1 + " " + iter.map(_._2).sum.toString)
    }
    res.show()

  }
}
