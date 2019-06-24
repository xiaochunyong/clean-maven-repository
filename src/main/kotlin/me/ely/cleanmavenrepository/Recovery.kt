package me.ely.cleanmavenrepository

import java.io.File

/**
 *
 *
 * @author  <a href="mailto:xiaochunyong@gmail.com">Ely</a>
 * @see
 * @since   2019-06-24
 */

object Recovery {

    private fun putBack(dir: File) {
        val files = dir.listFiles()
        if (files.isNullOrEmpty()) {
            return
        }

        for (file in files) {
            if (file.isFile) {
                println("Put Back ${file.absolutePath}")

                val newLocation = File(file.absolutePath.replace(".m3", ".m2"))
                newLocation.parentFile.mkdirs()
                file.renameTo(newLocation)
            } else {
                putBack(file)
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        putBack(File("${System.getProperty("user.home")}/.m3"))
    }
}