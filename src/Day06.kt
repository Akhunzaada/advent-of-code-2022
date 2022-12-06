import java.io.File

fun main() {
    fun String.allUnique(): Boolean = all(hashSetOf<Char>()::add)

    fun String.uniqueChunk(size: Int): Int {
        return windowedSequence(size).indexOfFirst {
            it.allUnique()
        } + size
    }

    fun part1(input: String) = input.uniqueChunk(4)

    fun part2(input: String) = input.uniqueChunk(14)

    // test if implementation meets criteria from the description, like:
    val testInput = File("src", "Day06_test.txt").readText()
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = File("src", "Day06.txt").readText()
    println(part1(input))
    println(part2(input))
}
