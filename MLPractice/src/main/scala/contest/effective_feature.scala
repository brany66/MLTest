package contest

import java.io.PrintWriter

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object effective_feature {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)


    val spark = SparkSession
      .builder.master("local[6]")
      .appName("effective_feature_extract")
      .getOrCreate()

    val train = spark.sparkContext.textFile("data/contest/data.0.0/data.labeledAll.0.0")
      .mapPartitions(part => {
        part.map(elem => {
          val sp = elem.split(" \\*\\*\\* ")

          val value = sp(1).split(" \\$+ ")
            .map(elem => {
              val feature = elem.concat("#").split(" \\|+ ")
              //feature(1).substring(0, feature(1).lastIndexOf('#'))
              feature(0)
            })


          (sp(0), value)
        })
      }).flatMap(elem => elem._2)

    println(train.count())

    val test = spark.sparkContext.textFile("data/contest/data.0.0/data.test.unlabeled.0.0")
      .mapPartitions(part => {
        part.map(elem => {
          val sp = elem.split(" \\*\\*\\* ")

          val value = sp(1).split(" \\$+ ")
            .map(elem => {
              val feature = elem.concat("#").split(" \\|+ ")
              //feature(1).substring(0, feature(1).lastIndexOf('#'))
              feature(0)
            })


          (sp(0), value)
        })
      }).flatMap(elem => elem._2)

    println(test.count())

    val join = train.intersection(test).collect()

    val writer: PrintWriter = new PrintWriter("data/contest/statistics/effective_id.0", "UTF-8")

    for (elem <- join)
      writer.println(elem)
    writer.close()

    spark.stop()
  }
}
