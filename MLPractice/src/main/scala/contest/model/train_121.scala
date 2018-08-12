package contest.model

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object train_121 {

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)


  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder.master("local[4]")
      .appName("train_test")
      .getOrCreate()

    /*val label = spark.sparkContext.textFile("data/contest/clean/data.labeledAll.0.0").map(elem => {
      val sp = elem.split(" \\*\\*\\* ")

      val value = sp(2).split("\\s+")

      (sp(0), value)
    }).filter(_._2.length == 5)
      .map(elem => (elem._1, elem._2.map(_.toFloat).mkString(",")))

    println(label.count())

    val data = spark.sparkContext.textFile("data/contest/1.3.2/train_csv_44").map(elem => {
      val arr = elem.split("\t")

      val value = arr(1).split(",")
      val tmp = new Array[String](value.length - 1)
      Array.copy(value, 0, tmp, 0, value.length - 1)

      (arr(0), tmp)
    }).filter(_._2.length == 44)
     .map(elem => (elem._1, elem._2.mkString(",")))

    println(data.count() + "\n" + data.first()._2)

    val join = data.join(label).map(elem => {
      elem._1 + "," + elem._2._1 + "," + elem._2._2
    })

    println(join.count())


    join.repartition(1).saveAsTextFile("data/contest/1.3.2/train.1.3.2")
    */

    val test = spark.sparkContext.textFile("data/contest/1.3.2/test_csv_44").map(elem => {
      val arr = elem.split("\t")
      (arr(0), arr(1))
    }).map(elem => elem._1 + "," + elem._2)

    println(test.count())


    test.repartition(1).saveAsTextFile("data/contest/1.3.2/test.1.3.2")


    spark.stop()
  }
}
