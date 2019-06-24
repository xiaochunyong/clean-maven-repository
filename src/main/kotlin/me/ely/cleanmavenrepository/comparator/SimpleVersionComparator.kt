package me.ely.cleanmavenrepository.comparator

import java.io.File

/**
 *
 *
 * @author  <a href="mailto:xiaochunyong@gmail.com">Ely</a>
 * @see
 * @since   2019-06-24
 */
class SimpleVersionComparator : Comparator<File> {

    //    if (a === b) return 0
//    if (a == null) return -1
//    if (b == null) return 1
    override fun compare(f1: File, f2: File): Int {
        val version1Parts = mutableListOf(*f1.name.split(".").toTypedArray())
        val version2Parts = mutableListOf(*f2.name.split(".").toTypedArray())
        while (version1Parts.size < 3) {
            version1Parts.add("0")
        }
        while (version2Parts.size < 3) {
            version2Parts.add("0")
        }

        try {
            if (version1Parts[0].toInt() > version2Parts[0].toInt()) {
                return 1
            } else if (version1Parts[0].toInt() < version2Parts[0].toInt()) {
                return -1
            } else {
                if (version1Parts[1].toInt() > version2Parts[1].toInt()) {
                    return 1
                } else if (version1Parts[1].toInt() < version2Parts[1].toInt()) {
                    return -1
                } else {
                    if (version1Parts[2].toInt() > version2Parts[2].toInt()) {
                        return 1
                    } else if (version1Parts[2].toInt() < version2Parts[2].toInt()) {
                        return -1
                    } else {
                        return 0
                    }
                }
            }
        } catch (e: Exception) {
            println("${f1.name} \t${f2.name}")
            throw e
        }
    }

}