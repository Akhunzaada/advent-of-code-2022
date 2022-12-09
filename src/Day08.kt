fun main() {
    fun List<String>.parseInput(): List<List<Int>> {
        val grid = MutableList(size) { MutableList(first().length) { 0 } }
        forEachIndexed { i, row ->
            row.forEachIndexed { j, c ->
                grid[i][j] = c - '0'
            }
        }
        return grid
    }

    fun isTreeVisible(tree: Int, index: Int, size: Int, next: (index: Int) -> Int): Boolean {
        var start = 0; var end = size
        while (index in (start + 1) until end) {
            val startVal = next(start); val endVal = next(end)
            if (startVal >= tree && endVal >= tree)
                return false
            if (startVal < tree) start++
            if (endVal < tree) end--
        }
        return true
    }

    fun countVisibleTrees(grid: List<List<Int>>): Int {
        var visibleTrees = 0
        val n = grid.size-1
        val m = grid.first().size-1
        for (i in 1 until n) {
            for (j in 1 until m) {
                val tree = grid[i][j]
                if (isTreeVisible(tree, i, n) { c -> grid[c][j] }
                    || isTreeVisible(tree, j, m) { r -> grid[i][r] }) {
                    visibleTrees++
                }
            }
        }
        return ((grid.size + grid.first().size - 2) * 2) + visibleTrees
    }

    fun treeScenicScore(tree: Int, index: Int, size: Int, next: (index: Int) -> Int): Pair<Int, Int> {
        var start = index; var end = index
        while (start > 0 || end < size) {
            var canBreak = false
            if (start > 0 && next(start-1) < tree) start--
            else canBreak = true

            if (end < size && next(end+1) < tree) end++
            else if (canBreak) {
                if (start != 0) start--
                if (end != size) end++
                break
            }
        }
        return Pair(start, end)
    }

    fun highestScenicScore(grid: List<List<Int>>): Int {
        var highestScenicScore = 0
        val n = grid.size-1
        val m = grid.first().size-1
        for (i in 0..n) {
            for (j in 0..m) {
                var distance = 1
                val tree = grid[i][j]

                val (t, b) = treeScenicScore(tree, i, n) { c -> grid[c][j] }
                distance *= i-t
                distance *= b-i

                val (l, r) = treeScenicScore(tree, j, m) { r -> grid[i][r] }
                distance *= j-l
                distance *= r-j

                highestScenicScore = maxOf(highestScenicScore, distance)
            }
        }
        return highestScenicScore
    }

    fun part1(input: List<List<Int>>) = countVisibleTrees(input)

    fun part2(input: List<List<Int>>) = highestScenicScore(input)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test").parseInput()
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08").parseInput()
    println(part1(input))
    println(part2(input))
}
