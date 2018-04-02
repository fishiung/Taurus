package rule.util

import java.io.File
import java.net.JarURLConnection
import java.net.URLDecoder

import rule.base.Rule
import util.TaurusException

import scala.collection.mutable

object ScanRules {

  private val rules = new mutable.ArrayBuffer[Rule]

  def load(packPaths: String*): mutable.ArrayBuffer[Rule] = {
    require(!packPaths.isEmpty)
    try {
      packPaths.foreach { pack =>
        val exist = for (rule <- rules) yield rule.getClass.getName
        val currRules = for {
          clz <- getClasses(pack)
          if (!exist.contains(clz.getName))
        } yield clz.newInstance().asInstanceOf[Rule]
        rules ++= currRules
      }
    } catch {
      case e: Exception => throw new TaurusException("路径下出现问题：" + packPaths, e)
    }
    rules
  }

  /**
    * 从包package中获取所有的Class
    *
    * @param packPath
    * @return
    */
  def getClasses(packPath: String): mutable.ArrayBuffer[Class[_]] = {
    val classLoader = Thread.currentThread.getContextClassLoader
    getClasses(packPath, classLoader)
  }

  /**
    * 根据指定类加载器 获取所有的Class
    *
    * @param packPath
    * @param classLoader
    * @return
    */
  def getClasses(packPath: String, classLoader: ClassLoader): mutable.ArrayBuffer[Class[_]] = {
    val classes = new mutable.ArrayBuffer[Class[_]]
    try {

      var packName = packPath
      val packageDirName = packName.replace('.', '/')
      val dirs = classLoader.getResources(packageDirName)
      while (dirs.hasMoreElements) {
        val url = dirs.nextElement()
        // 得到协议的名称
        url.getProtocol match {
          case "file" =>
            val filePath = URLDecoder.decode(url.getFile, "UTF-8")
            findAndAddClassesInPackageByFile(packName, filePath, classes, classLoader)
          case "jar" =>
            val jar = url.openConnection.asInstanceOf[JarURLConnection].getJarFile // 定义一个jar 并获取jar文件
          val entries = jar.entries()
            while (entries.hasMoreElements) {
              val entry = entries.nextElement()
              var name = entry.getName
              //如果是以/开头的  获取后面的字符串
              if (name(0) == "/") name = name.substring(1)
              if (name.startsWith(packageDirName)) {
                val idx = name.lastIndexOf('/')
                // 如果以"/"结尾 是一个包
                if (idx != -1) packName = name.substring(0, idx).replace('/', '.')
                // 如果可以迭代下去 并且是一个包
                if ((idx != -1)) { // 如果是一个.class文件 而且不是目录
                  if (name.endsWith(".class") && !entry.isDirectory) {
                    val className = name.substring(packPath.length + 1, name.length - 6)
                    classes += classLoader.loadClass(packName + '.' + className)
                  }
                }
              }
            }
          case _ => ???
        }
      }

      classes
    } catch {
      case e: Exception => e.printStackTrace()
        classes
    }
  }

  /**
    * 以文件的形式来获取包下的所有Class
    *
    * @param packName
    * @param packPath
    * @param classes
    */
  private def findAndAddClassesInPackageByFile(packName: String, packPath: String, classes: mutable.ArrayBuffer[Class[_]], classLoader: ClassLoader): Unit = {
      val dir = new File(packPath)
      if (!dir.exists || !dir.isDirectory) return
      val dirFiles = dir.listFiles().filter(file => file.isDirectory || file.getName.endsWith(".class"))
      dirFiles.foreach { file =>
        if (file.isDirectory) findAndAddClassesInPackageByFile(packName + '.' + file.getName, file.getAbsolutePath, classes, classLoader)
        else classes += classLoader.loadClass(packName + '.' + file.getName.substring(0, file.getName.length - 6))
      }
  }

}
