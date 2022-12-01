import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * Splits the original list into sub-lists using the given [predicate].
 */
fun List<String>.split(predicate: (String)-> Boolean): List<List<String>> {
    return flatMapIndexed { i, v ->
        when {
            i == 0 || i == lastIndex -> listOf(i)
            predicate(v) -> listOf(i - 1, i + 1)
            else -> emptyList()
        }
    }.windowed(size = 2, step = 2) { (from, to) -> slice(from..to) }
}