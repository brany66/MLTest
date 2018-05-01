package contest

import java.io.PrintWriter

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object split {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)


    val spark = SparkSession
      .builder.master("local[6]")
      .appName("split_test")
      .getOrCreate()


    val token  = Set[String] (
      "0424", "10002", "10004", "1115", "1117",
           "1814", "1815",   "183",  "1840", "1850",
           "190",   "191",   "192",  "193",  "2174",
           "2403",  "2404",  "2405", "314", "3193")
      .flatMap(elem => Array(elem + "_1", elem + "_2"))


    val data = spark.sparkContext.textFile("data/contest/data.0.0/data.unlabeled.0.0")
      .mapPartitions(part => {
      part.map(elem => {
        val sp = elem.split(" \\*\\*\\* ")

        val value = sp(1).split(" \\$+ ")
          .map(elem => {
          val feature = elem.concat("#").split(" \\|+ ")

          (feature(0), feature(1).substring(0, feature(1).lastIndexOf('#')))
        }).filter(elem => token.contains(elem._1))


        (sp(0), value)
      })
    }).filter(_._2.length > 0)
      .map(elem => {
      val arr = elem._2.map(elem => {
        val key = elem._1.substring(0, elem._1.lastIndexOf("_"))
        (key, elem._2)
      })

      (elem._1, arr)
    })

    val token2  = List[String] (
      "0424", "10002", "10004", "1115", "1117",
      "1814", "1815",   "183",  "1840", "1850",
      "190",   "191",   "192",  "193",  "2174",
      "2403",  "2404",  "2405", "314", "3193")

    for (token <- token2) {
      val writer: PrintWriter = new PrintWriter("data/contest/statistics/" + token + ".result.0", "UTF-8")

      val take = data.map(elem => {
        val value = elem._2.filter(e => e._1.equals(token))
        (elem._1, value)
      }).filter(_._2.length > 0).collect()


      for (item <- take) {
        writer.print(item._1 + " *** ")

        writer.print(item._2(0)._1 + " ||| " + item._2(0)._2)
        for (i <- 1 until item._2.length) {
          writer.print(" $$$ " + item._2(i)._1 + " ||| " + item._2(i)._2)
        }
        writer.println()
      }
      writer.close()
    }


    spark.stop()
  }
}
