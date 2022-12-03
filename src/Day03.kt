fun main() {
    fun Char.getPriority() = code - if (isLowerCase()) 96 else 38

    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach skip@ {
            val firstCompartment = it.subSequence(0, it.length / 2).toSet()
            for (i in it.length / 2 until it.length) {
                if (firstCompartment.contains(it[i])) {
                    sum += it[i].getPriority()
                    return@skip
                }
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf {
            val commonItems = HashSet(it.first().toSet())
            for (i in 1 until it.size) {
                commonItems.retainAll(it[i].toSet())
            }
            commonItems.first().getPriority()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
