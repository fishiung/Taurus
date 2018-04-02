//package computer.pre
//
//import org.apache.hadoop.conf.Configuration
//import org.apache.hadoop.hbase.NamespaceDescriptor
//import org.apache.hadoop.hbase.client.ConnectionFactory
//import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos.NamespaceDescriptor
//
///**
//  *
//  * Created by fishiung on 2018-01-15
//  *
//  **/
//
//object TaurusHbaseOp {
//
//  val taurusSpace = "taurus"
//
//
//  def initHbase= {
//    val configuration = new Configuration
//    val hbaseConnection = ConnectionFactory.createConnection
//    val admin = hbaseConnection.getAdmin
//    admin.listNamespaceDescriptors().foreach(desc=>desc.asInstanceOf[NamespaceDescriptor].getName.equals(taurusSpace))
//
//  }
//
//
//
//
//}
