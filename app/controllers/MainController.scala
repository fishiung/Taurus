package controllers

import javax.inject._

import db.DBHandle
import db.model._
import db.util.JDBCDataSourceUtil
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form

import scala.concurrent.{ExecutionContext, Future}
import rule.util.RuleUtil._
import rule.util.ScanRules
import util.TaurusException

@Singleton
class MainController @Inject()(cc: ControllerComponents, db: JDBCDataSourceUtil, handle: DBHandle)(implicit ec: ExecutionContext) extends AbstractController(cc) {


  /**
    *加载所有的规则  转为不可变列表
    */
  val rules = ScanRules.load("rule.impl").zipWithIndex.toList



  def home = Action { implicit request =>
    Ok(views.html.home())
  }

  def infoForm = Action { implicit request =>
    Ok(views.html.infoForm())
  }

  //TODO 目标表删除
  //TODO 目标表判重
  //TODO ajax 修改目标表的各项的值
  //TODO 目标字段的删除
  //TODO 规则页面表单的添加
  //TODO 目标表更新状态的添加

  def addDataSourceInfo = Action.async { implicit request: Request[AnyContent] =>
    Form(tuple("url" -> nonEmptyText, "username" -> nonEmptyText, "password" -> nonEmptyText)).bindFromRequest().fold(

      errForm => Future.successful(Redirect(routes.MainController.showDataSourceInfo())),
      dsInfo => {
        //TODO  数据源类型判断， 数据源是否已经存在判断
        db.init(DataSourceInfo(dsInfo._1, dsInfo._2, dsInfo._2, "mysql", None))
        Future.successful(Redirect(routes.MainController.showDataSourceInfo()))
      }
    )
  }

  def addEntity = Action.async { implicit request: Request[AnyContent] =>
    Form(tuple("enName" -> nonEmptyText, "cnName" -> nonEmptyText)).bindFromRequest().fold(
      errForm => Future.successful(Redirect(routes.MainController.showTargetTablesbyType(RDFType.Entity))),
      data => {
        handle.addTargetTable(TargetTable(data._1, data._2, RDFType.Entity))
        Future.successful(Redirect(routes.MainController.showTargetTablesbyType(RDFType.Entity)))
      }
    )
  }

  def addEvent = Action.async { implicit request: Request[AnyContent] =>
    Form(tuple("enName" -> text, "cnName" -> text, "subjectId" -> number, "objectId" -> number)).bindFromRequest().fold(
      errForm => Future.successful(Redirect(routes.MainController.showTargetTablesbyType(RDFType.Event))),
      data => {
        handle.addTargetTable(TargetTable(data._1, data._2, RDFType.Event, subjectId = data._3, objectId = data._4))
        Future.successful(Redirect(routes.MainController.showTargetTablesbyType(RDFType.Event)))
      }
    )
  }

  def addRelation = Action.async { implicit request: Request[AnyContent] =>
    Form(tuple("enName" -> text, "cnName" -> text, "subjectId" -> number, "objectId" -> number)).bindFromRequest().fold(
      errForm => Future.successful(Redirect(routes.MainController.showTargetTablesbyType(RDFType.Relation))),
      data => {
        handle.addTargetTable(TargetTable(data._1, data._2, RDFType.Relation, subjectId = data._3, objectId = data._4))
        Future.successful(Redirect(routes.MainController.showTargetTablesbyType(RDFType.Relation)))
      }
    )
  }


  def addTargetAttr = Action.async { implicit request: Request[AnyContent] =>
    Forms.targetAttrForm.bindFromRequest().fold(//不是单一参数的方法不能用{}
      errFrom => Future.successful(NotFound),
      data => {
        handle.addTargetAttr(TargetAttr(data.enName, data.cnName, data.atype, data.orId, data.otId, ^(data.isPK), ^(data.isMP), ^(data.isMultValue), ^(data.isLongValue), None, Some("")))
        handle.updateOrTtTa(data.orId, data.ttId, data.enName)
        Future.successful(Redirect(routes.MainController.showOriginAttrInfo(data.otId)))
      }
    )
  }

  def addTargetAttrs = Action.async { implicit request: Request[AnyContent] =>
    Form(tuple("taIds"->text,"ttId" -> number)).bindFromRequest().fold(
      errFrom => Future.successful(NotFound),
      data => {
        handle.updateTargetTableAttrs(data._2,data._1.split(","):_*)
        Future.successful(ResetContent)
      }
    )
  }


  def showDataSourceInfo = Action.async { implicit request =>
    Future.successful(Ok(views.html.showInfo(handle.showDataSourceInfos)))
  }

  def showTargetTablesbyType(t: String) = Action.async { implicit request =>
    if (t == RDFType.Entity || t == RDFType.Event || t == RDFType.Relation)
      Future.successful(Ok(views.html.targetTableInfo(t, handle.showTargetTables)))
    else Future.successful(NotFound)
  }


  def showOriginTableInfo(dsId: Int) = Action.async { implicit request =>
    Future.successful(Ok(views.html.originTableInfo(handle.showOriginTableInfos(dsId))))
  }

  def showOriginAttrInfo(otId: Int) = Action.async { implicit request =>
    Future.successful(Ok(views.html.originAttrInfo(handle.showOriginAttrInfos(otId), handle.showTargetTables,rules)))
  }

  def showTargetAttrInfo(ttId: Int,tn:String,t:String) = Action.async { implicit request =>
    val msg = t match {
      case "Entity" => "实体: " + tn
      case "Event" => "事件： " + tn
      case "Relation" => "关系： " + tn
      case _ => throw new TaurusException("错误类型")
    }
    Future.successful(Ok(views.html.targetAttrInfo(handle.getTargetTableAttrs(ttId), msg)))
  }

}


