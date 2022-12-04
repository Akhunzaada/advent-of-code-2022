fun main() {
    fun String.toIntRange(delimiter: Char = '-'): IntRange = split(delimiter)
        .let { sections ->
            IntRange(sections.first().toInt(), sections.last().toInt())
        }

    fun String.toSectionsPair() = split(',')
        .let {
            Pair(it.first().toIntRange(), it.last().toIntRange())
        }

    fun Pair<IntRange, IntRange>.inclusion(): Boolean =
        (second.first in first && second.last in first) ||
                (first.first in second && first.last in second)

    fun Pair<IntRange, IntRange>.overlap(): Boolean =
        (second.first in first || second.last in first) ||
                (first.first in second || first.last in second)

    fun part1(input: List<String>): Int {
        return input.map { it.toSectionsPair() }
            .count { it.inclusion() }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toSectionsPair() }
            .count { it.overlap() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
