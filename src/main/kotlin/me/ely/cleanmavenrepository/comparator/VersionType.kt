package me.ely.cleanmavenrepository.comparator

/**
 *
 *
 * @author  <a href="mailto:xiaochunyong@gmail.com">Ely</a>
 * @see
 * @since   2019-06-24
 */
enum class VersionType(val value: Int, val label: String) {
    MAVEN(1, "Maven"),
    SIMPLE(2, "Simple"),
    OTHER(3, "Other"),
    ;

    companion object {

        private val map = mutableMapOf<Int, VersionType>()

        init {
            values().forEach { map[it.value] = it }
        }

        fun parse(value: Int): VersionType? {
            return map[value]
        }
    }
}