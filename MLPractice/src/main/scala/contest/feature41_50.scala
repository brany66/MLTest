package contest

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object feature41_50 {
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    val spark = SparkSession
      .builder.master("local[4]")
      .appName("train_test")
      .getOrCreate()

    val token = Set[String] (
      "0974_1", "0975_1", "0976_1", "3207_2", "0405_1",
      "1301_1", "1302_1", "0978_1", "1321_1", "1322_1"
    )

    val feature = spark.sparkContext.textFile("data/contest/feature.txt")
      .map(elem => elem.split("\\u0001")).filter(_.length == 2)
      .map(elem => (elem(0), elem(1).split("\t"))).filter(elem => token.contains(elem._1))

    //println(feature.count())

    val sample = feature.collect()

    val map : Array[Map[String, String]] = new Array[Map[String, String]](9)

    for (elem <- sample) {
      val value = elem._2.map(_.trim)
        .filter(seq => !seq.eq("-"))
        .groupBy(x => x).map(elem => (elem._1, elem._2.length))
        .toSeq.sortBy(_._2).reverse
        .mkString(" , ")
        //.mkString(" $$$ ")

      println(elem._1 + " |  " + value)
      println()
    }
    spark.stop()
  }
}
