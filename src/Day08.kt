fun main() {
    fun part1(input: List<String>): Int {
        val height = input.size
        val width = input[0].length
        val treeGrid = Array(width) {Array(height) {0} }

        // Parse input into 2d array
        for ((y, row) in input.withIndex()) {
            for ((x, value) in row.toCharArray().withIndex()) {
                treeGrid[y][x] = value.digitToInt()
            }
        }

        // Initialize visible count to outside trees
        var visisbleCount: Int = (width * 2) + ((height - 2) * 2)

        for (y in 1 until height - 1) {
            for (x in 1 until width - 1) {
                if (isTreeVisible(x, y, treeGrid)) {
                    visisbleCount++
                }
            }
        }

        return visisbleCount
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

fun isTreeVisible(x: Int, y: Int, treeGrid: Array<Array<Int>>) : Boolean {
    val currentTreeHeight = treeGrid[y][x]
    val treesInRow = treeGrid[y]

    // Check trees in row
    val treesToLeft = treesInRow.take(x)
    val treesToRight = treesInRow.takeLast(treesInRow.size - x - 1)

    // If there are no trees to the left of the tree then it is visible
    if (treesToLeft.none { it >= currentTreeHeight }) {
        return true
    }

    // If there are no trees to the right of the tree then it is visible
    if (treesToRight.none { it >= currentTreeHeight}) {
        return true
    }

    val treesAbove = mutableListOf<Int>()
    treeGrid.take(y).forEach { treesAbove.add(it[x]) }

    val treesBelow = mutableListOf<Int>()
    treeGrid.takeLast(treeGrid.size - y - 1).forEach { treesBelow.add(it[x]) }

    // If there are no trees to above the tree then it is visible
    if (treesAbove.none { it >= currentTreeHeight }) {
        return true
    }

    // If there are no trees below the tree then it is visible
    if (treesBelow.none { it >= currentTreeHeight}) {
        return true
    }

    return false
}

/*

30373


Current tree is 7
All trees >= 7 -> none
This means the tree is visible

Current tree is 3rd with height 3
All trees >= 3 -> left (3); right (7, 3)
Not visible


33549

Current tree = 5
All trees >= 5 -> left(); right(9)
visible on left but not right
 */