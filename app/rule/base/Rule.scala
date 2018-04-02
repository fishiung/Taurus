package rule.base

/**
  * 规则基类
  */
abstract class Rule {

  /**
    * 规则唯一id,转换规则T开头，验证规则V开头 后面随意
    */

  val id:String

  /**
    * 规则名称
    */
  val name: String

  /**
    * 规则描述
    */
  val desc: String

  /**
    * 可选填的数据格式 （时间格式，小数点...精确几位）
    * 注：如果该规则需要传入一个规则，那么在实现规则时应将该字段的初始值设为 "need"
    */
  var format: String

  /**
    * 具体的数据处理流程
    *
    * @param value 待处理数据
    * @return 返回处理结果字符串，如果是验证类型的规则，返回字符串 "true" "false"
    */
  def undergo(value: String): String

}
