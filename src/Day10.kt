class CPU {
    private var register = 1
    private var cycles = 0

    fun exec(program: List<String>, op: (register: Int, cycles: Int) -> Unit) {
        program.forEach {
            op(register, ++cycles)
            if (it.startsWith("addx")) {
                op(register, ++cycles)
                register += it.substringAfter(" ").toInt()
            }
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        var signalStrength = 0
        CPU().exec(input) { register, cycles ->
            if (cycles % 40 == 20)
                signalStrength += cycles * register
        }
        return signalStrength
    }

    fun part2(input: List<String>) = buildString(6 * 40) {
        CPU().exec(input) { register, cycles ->
            val sprite = register-1..register+1
            if ((cycles-1) % 40 in sprite) append('#') else append('.')
            if ((cycles) % 40 == 0) appendLine()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
