package rule.base

class Pipeline(var value:String) {

  def ->(rule:Rule):Pipeline={
    rule.undergo(value)
    this
  }

  def out:String = value
}
