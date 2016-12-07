package edu.nju.pasalab.dataSet

import org.apache.spark.sql.SparkSession
/**
  * Created by YWJ on 2016.11.19.
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  * Logical Plans for Dataframe and Dataset
  */
object DatasetVsDataFrame {

  case class Sales(transactionId:Int,customerId:Int,itemId:Int,amountPaid:Double)

  def main(args: Array[String]) {

    val sparkSession = SparkSession.builder.
      master("local")
      .appName("example")
      .getOrCreate()

    val sparkContext = sparkSession.sparkContext
    import sparkSession.implicits._


    //read data from text file

    val df = sparkSession.read.option("header","true").option("inferSchema","true").csv("data/sales.csv")
    val ds = sparkSession.read.option("header","true").option("inferSchema","true").csv("data/sales.csv").as[Sales]


    val selectedDF = df.select("itemId")

    val selectedDS = ds.map(_.itemId)

    println(selectedDF.queryExecution.optimizedPlan.numberedTreeString)

    println(selectedDS.queryExecution.optimizedPlan.numberedTreeString)


  }

}
