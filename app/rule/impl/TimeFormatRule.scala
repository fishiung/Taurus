package rule.impl

import rule.base.Rule

class TimeFormatRule extends Rule {

  override val name: String = "格式化时间"

  override def undergo(value: String): String = ???

  override val desc: String = "将字符串形式时间根据指定格式进行格式化"

  override var format: String = "need"
  /**
    * 规则唯一id,转换规则T开头，验证规则V开头 后面随意
    */
  override val id: String = "T2"
}
