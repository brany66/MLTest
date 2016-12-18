package edu.nju.pasalab.test

/**
  * Created by YWJ on 2016.12.17.
  * Copyright (c) 2016 NJU PASA Lab All rights reserved.
  */
object Schema {

  case class wordCount (word :String, count : Long)
  case class freq(word : String, freq : Double)
}
