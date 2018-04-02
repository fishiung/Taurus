package rule.util

object RuleUtil {
  def ^(a:Int):Boolean = a match {
    case 1=> true
    case 0=>false
    case _ => throw new Exception("路径下出现问题：")
  }
  def ^[T](a:Option[T]):Boolean = a match {
    case Some(a)=> true
    case None=>false
    case _ => throw  new Exception("参数不是0 | 1")
  }

}
