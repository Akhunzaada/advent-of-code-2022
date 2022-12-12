import java.io.File

data class Monkey(
    private val items: MutableList<Long>,
    private val operation: String,
    private val divBy: Int,
    private val throwToMonkey: Int,
    private val elseThrowToMonkey: Int
) {
    private var worryLevel: Long = 0
    var inspections: Long = 0

    fun divBy() = divBy

    fun hasItems() = items.isNotEmpty()

    fun inspect(): Monkey {
        inspections++
        worryLevel = items.removeFirst()
        return this
    }

    fun operate(): Monkey {
        operation.split(' ').let {
            val operator = it.component2()
            val operand2 = it.component3().let {
                if (it == "old") worryLevel else it.toLong()
            }

            worryLevel = when(operator) {
                "*" -> worryLevel * operand2
                "+" -> worryLevel + operand2
                else -> worryLevel
            }
        }
        return this
    }

    fun bored(manageWorryLevel: (worryLevel: Long) -> Long): Monkey {
        worryLevel = manageWorryLevel(worryLevel)
        return this
    }

    fun throwAway(): Pair<Long, Int> {
        val monkey = if (worryLevel % divBy == 0.toLong()) throwToMonkey else elseThrowToMonkey
        return Pair(worryLevel, monkey)
    }

    fun catchItem(item: Long) {
        items.add(item)
    }
}

fun main() {
    fun String.parseInput(): List<Monkey> {
        val monkeys = mutableListOf<Monkey>()
        split("\n\n").map {
            it.lines().drop(1).let {
                val items = it.component1().substringAfter(": ").split(", ").map { it.toLong() }
                val op = it.component2().substringAfter("= ")
                val divBy = it.component3().substringAfterLast(' ').toInt()
                val throwToMonkey = it.component4().substringAfterLast(' ').toInt()
                val elseThrowToMonkey = it.component5().substringAfterLast(' ').toInt()

                monkeys.add(Monkey(items.toMutableList(), op, divBy, throwToMonkey, elseThrowToMonkey))
            }
        }
        return monkeys
    }

    fun List<Monkey>.rounds(times: Int, manageWorryLevel: (worryLevel: Long) -> Long): Long {
        repeat(times) {
            forEach { monkey ->
                while (monkey.hasItems()) {
                    monkey.inspect().operate().bored(manageWorryLevel).throwAway().let { (item, toMonkey) ->
                        get(toMonkey).catchItem(item)
                    }
                }
            }
        }

        return sortedByDescending { it.inspections }
            .let { it.component1().inspections * it.component2().inspections }
    }

    fun part1(input: List<Monkey>): Long = input.rounds(20) { it / 3 }

    fun part2(input: List<Monkey>): Long {
        val mod = input.map { it.divBy().toLong() }.reduce(Long::times)
        return input.rounds(10_000) { it % mod }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = File("src", "Day11_test.txt").readText()
    check(part1(testInput.parseInput()) == 10605L)
    check(part2(testInput.parseInput()) == 2713310158L)

    val input = File("src", "Day11.txt").readText()
    println(part1(input.parseInput()))
    println(part2(input.parseInput()))
}
