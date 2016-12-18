package edu.nju.pasalab.join.testCase

import edu.nju.pasalab.join.defaultLeanReplication
import org.apache.log4j.{Level, Logger}
import org.scalatest.FunSpec

/**
  * Created by YWJ on 2016.12.18.
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  */
class leanReplicationTest extends FunSpec{
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)
  lazy val sc = sparkSession.spark

  describe("leanReplication ") {
    it("should get correct replication") {
      val replicator1 = defaultLeanReplication(1)
      val replicator2 = defaultLeanReplication(1e-6)
      assert(replicator1.getReplication(5, 4, 1) === (1, 1))
      assert(replicator1.getReplication(80, 5, 90) === (5, 80))
      assert(replicator2.getReplication(10, 50, 80) === (1, 1))
    }

    it("should not return replications bigger than numPartitions") {
      val replicator = defaultLeanReplication(50)
      assert(replicator.getReplication(5, 4, 100) === (100, 100))
      assert(replicator.getReplication(5, 1, 100) === (50, 100))
    }
  }
}
