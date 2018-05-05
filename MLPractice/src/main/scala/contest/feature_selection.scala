package contest

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object feature_selection {

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)


    val spark = SparkSession
      .builder.master("local[1]")
      .appName("train_test")
      .getOrCreate()


    /*val token  = Set[String] (
      "3430_2", "0118_1", "0117_1", "3195_2", "3192_2",   "3196_2",  "3190_2", "300005_2", "0115_1")


    val data = spark.sparkContext.textFile("data/contest/feature.txt")
      .map(elem => elem.split("\\u0001")).filter(_.length == 2)
        .map(elem => (elem(0), elem(1).split("\t"))).filter(elem => token.contains(elem._1))

    val sample = data.take(9)

    for (elem <- sample) {
      println(elem._1 + " |||  " + elem._2.length + " ||| " + elem._2.distinct.length)
      println("\n************************************************************************************\n")
    }*/

//    val regex : scala.util.matching.Regex =  "^(\\-|\\+)?\\d+(\\.\\d+)?$".r
//    val dregex: scala.util.matching.Regex = "-?([0-9]+).([0-9]+)|-?([0-9]+)".r

    val train = spark.sparkContext.textFile("data/contest/train.0.2_csv").map(elem => {
      //textFile("data/contest/train_feature_with_label.csv").map(elem => {
      val sp = elem.split("\t")(1).split(",")
      sp
    }).filter(_.length > 0).map(elem => {
      //println(elem.mkString(" "))
      elem.map(_.toFloat)
    })
    println(train.count())

    /*val take = train.take(100)
    for (elem <- take)
      {
        for (e <- elem)
          print(e + "   ")
        println()
      }*/
    spark.stop()
  }
}
