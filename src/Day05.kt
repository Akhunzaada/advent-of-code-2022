import java.io.File
import java.util.Stack

data class Cargo(val stacks: List<Stack<Char>>) {
    fun peekStacks() = buildString {
        stacks.forEach {
            append(it.peek())
        }
    }
}

data class ArrangeCrates(val quantity: Int, private val _fromStack: Int, private val _toStack: Int) {
    val fromStack = _fromStack
        get() = field - 1

    val toStack = _toStack
        get() = field - 1
}

interface Crane {
    fun arrange(cargo: Cargo, procedure: List<ArrangeCrates>)
}

class Crane9000 : Crane {
    override fun arrange(cargo: Cargo, procedure: List<ArrangeCrates>) {
        procedure.forEach {
            for (i in 1..it.quantity)
                cargo.stacks[it.toStack].push(cargo.stacks[it.fromStack].pop())
        }
    }
}

class Crane9001 : Crane {
    override fun arrange(cargo: Cargo, procedure: List<ArrangeCrates>) {
        val tempStack = Stack<Char>()
        procedure.forEach {
            for (i in 1..it.quantity)
                tempStack.push(cargo.stacks[it.fromStack].pop())
            for (i in 1..it.quantity)
                cargo.stacks[it.toStack].push(tempStack.pop())
            tempStack.clear()
        }
    }
}

fun main() {
    fun parseStacks(input: String): List<Stack<Char>> {
        val columnInput = input.lines().last()
        val columns = columnInput
            .trim()
            .split("\\s+".toRegex())
            .map { it.toInt() }

        val stacks = MutableList(columns.size) { Stack<Char>() }
        input.lines().reversed().drop(1).forEach { stacksRow ->
            columns.forEach {
                val crate = stacksRow.elementAt(columnInput.indexOf("$it"))
                if (crate.isLetter()) stacks[it - 1].add(crate)
            }
        }
        return stacks
    }

    fun parseProcedure(input: String) =
        input.lines().map {
            it.split(' ').let {
                ArrangeCrates(it[1].toInt(), it[3].toInt(), it[5].toInt())
            }
        }

    fun parseInput(input: String) = input.split("\n\n")
        .let { (stacksInput, procedureInput) ->
            Pair(Cargo(parseStacks(stacksInput)), parseProcedure(procedureInput))
        }

    fun part1(input: String): String {
        return parseInput(input)
            .let { (cargo, procedure) ->
                Crane9000().arrange(cargo, procedure)
                cargo.peekStacks()
            }
    }

    fun part2(input: String): String {
        return parseInput(input)
            .let { (cargo, procedure) ->
                Crane9001().arrange(cargo, procedure)
                cargo.peekStacks()
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = File("src", "Day05_test.txt").readText()
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = File("src", "Day05.txt").readText()
    println(part1(input))
    println(part2(input))
}
