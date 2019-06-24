package me.ely.cleanmavenrepository.comparator

import java.io.File
import java.util.regex.Pattern

/**
 *
 *
 * @author  <a href="mailto:xiaochunyong@gmail.com">Ely</a>
 * @see
 * @since   2019-06-24
 */

object ComparatorDecider {

    val MAVEN_VERSION = 1
    val SIMPLE_VERSION = 2
    val OTHER_VERSION = 3

    val mavenPattern = Pattern.compile("^(\\d+\\.)(\\d+\\.)(\\d+)[\\.-](.*)$")

    val simplePattern = Pattern.compile("^(\\d+)(\\.\\d+){0,2}$")

    fun decide(list: Array<File>): VersionType {
        if (isSimpleVersion(list)) {
            return VersionType.SIMPLE
        }

        if (isMavenVersion(list)) {
            return VersionType.MAVEN
        }

        return VersionType.OTHER
    }

    fun isMavenVersion(fileList: Array<File>): Boolean {
        fileList.forEach {
            if (!mavenPattern.matcher(it.name).find()) {
                return false
            }
        }
        return true
    }

    fun isSimpleVersion(fileList: Array<File>): Boolean {
        fileList.forEach {
            if (!simplePattern.matcher(it.name).find()) {
                return false
            }
        }
        return true
    }


    @JvmStatic
    fun main(args: Array<String>) {
        println(decide(arrayOf(File("1"))) == VersionType.SIMPLE)
        println(decide(arrayOf(File("1.2"))) == VersionType.SIMPLE)
        println(decide(arrayOf(File("1.2.3"))) == VersionType.SIMPLE)
        println(decide(arrayOf(File("2.9.7-BE"))) == VersionType.MAVEN)
        println(decide(arrayOf(File("2.9.7.RELEASE"))) == VersionType.MAVEN)
        println(decide(arrayOf(File("v1-rev20181109-1.27.0"))) == VersionType.OTHER)
    }

}