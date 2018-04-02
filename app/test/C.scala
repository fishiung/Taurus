package test

object C {


  def test(s:String*):Unit={
    val a ="44"
   // a.split(",").foreach(println(_))
    val res = (a.split(",").toSet ++ s).filter(!_.isEmpty).mkString(",")

    println(res)
  }

  def main(args: Array[String]): Unit = {


    test("2","1","33")

  }
}
