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

    fun part1(input: List<String>): Int {
        val visited = mutableSetOf<Point>()
        val head = Point(0, 0)
        val tail = Point(0, 0)
        input.forEach {
            it.direction().let { (delta, steps) ->
                repeat(steps) {
                    head moveTo delta
                    tail follow head
                    visited.add(tail.copy())
                }
            }
        }
        return visited.count()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)

    val input = readInput("Day09")
    println(part1(input))
}
