package db.model
//===========================================form===============================
/**
  * 创建目标字段 表单字段
  * @param cnName
  * @param enName
  * @param orId
  * @param otId
  * @param ttId
  * @param atype
  * @param isPK
  * @param isMP
  * @param isMultValue
  * @param isLongValue
  * @param rulePipeline
  */
case class TargetAttrForm(cnName:String,enName:String,orId:Int,otId:Int,ttId:Int,atype:String, isPK:Option[Int],isMP:Option[Int],
                          isMultValue:Option[Int],isLongValue:Option[Int],rulePipeline:Option[String])

//===========================================dataBase===========================
/** 数据源信息表
  * @param url
  * @param username
  * @param password
  * @param dtype
  * @param id
  */
case class DataSourceInfo(url: String, username: String, password: String, dtype: String, id: Option[Int])

/**
  * 原始表
  * @param enName
  * @param rowsum
  * @param dsize
  * @param id
  * @param dsId
  */
case class OriginTable(enName: String, rowsum: Long, dsize: Long, id: Option[Int], dsId: Int)

/**
  * 原始字段表
  * @param enName
  * @param atype
  * @param sample1
  * @param sample2
  * @param sample3
  * @param isPK
  * @param id
  * @param otId
  * @param taId
  */
case class OriginAttr(enName: String, atype: String, var sample1: String, var sample2: String, var sample3: String, isPK: Boolean, id: Option[Int], otId: Int,var taId:Int) {
  def setSample(value: String): Unit = if (sample1 == "") sample1 = value else if (sample2 == "") sample2 = value else sample3 = value
}

/**
  * 目标表
  * @param enName
  * @param cnName
  * @param ttpye
  * @param version
  * @param parents
  * @param subjectId
  * @param objectId
  * @param id
  * @param attrs
  * @param isInited
  * @param isCalc
  * @param isWaitUpdate
  */
case class TargetTable(enName: String, cnName: String, ttpye: String,
                       version: Int = 0, var parents: String = "", subjectId:Int = -1,
                       objectId: Int = -1, id: Option[Int] = None,var attrs:String="",
                       isInited: Boolean = false, isCalc: Boolean = false, isWaitUpdate: Boolean = false)

/**
  * 目标字段表
  * @param enName
  * @param cnName
  * @param atype
  * @param orId
  * @param otId
  * @param isPK
  * @param isMP
  * @param isMultValue
  * @param isLongValue
  * @param id
  * @param rulePipeline
  */
case class TargetAttr(enName:String,cnName:String,atype:String,orId:Int,otId:Int,
                      isPK:Boolean=false,isMP:Boolean = false,isMultValue:Boolean=false,
                      isLongValue:Boolean = false,id:Option[Int],rulePipeline:Option[String])


object RDFType {
  val Entity: String = "Entity"
  val Event: String = "Event"
  val Relation: String = "Relation"
}

object TargetAttrType{
  val string:String="String"
  val time:String = "Time"
  val geo:String="Geo"
}


