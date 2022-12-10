fun main() {
    fun part1(input: List<String>): Int {
        val height = input.size
        val width = input[0].length
        val treeGrid = getTreeGrid(input)

        // Initialize visible count to outside trees
        var visisbleCount: Int = (width * 2) + ((height - 2) * 2)

        for (y in 1 until height - 1) {
            for (x in 1 until width - 1) {
                val treesInLineOfSight = getTreesInLineOfSight(x, y, treeGrid)
                if (isTreeVisible(treeGrid[y][x], treesInLineOfSight)) {
                    visisbleCount++
                }
            }
        }

        return visisbleCount
    }

    fun part2(input: List<String>): Int {
        val height = input.size
        val width = input[0].length
        val treeGrid = getTreeGrid(input)

        // Initialize visible count to outside trees
        var largestScenicSore = 0

        for (y in 1 until height - 1) {
            for (x in 1 until width - 1) {
                val treesInLineOfSight = getTreesInLineOfSight(x, y, treeGrid)
                val scenicScore = getScenicScore(treeGrid[y][x], treesInLineOfSight)

                if (scenicScore > largestScenicSore) {
                    largestScenicSore = scenicScore
                }
            }
        }

        return largestScenicSore
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

fun getTreeGrid(input: List<String>) : Array<Array<Int>> {
    val height = input.size
    val width = input[0].length
    val treeGrid = Array(width) {Array(height) {0} }

    // Parse input into 2d array
    for ((y, row) in input.withIndex()) {
        for ((x, value) in row.toCharArray().withIndex()) {
            treeGrid[y][x] = value.digitToInt()
        }
    }

    return treeGrid
}

fun getTreesInLineOfSight(x: Int, y: Int, treeGrid: Array<Array<Int>>) : TreesInLineOfSight {
    val treesInRow = treeGrid[y]

    // Check trees in row
    val treesToLeft = treesInRow.take(x)
    val treesToRight = treesInRow.takeLast(treesInRow.size - x - 1)

    val treesAbove = mutableListOf<Int>()
    treeGrid.take(y).forEach { treesAbove.add(it[x]) }

    val treesBelow = mutableListOf<Int>()
    treeGrid.takeLast(treeGrid.size - y - 1).forEach { treesBelow.add(it[x]) }

    return TreesInLineOfSight(treesToLeft, treesToRight, treesAbove, treesBelow)
}

fun isTreeVisible(currentTreeHeight: Int, treesInLineOfSight: TreesInLineOfSight) : Boolean {
    // If there are no trees to the left of the tree then it is visible
    if (treesInLineOfSight.treesToLeft.none { it >= currentTreeHeight }) {
        return true
    }

    // If there are no trees to the right of the tree then it is visible
    if (treesInLineOfSight.treesToRight.none { it >= currentTreeHeight}) {
        return true
    }

    // If there are no trees to above the tree then it is visible
    if (treesInLineOfSight.treesAbove.none { it >= currentTreeHeight }) {
        return true
    }

    // If there are no trees below the tree then it is visible
    if (treesInLineOfSight.treesBelow.none { it >= currentTreeHeight}) {
        return true
    }

    return false
}

fun getScenicScore(currentTreeHeight: Int, treesInLineOfSight: TreesInLineOfSight) : Int {
    var leftCount = 0
    var rightCount = 0
    var aboveCount = 0
    var belowCount = 0

    for (tree in treesInLineOfSight.treesToLeft.reversed()) {
        leftCount++
        if (tree >= currentTreeHeight) break
    }

    for (tree in treesInLineOfSight.treesToRight) {
        rightCount++
        if (tree >= currentTreeHeight) break
    }

    for (tree in treesInLineOfSight.treesAbove.reversed()) {
        aboveCount++
        if (tree >= currentTreeHeight) break
    }

    for (tree in treesInLineOfSight.treesBelow) {
        belowCount++
        if (tree >= currentTreeHeight) break
    }

    return leftCount * rightCount * aboveCount * belowCount
}

data class TreesInLineOfSight(val treesToLeft: List<Int>, val treesToRight: List<Int>, val treesAbove: List<Int>, val treesBelow: List<Int>)