package edu.nju.pasalab.join.testCase
import edu.nju.pasalab.join.{defaultLeanReplication, leanReplication, leanJoinOperation}
import org.apache.log4j.{Level, Logger}
import org.scalatest.FunSpec
import edu.nju.pasalab.join.Dsl._
import org.apache.spark.Partitioner.defaultPartitioner
/**
  * Created by YWJ on 2016.12.18.
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  */
case object DummySkewReplication extends leanReplication {
  override def getReplication(left: Long, right: Long, numPartitions: Int) = (2, 2)
}
class leanJoinTest extends FunSpec {
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)
  lazy val sc = sparkSession.spark

  lazy val rdd1 = sc.sparkContext.parallelize(Array(1, 1, 2, 3, 4)).map(s => (s, 1)).repartition(2)
  lazy val rdd2 = sc.sparkContext.parallelize(Array(1, 1, 6, 4, 5)).map(s => (s, 2)).repartition(2)
  //rdd1.join(rdd2)
  describe("SkewJoin") {
    it("should inner join two datasets using leanJoin correctly") {
      scala.Predef.assert(rdd1.leanJoin(rdd2, defaultPartitioner(rdd1, rdd2), defaultLeanReplication(1)).sortByKey(true).collect.toList ==
        Seq((1, (1, 2)), (1, (1, 2)), (1, (1, 2)), (1, (1, 2)), (4, (1, 2))))
    }

    it("should left join two datasets using leanLeftOutJoin correctly") {
      scala.Predef.assert(rdd1.leanLeftOutJoin(rdd2, defaultPartitioner(rdd1, rdd2), defaultLeanReplication(1)).sortByKey(true).collect.toList ==
        Seq((1, (1, Some(2))), (1, (1, Some(2))), (1, (1, Some(2))), (1, (1, Some(2))), (2, (1, None)), (3, (1, None)), (4, (1, Some(2)))))
    }

    it("should right join two datasets using leanRightOutJoin correctly") {
      scala.Predef.assert(rdd1.leanRightOutJoin(rdd2, defaultPartitioner(rdd1, rdd2), defaultLeanReplication(1)).sortByKey(true).collect.toList ==
        Seq((1, (Some(1), 2)), (1, (Some(1), 2)), (1, (Some(1), 2)), (1, (Some(1), 2)), (4, (Some(1), 2)), (5, (None, 2)), (6, (None, 2))))
    }
  }
}
