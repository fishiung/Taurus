package test

import rule.base.Rule

import scala.collection.mutable.ArrayBuffer

object D {
  def main(args: Array[String]): Unit = {

    import rule.util._

    //D:\workspace\idea\taurus\target\scala-2.11\classes\rule

    //println(ScanRules.getClass.getClassLoader.getResources("rule").nextElement().getProtocol)
    ScanRules.load("rule.impl").foreach(r=> println(r.getClass.getName))

  }
}
