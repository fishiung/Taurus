package db

import db.model._
import slick.jdbc.{JdbcProfile, MySQLProfile}

trait TaurusTables {
  protected val driver: JdbcProfile = MySQLProfile

  import driver.api._

  class DataSourceInfoTable(tag: Tag) extends Table[DataSourceInfo](tag, "data_source_info") {
    def id = column[Int]("id", O.SqlType("int"), O.PrimaryKey, O.AutoInc)

    def url = column[String]("url", O.SqlType("varchar(255)"))

    def username = column[String]("uname", O.SqlType("varchar(50)"))

    def password = column[String]("pwd", O.SqlType("varchar(50)"))

    def dtype = column[String]("data_source_type", O.SqlType("varchar(10)"))

    override def * = (url, username, password, dtype, id.?) <> (DataSourceInfo.tupled, DataSourceInfo.unapply)
  }

  class OriginTableTable(tag: Tag) extends Table[OriginTable](tag, "origin_table") {
    def id = column[Int]("id", O.SqlType("int"), O.PrimaryKey, O.AutoInc)

    def dsId = column[Int]("dsId", O.SqlType("int"))

    def enName = column[String]("en_name", O.SqlType("varchar(60)"))

    def rowsum = column[Long]("rowsum", O.SqlType("long"))

    def dsize = column[Long]("dsize", O.SqlType("long"))

    def fk = foreignKey("ds_ot", dsId, dataSourceInfos)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    override def * = (enName, rowsum, dsize, id.?, dsId) <> (OriginTable.tupled, OriginTable.unapply)
  }

  class OriginAttrTable(tag: Tag) extends Table[OriginAttr](tag, "origin_attr") {
    def id = column[Int]("id", O.SqlType("int"), O.PrimaryKey, O.AutoInc)

    def otId = column[Int]("otId", O.SqlType("int"))

    def taId = column[Int]("taId", O.SqlType("int"))

    def enName = column[String]("en_name", O.SqlType("varchar(60)"))

    def atype = column[String]("type", O.SqlType("varchar(30)"))

    def sample1 = column[String]("sample1", O.SqlType("text"))

    def sample2 = column[String]("sample2", O.SqlType("text"))

    def sample3 = column[String]("sample3", O.SqlType("text"))

    def isPK = column[Boolean]("isPK", O.SqlType("bool"))

    def fk = foreignKey("or_ot", otId, originTables)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    override def * = (enName, atype, sample1, sample2, sample3, isPK, id.?, otId, taId) <> (OriginAttr.tupled, OriginAttr.unapply)
  }

  class TargetTableTable(tag: Tag) extends Table[TargetTable](tag, "target_table") {
    def id = column[Int]("id", O.SqlType("int"), O.PrimaryKey, O.AutoInc)

    def version = column[Int]("version", O.SqlType("int"))

    def subjectId = column[Int]("subject_Id", O.SqlType("int"))

    def objectId = column[Int]("object_Id", O.SqlType("int"))

    def enName = column[String]("en_name", O.SqlType("varchar(60)"))

    def cnName = column[String]("cn_name", O.SqlType("varchar(60)"))

    def parents = column[String]("parents", O.SqlType("varchar(255)"))

    def ttype = column[String]("type", O.SqlType("varchar(30)"))

    def isInited = column[Boolean]("isInited", O.SqlType("bool"))

    def isCalc = column[Boolean]("isCalc", O.SqlType("bool"))

    def isWaitUpdate = column[Boolean]("isWaitUpdate", O.SqlType("bool"))

    def attrs = column[String]("attrs", O.SqlType("text"))

    override def * = (enName, cnName, ttype, version, parents, subjectId, objectId, id.?, attrs, isInited, isCalc, isWaitUpdate) <> (TargetTable.tupled, TargetTable.unapply)
  }

  class TargetAttrTable(tag: Tag) extends Table[TargetAttr](tag, "target_attr") {
    def id = column[Int]("id", O.SqlType("int"), O.PrimaryKey, O.AutoInc)

    def orId = column[Int]("orId", O.SqlType("int"))

    def otId = column[Int]("otId", O.SqlType("int"))

    def enName = column[String]("en_name", O.SqlType("varchar(60)"))

    def cnName = column[String]("cn_name", O.SqlType("varchar(60)"))

    def rulePipeline = column[String]("rule_pipeline", O.SqlType("varchar(255)"))

    def atype = column[String]("type", O.SqlType("varchar(30)"))

    def isPK = column[Boolean]("isPK", O.SqlType("bool"))

    def isMP = column[Boolean]("isMP", O.SqlType("bool"))

    def isMultValue = column[Boolean]("isMultValue", O.SqlType("bool"))

    def isLongValue = column[Boolean]("isLongValue", O.SqlType("bool"))

    override def * = (enName, cnName, atype, orId, otId, isPK, isMP, isMultValue, isLongValue, id.?, rulePipeline.?) <> (TargetAttr.tupled, TargetAttr.unapply)
  }


  val dataSourceInfos = TableQuery[DataSourceInfoTable]
  val originTables = TableQuery[OriginTableTable]
  val originAttrs = TableQuery[OriginAttrTable]
  val targetTables = TableQuery[TargetTableTable]
  val targetAttrs = TableQuery[TargetAttrTable]
}
