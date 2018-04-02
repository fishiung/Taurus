package test

import db.model.{RDFType, TargetTable}
import org.omg.PortableInterceptor.SUCCESSFUL
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
object A {


  def main(args: Array[String]): Unit = {

    val db = Database.forURL("jdbc:mysql://localhost:3306/play","root","root",driver = "com.mysql.jdbc.Driver")


//    val res = db.createSession().prepareInsertStatement("select * from people limit 3").executeQuery()
//    while(res.next()){
//      println(res.getString(1))
//    }
//    db.run(sql"select xm from people limit 3".as[AnyRef]).value.get.get.foreach(println)
//    case class User(id:Option[Int],name:String)
//
//    class UserTable(tag: Tag) extends Table[User](tag, "user") {
//      def id = column[Int]("id", O.SqlType("int"), O.PrimaryKey, O.AutoInc)
//
//
//      def name = column[String]("name", O.SqlType("varchar(50)"))
//
//
//      override def * = (id.?,name) <> ((User.apply _).tupled, User.unapply)
//    }
//
//    val users = TableQuery[UserTable]
//
//    //Await.result(db.run(users.schema.create),Duration.Inf)
//
//
//  //  val user1 = User(1,"aa")
//
//    val aa = DBIO.seq(
//     // users+=user1,
//      users+=User(None,"a"),
//      users+=User(None,"a"),
//      users+=User(None,"a"),
//      users+=User(None,"a")
//    )
//
//      Await.result(db.run (aa),Duration.Inf)
//      Await.result(db.run (users.result),Duration.Inf).foreach(println(_))

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
    val targetTables = TableQuery[TargetTableTable]


    Await.result(db.run (targetTables += TargetTable("bbb","a","a")),Duration.Inf)
    Await.result(db run (targetTables.filter(t => t.ttype ==="a").result),Duration.Inf).foreach(println(_))



  }

}
