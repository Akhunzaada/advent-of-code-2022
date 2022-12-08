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

    fun countVisibleTrees(grid: List<List<Int>>): Int {
        var visibleTrees = 0
        for (i in 1 until grid.size-1) {
            for (j in 1 until grid.first().size-1) {

                var visible = true
                val tree = grid[i][j]
                var t = 0; var b = grid.size-1
                while (i in (t + 1) until b && visible) {
                    if (grid[t][j] >= tree && grid[b][j] >= tree) {
                        visible = false
                        break
                    }
                    if (grid[t][j] < tree) t++
                    if (grid[b][j] < tree) b--
                }

                if (!visible) {
                    visible = true
                    var l = 0; var r = grid.first().size-1
                    while (j in (l + 1) until r && visible) {
                        if (grid[i][l] >= tree && grid[i][r] >= tree) {
                            visible = false
                            break
                        }
                        if (grid[i][l] < tree) l++
                        if (grid[i][r] < tree) r--
                    }
                }

                if (visible) visibleTrees++
            }
        }
        return ((grid.size + grid.first().size - 2) * 2) + visibleTrees
    }

    fun part1(input: List<List<Int>>) = countVisibleTrees(input)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test").parseInput()
    check(part1(testInput) == 21)

    val input = readInput("Day08").parseInput()
    println(part1(input))
}
