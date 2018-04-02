package db.util


import javax.inject.{Inject, Singleton}

import db.DBHandle
import db.model.{DataSourceInfo, OriginAttr, OriginTable}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._
import util.TaurusException

import scala.collection.mutable.ArrayBuffer

@Singleton
class JDBCDataSourceUtil @Inject()(handel: DBHandle) {


  def init(info: DataSourceInfo): Unit = {
    val externalDB = Database.forURL(info.url, info.username, info.password, driver = info.url match {
      case url if (url.startsWith("jdbc:mysql")) => "com.mysql.jdbc.Driver"
      case _ => throw new TaurusException(s"don't support ${info.url}")
    })
    //根据url获取数据库的schema
    val schema = info.url.substring(info.url.indexOf("3306") + 5, if (info.url.indexOf("?") != -1) info.url.indexOf("?") else info.url.length).trim
    handel.addDataSourceInfo(info)
    val dsId = handel.getDataSourceInfoIdByUrl(info.url)
    val originPart = sql"select table_name,table_rows,data_length/1024/1024 from  information_schema.tables where table_schema =$schema".as[(String, Long, Long)]
    var originTableName = new ArrayBuffer[String]
    handel.addOriginTables((for {
      tuple <- handel wait externalDB.run(originPart)
    } yield {
      originTableName += tuple._1; OriginTable(tuple._1, tuple._2, tuple._3, None, dsId)
    }): _*)

    originTableName.foreach { name =>
      val osId = handel.getOriginTableId(dsId, name)
      val attrs = new ArrayBuffer[OriginAttr]
      //查询的字段的主键名称
      val pks = handel wait externalDB.run {
        sql"""
             select column_name from information_schema.key_column_usage a,information_schema.table_constraints b
            where a.table_name = b.table_name and constraint_type = 'primary key'
            and a.table_schema = $schema and a.table_name = $name
        """.as[String]
      }
      //查询的字段的名字，类型，
      (handel wait externalDB.run {
        sql"select column_name,data_type from information_schema.columns where table_schema = $schema and table_name =$name".as[(String, String)]
      }).foreach(t => attrs += OriginAttr(t._1, t._2, "", "", "", pks.contains(t._1), None, osId,-1))



      //添加的样例数据
      val res = externalDB.createSession().prepareStatement(s"select * from $schema.$name limit 3").executeQuery()
      while (res.next()) for (i <- 0 until attrs.length) attrs(i).setSample{
        val value = res.getString(attrs(i).enName)
        if(value == null || value.trim== "") "" else value
      }
      handel.addOriginAttrs(attrs: _*)
    }
  }


}
