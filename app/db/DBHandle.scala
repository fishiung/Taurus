package db

import javax.inject.{Inject, Singleton}

import db.model._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

/**
  * 使用play的集成的slick  db
  *
  * @param dbConfigProvider
  * @param ec
  */
@Singleton
class DBHandle @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends TaurusTables {


  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def create = wait {
    val setup = DBIO.seq(
      dataSourceInfos.schema.create,
      originTables.schema.create,
      originAttrs.schema.create,
      targetTables.schema.create,
    targetAttrs.schema.create
    )
    db.run(setup)
  }

  /**
    * GET
    */
  def getOriginTableId(dsId: Int, name: String): Int = wait(db run (originTables.filter(t => t.dsId === dsId && t.enName === name).map(r => r.id).result.head))

  def getDataSourceInfoIdByUrl(url: String): Int = wait(db run (dataSourceInfos.filter(_.url === url).map(r => r.id).result.head))

  def getTargetTableById(ttId: Int): TargetTable = wait(db run (targetTables.filter(_.id === ttId).result.head))

  def getTargetAttrIdByEnName(enNames: String*): Seq[Int] = wait(db run (targetAttrs.filter(_.enName inSet (enNames)).map(r => r.id).result))

  def getTargetTableAttrs(ttId: Int): Seq[TargetAttr] = wait(db run (targetAttrs.filter(_.id inSet (for (id <- getTargetTableById(ttId).attrs.split(",") if (id != "")) yield id.toInt)).result))

  def getOriginAttr(orId: Int): OriginAttr = wait(db run (originAttrs.filter(_.id === orId).result.head))

  /**
    * ADD
    */

  def addTargetTable(table: TargetTable): Unit = wait(db run (targetTables += table))

  def addDataSourceInfo(dataSourceInfo: DataSourceInfo): Unit = wait(db run (dataSourceInfos += dataSourceInfo))

  def addOriginTables(tables: OriginTable*): Unit = wait(db run (originTables ++= tables))

  def addOriginAttrs(attrs: OriginAttr*): Unit = wait(db run (originAttrs ++= attrs))

  def addTargetAttr(attr: TargetAttr): Unit = wait(db run (targetAttrs += attr))

  /**
    * UPDATE
    */

  def updateOrTtTa(orId: Int, ttId: Int, attrEnName: String): Future[Unit] = {
    val table = getTargetTableById(ttId)
    val taId = getTargetAttrIdByEnName(attrEnName)(0)
    val newAttrs = table.attrs.split(",").toSet + taId.toString
    val originAttr = getOriginAttr(orId)
    table.attrs = newAttrs.filter(!_.isEmpty).mkString(",")
    originAttr.taId = taId
    db run (DBIO.seq(originAttrs.filter(_.id === orId).update(originAttr), targetTables.filter(_.id === ttId).update(table)))
  }

  def updateTargetTableAttrs(ttId: Int, attrs: String*): Future[Int] = {
    require(!attrs.isEmpty)
    val table = getTargetTableById(ttId)
    var newAttrs = table.attrs.split(",").toSet
    if (attrs.toSet.subsetOf(newAttrs)) {
      Future(0)
    } else {
      newAttrs ++= attrs
      table.attrs = newAttrs.filter(!_.isEmpty).mkString(",")
      db run (targetTables.filter(_.id === ttId).update(table))
    }
  }

  /**
    * SHOW
    */

  implicit def wait[R](action: Future[R]) = Await.result(action, Duration.Inf)

  def showOriginTableInfos(dsId: Int): Seq[OriginTable] = wait(db run (originTables.filter(t => t.dsId === dsId).result))

  def showOriginAttrInfos(otId: Int): Seq[OriginAttr] = wait(db run (originAttrs.filter(t => t.otId === otId).result))

  def showDataSourceInfos: Seq[DataSourceInfo] = wait(db run (dataSourceInfos.result))

  def showTargetTables: Seq[TargetTable] = wait(db run (targetTables.result))

  def showEntitys: Seq[TargetTable] = wait(db run (targetTables.filter(t => t.ttype === RDFType.Entity).result))

  def showEvents: Seq[TargetTable] = wait(db run (targetTables.filter(t => t.ttype === RDFType.Event).result))

  def showRelations: Seq[TargetTable] = db run (targetTables.filter(t => t.ttype === RDFType.Relation).result)




}

