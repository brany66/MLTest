package edu.nju.pasalab.join.testCase

import org.apache.log4j.{Level, Logger}
import org.scalatest.FunSpec
import edu.nju.pasalab.join.Dsl._
import scala.Predef._
/**
  * Created by YWJ on 2016.12.18.
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  */
class blockJoinTest extends FunSpec {
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)
  lazy val sc = sparkSession.spark

  describe("blockJoin") {
    it ("do block inner join") {
      val rddA = sc.sparkContext.parallelize(Array((1, 1), (1, 2), (2, 1), (2, 2), (3, 1)))
      val rddB = sc.sparkContext.parallelize(Array((1, 'x'), (2, 'y'), (2, 'z'), (4, 'w')))

      val joined = rddA.blockJoin(rddB, 5, 5).collect()
      scala.Predef.assert(joined.size == 6)
      scala.Predef.assert(joined.toSet == Set(
        (1, (1, 'x')),
        (1, (2, 'x')),
        (2, (1, 'y')),
        (2, (1, 'z')),
        (2, (2, 'y')),
        (2, (2, 'z'))
      ))
    }

    it ("do block left out join") {
      val rddA = sc.sparkContext.parallelize(Array((1, 1), (1, 2), (2, 1), (2, 2), (3, 1)))
      val rddB = sc.sparkContext.parallelize(Array((1, 'x'), (2, 'y'), (2, 'z'), (4, 'w')))

      val joined = rddA.blockLeftOutJoin(rddB, 5).collect()
      scala.Predef.assert(joined.size == 7)
      scala.Predef.assert(joined.toSet == Set (
        (1, (1, Some('x'))),
        (1, (2, Some('x'))),
        (2, (1, Some('y'))),
        (2, (1, Some('z'))),
        (2, (2, Some('y'))),
        (2, (2, Some('z'))),
        (3, (1, None))
      ))
    }

    it ("do block right join") {
      val rddA = sc.sparkContext.parallelize(Array((1, 1), (1, 2), (2, 1), (2, 2), (3, 1)))
      val rddB = sc.sparkContext.parallelize(Array((1, 'x'), (2, 'y'), (2, 'z'), (4, 'w')))

      val joined = rddA.blockRightOutJoin(rddB, 5).collect()
      scala.Predef.assert(joined.size == 7)
      scala.Predef.assert(joined.toSet == Set (
        (1, (Some(1), 'x')),
        (1, (Some(2), 'x')),
        (2, (Some(1), 'y')),
        (2, (Some(1), 'z')),
        (2, (Some(2), 'y')),
        (2, (Some(2), 'z')),
        (4, (None, 'w'))
      ))
    }
  }
}
