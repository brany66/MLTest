package contest

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object feature_selection {

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)


    val spark = SparkSession
      .builder.master("local[6]")
      .appName("train_test")
      .getOrCreate()


    val data = spark.sparkContext.textFile("data/contest/feature.txt")
      .map(elem => elem.split("\\u0001")).filter(_.length == 2)
        .map(elem => (elem(0), elem(1).split("\t")))

    println(data.count())

    val sample = data.take(100)

    for (elem <- sample) {
      println(elem._1 + " *** " + elem._2.length)
      for (e <- elem._2)
        print(e + "\t")

      println("\n************************************************************************************\n")
    }

    spark.stop()
  }
}
