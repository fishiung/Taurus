package rule.impl

import rule.base.Rule

class TrimRule extends Rule {
  override def undergo(value: String): String = value.trim

  override val name: String = "Trim规则"
  //Todo  后续添加 根据指定字符trim
  override val desc: String = "暂时只能去掉字段两端的空格"

  override var format: String = _
  /**
    * 规则唯一id,转换规则T开头，验证规则V开头 后面随意
    */
  override val id: String = "T3"
}
