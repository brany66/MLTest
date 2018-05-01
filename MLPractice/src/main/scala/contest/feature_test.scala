package contest

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object feature_test {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)


    val spark = SparkSession
      .builder.master("local[6]")
      .appName("feature_test")
      .getOrCreate()


    val train = spark.sparkContext.textFile("data/contest/data.0.0/data.unlabeled.0.0").mapPartitions(part => {
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
    }).mapPartitions(part => part.map(elem => elem._2.map(e => (e, elem._1))))

    for (elem <- train.take(10)) {
      for (e <- elem)
        print(e + "\t")
      println()
    }
      //.foreach(println)

    val train_feature = train.flatMap(x => x).reduceByKey((a, b) => a + " ||| " + b)

    println(train_feature.count())
    spark.stop()
  }
}
