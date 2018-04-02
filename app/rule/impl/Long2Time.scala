package rule.impl

import rule.base.Rule

class Long2Time extends Rule {
  //TODO
  override def undergo(value: String): String = value.substring(1)

  override val name: String = "Long型转指定格式可读时间"

  override val desc: String = "根据指定时间格式，将毫秒表示的时间转换为可读时间"

  override var format: String = "need"
  /**
    * 规则唯一id,转换规则T开头，验证规则V开头 后面随意
    */
  override val id: String = "T"+1
}
