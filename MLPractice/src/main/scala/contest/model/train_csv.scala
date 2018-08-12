package contest.model

import java.io.PrintWriter

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object train_csv {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)


    val spark = SparkSession
      .builder.master("local[6]")
      .appName("train_test")
      .getOrCreate()

    val data = spark.sparkContext.textFile("data/contest/model_data/train_csv").map(elem => {
      val arr = elem.split("\t")

      val value = arr(1).split(",")
      val tmp = new Array[String](value.length - 1)
      Array.copy(value, 0, tmp, 0, value.length - 1)

      (arr(0), tmp)
    }).filter(_._2.length == 80).map(elem => (elem._1, elem._2.mkString(",")))

    println(data.count() + " " + data.first()._2)



    val label = spark.sparkContext.textFile("data/contest/clean/data.labeledAll.0.0").map(elem => {
      val sp = elem.split(" \\*\\*\\* ")
 
      val value = sp(2).split("\\s+")

      (sp(0),value)
    }).filter(_._2.length == 5)
      .map(elem => (elem._1, elem._2.map(_.toFloat).mkString(",")))


    println(label.count())

    val join = data.join(label).map(elem => {
      elem._1 + "," + elem._2._1 + "," + elem._2._2
    }).collect()

    println(join.length)

    println(join(0).split(",").length + "   " + join(0))
    val writer: PrintWriter = new PrintWriter("data/contest/model_data/train.1.0.csv", "UTF-8")

    for (elem <- join) {
      writer.println(elem)
    }
    writer.close()

    spark.stop()

  }

}
