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


    val data = spark.read.text("data/contest/feature_context.0.0").rdd

    spark.stop()
  }
}
