package contest

import java.io.PrintWriter

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

import scala.collection.mutable

object test {
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)


    val spark = SparkSession
      .builder.master("local[6]")
      .appName("test")
      .getOrCreate()

    val part1 = spark.read.text("data/contest/round1_part1_0408.txt").rdd
    val filter = part1.first()

    val data1 = part1.filter(_ != filter).mapPartitions(part => {
      part.map(elem => {

        val sp = elem.mkString("$").concat("#").split('$')

        (sp(0), (sp(1) + "_1", sp(2)).productIterator.mkString("$"))
      })
    }).reduceByKey((a, b) => a + "|||" + b).cache()

    val data2 = spark.read.text("data/contest/round1_part2_0408.txt").rdd
      .filter(_ != filter)
      .mapPartitions(part => {
        part.map(elem => {

          val sp = elem.mkString("$").concat("#").split('$')
          (sp(0), (sp(1)+ "_2", sp(2)).productIterator.mkString("$"))
        })
      }).reduceByKey((a, b) => a + "|||" + b).cache()

    println(data1.count() + " " + data2.count())


    val join = data1.join(data2)
      .mapPartitions(part => {
      part.map(elem => {
        (elem._1, elem._2._1 + "|||" + elem._2._2)
      })
    }).reduceByKey((a, b) => a + "|||" + b).mapPartitions(part => {
      part.map(elem => {

        val sb = new mutable.StringBuilder(128)
        //sb.append(elem._1).append(" *** ")

        val sp = elem._2.split("\\|\\|\\|").map(elem => {
          val tmp = elem.split('$')
          (tmp(0), tmp(1).substring(0, tmp(1).length - 1))
        })

        val map = new mutable.HashMap[String, String]()

        for (elem <- sp) {
            if (!map.contains(elem._1))
              map += (elem._1 -> elem._2)
            else {
              val value = map.getOrElse(elem._1, "") + " ### " + elem._2
              map.-=(elem._1)
              map += (elem._1 -> value)
            }
          }

        for ((k, v) <- map) {
          sb.append(k).append(" ||| ").append(v).append(" $$$ ")
        }
        val res = sb.toString()

        (elem._1, res.substring(0, res.lastIndexOf("$$$")))
      })
    })

    val tmp = spark.read.csv("data/contest/round1_train_0408.csv").rdd
    val head = tmp.first()

    val test = spark.read.csv("data/contest/round1_test_a_0409.csv").rdd.filter(_ != head).mapPartitions(part => {
      part.map(elem => {
        val arr = elem.mkString(",").split(",")

        (arr(0), "")
      })
    })

    val testRest = join.join(test).map(elem => elem._1 + " *** " + elem._2._1).collect()

    println(testRest.length)
    val writer_test : PrintWriter = new PrintWriter("data/contest/data.test.unlabeled.0.0", "UTF-8")

    for (elem <- testRest)
      writer_test.println(elem)

    writer_test.close()

    /*join.map(elem => elem._1 + " *** " + elem._2).saveAsTextFile("data/data.0.0")



    val regex : scala.util.matching.Regex =  "^(\\-|\\+)?\\d+(\\.\\d+)?$".r
    val dregex: scala.util.matching.Regex = "-?([0-9]+).([0-9]+)|-?([0-9]+)".r
    val user = tmp.filter(_ != head).mapPartitions(part => {
      part.map(elem => {
        val arr = elem.mkString(",").split(",")


        (arr(0), (arr(1), arr(2), arr(3), arr(4), arr(5)))
      })
    }).filter(elem => regex.findFirstMatchIn(elem._2._1).isDefined)
      .filter(elem => regex.findFirstMatchIn(elem._2._2).isDefined)
      .map(elem => (elem._1, (elem._2._1.toInt, elem._2._2.toInt, elem._2._3, elem._2._4, elem._2._5)))
      .filter(elem => elem._2._1 - elem._2._2 > 0)
      .filter(elem => elem._2._1 >= 40 && elem._2._1 <= 300)
      .filter(elem => elem._2._2 >= 20 && elem._2._2 <= 200)
      .map(elem => {
        val col = dregex.findAllIn(elem._2._3).toArray.map(_.toDouble)

        (elem._1, (elem._2._1, elem._2._2, col(0), elem._2._4, elem._2._5.toFloat))
    })
      .filter(elem => elem._2._5 > 0)
      .map(elem => {(elem._1, elem._2.productIterator.mkString("\t"))})


    val unlabeled = join.subtractByKey(user)

    println(unlabeled.count() + " " + unlabeled.first()._2)

    val labeled = join.join(user).map(elem => {
      val sb = new mutable.StringBuilder(256)

      sb.append(elem._1).append(" *** ").append(elem._2._1).append(" *** ").append(elem._2._2).toString()
    }).collect()

    val writer_all : PrintWriter = new PrintWriter("data/contest/data.labeledAll.0.0", "UTF-8")
    val writer_2 : PrintWriter = new PrintWriter("data/contest/data.labeled-2.0.0", "UTF-8")
    val writer_8 : PrintWriter = new PrintWriter("data/contest/data.labeled-8.0.0", "UTF-8")

    for (i <- labeled.indices) {
      writer_all.println(labeled(i))

      if (i % 5 == 0)
        writer_2.println(labeled(i))
      else
        writer_8.println(labeled(i))
    }

    writer_2.close()
    writer_8.close()
    writer_all.close()*/

    spark.stop()
  }

}
