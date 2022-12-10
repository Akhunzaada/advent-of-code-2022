import java.lang.Exception
import kotlin.math.abs

data class Point(var x: Int, var y: Int) {
    infix fun moveTo(delta: Point) {
        x += delta.x
        y += delta.y
    }

    infix fun follow(point: Point) {
        if (abs(x-point.x) > 1 || abs(y-point.y) > 1) {
            x += if (x < point.x) +1 else if (x > point.x) -1 else 0
            y += if (y < point.y) +1 else if (y > point.y) -1 else 0
        }
    }
}

fun main() {
    fun String.direction(): Pair<Point, Int> {
        val (direction, steps) = split(' ')
        val delta = when (direction) {
            "L" -> Point(-1, 0)
            "U" -> Point(0, -1)
            "R" -> Point(1, 0)
            "D" -> Point(0, 1)
            else -> throw Exception("Invalid Direction!")
        }
        return Pair(delta, steps.toInt())
    }

    fun List<String>.directions(move: (delta: Point) -> Unit) {
        forEach {
            it.direction().let { (delta, steps) ->
                repeat(steps) {
                    move(delta)
                }
            }
        }
    }

    fun positionsVisitedByTail(input: List<String>, ropeKnots: Int): Int {
        val visited = mutableSetOf<Point>()
        val knots = MutableList(ropeKnots) { Point(0, 0) }
        input.directions {
            knots.first() moveTo it
            knots.windowed(2) {
                it.component2() follow it.component1()
            }
            visited.add(knots.last().copy())
        }
        return visited.count()
    }

    fun part1(input: List<String>) = positionsVisitedByTail(input, 2)

    fun part2(input: List<String>) = positionsVisitedByTail(input, 10)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)
    val testInput2 = readInput("Day09_test2")
    check(part2(testInput2) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
