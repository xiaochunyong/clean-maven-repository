package me.ely.cleanmavenrepository

import me.ely.cleanmavenrepository.comparator.*
import java.io.File
import java.nio.file.Files
import java.util.*

/**
 *
 *
 * @author  <a href="mailto:xiaochunyong@gmail.com">Ely</a>
 * @see
 * @since   2019-06-24
 */
object CleanMavenRepository {

    var artifactCount = 0

    /**
     * 返回true, 表示退出循环, false表示继续遍历
     */
    private fun showM2DirInfo(dir: File, doDelete: Boolean = false, num: Int): Boolean {
        val files = dir.listFiles()
        if (files.isNullOrEmpty()) {
            return false
        }

        for (file in files) {
            if (file.isFile) {
                if (file.name.endsWith(".pom") || file.name.endsWith(".jar")) {
                    artifactCount++
                    val artifactDir = file.parentFile.parentFile
                    val versionDirList = artifactDir.listFiles()
                    if (versionDirList != null && versionDirList.size > 1) {
                        // 多个版本, 根据修改时间决定最新版

                        val versionType = ComparatorDecider.decide(versionDirList)
                        val reserveVersionList = if (versionType == VersionType.MAVEN) {
                            versionDirList.sortedWith(MavenVersionComparator()).reversed().take(3)
                        } else if (versionType == VersionType.SIMPLE) {
                            versionDirList.sortedWith(SimpleVersionComparator()).reversed().take(3)
                        } else {
                            versionDirList.sortBy {
                                Files.getLastModifiedTime(it.toPath())
                            }
                            versionDirList.reverse()
                            versionDirList.take(num)
                        }

                        println("Artifact [${artifactDir.name}] 有多个版本存在:")
                        versionDirList.forEach {
                            if (reserveVersionList.contains(it)) {
                                println("\t*${it.name}")
                            } else {
                                println("\t${it.name} ${ if (doDelete) "(Remove)" else "" }")
                                if (doDelete) {
                                    val newLocation = File(it.absolutePath.replace(".m2", ".m3"))
                                    newLocation.parentFile.mkdirs()
                                    it.renameTo(newLocation)
                                }
                            }
                        }
                        println()
                    }
                    return true // 退出当前循环, 以及上层循环(不要再遍历同artifact其它版本目录)
                }
                // 文件名如果不是, 则继续遍历
            } else {
                if (showM2DirInfo(file, doDelete, num)) {
                    break
                }
            }
        }
        return false
    }

    /**
     * 简单的递归打印目录
     */
    fun recursiveDir(dir: File, indent: String? = null): Long {
        val files = dir.listFiles()
        if (files.isNullOrEmpty()) return 0

        var tab: String? = null
        if (indent != null) {
            println("${indent}${dir.name}")
            tab = indent + "\t"
        }

        var size = 0L
        for (file in files) {
            if (file.isFile) {
                size += file.length()
                if (tab != null) {
                    println("${tab}${file.name}")
                }
            } else {
                size += recursiveDir(file)
            }
        }
        return size
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = Scanner(System.`in`)
        val dir = File("${System.getProperty("user.home")}/.m2/repository")
        println("Maven Local Repository目录为: ${dir.absolutePath}")
        println("保留几个版本:")
        val num = input.nextInt()
        println("要执行清理操作吗? Y/N ")
        val answer = input.next()
        val doDelete = answer.equals("y", true)
        val size1 = recursiveDir(dir)
        showM2DirInfo(dir, doDelete, num)
        println("Artifact总计: ${artifactCount}")
        println("清理前磁盘占用: ${size1 / 1024 / 1024} MB")

        if (doDelete) {
            val size2 = recursiveDir(dir, "")
            println("清理后磁盘占用: ${size2 / 1024 / 1024} MB")
        }
    }
}