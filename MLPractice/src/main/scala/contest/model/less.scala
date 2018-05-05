package contest.model

import java.io.PrintWriter

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object less {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)


    val spark = SparkSession
      .builder.master("local[6]")
      .appName("train_test")
      .getOrCreate()

    /*val data = spark.sparkContext.textFile("data/contest/model_data/train.0.2_csv")
      .map(elem => elem.split("\t")(1).split(","))
      .filter(_.length == 61).map(elem => elem.mkString(","))

    println(data.count())

    val writer: PrintWriter = new PrintWriter("data/contest/model_data/train.0.3.csv", "UTF-8")
    val local = data.collect()
    for (elem <- local)
      writer.println(elem)

    writer.close()*/

    val label = spark.sparkContext.textFile("data/contest/train.1.0.csv").map(elem => {
      val sp = elem.split("\t")(1).split(",")
      sp.map(_.toFloat)
    })

    println(label.count())
    spark.stop()
  }
}
