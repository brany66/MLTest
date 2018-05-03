package contest

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
      val arr = elem.split("\\s+")

      val value = arr(1).split(",")
      val tmp = new Array[String](value.length - 1)
        Array.copy(value, 0, tmp, 0, value.length - 1)
      (arr(0), tmp.mkString(","))
    })

    println(data.count() + " " + data.first()._2)



    val label = spark.sparkContext.textFile("data/contest/clean/data.labeledAll.0.0").map(elem => {
      val sp = elem.split(" \\*\\*\\* ")
 
      val value = sp(2).split("\\s+")


      (sp(0), value)
    }).filter(_._2.length == 5)
      .map(elem => (elem._1, elem._2.mkString(",")))


    println(label.count())


  val join = data.join(label).map(elem => {
      elem._1 + "\t" + elem._2._1 + "," + elem._2._2
    }).collect()

    val writer: PrintWriter = new PrintWriter("data/contest/train.0.2_csv", "UTF-8")

    for (elem <- join) {
      writer.println(elem)
    }
    writer.close()

    spark.stop()

  }

}
