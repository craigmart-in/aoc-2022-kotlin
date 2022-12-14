import java.util.PriorityQueue
import kotlin.math.abs

fun main() {
    fun Char.getInt(): Int {
        if (this == 'S') return 1
        if (this == 'E') return 26
        return this.code - 96
    }

    fun Int.canMoveTo(destination: Int): Boolean {
        return (destination - this) <= 1
    }

    data class Position(val x: Int, val y: Int, val value: Char, val direction: Char, val priority: Int) : Comparable<Position> {
        override fun compareTo(other: Position): Int {
            if ((this.x == other.x && this.y == other.y) || this.value.getInt() == other.value.getInt()) {
                return 0
            }
            else if (this.priority < other.priority) {
                return -1
            } else {
                return 1
            }
        }

        override fun equals(other: Any?): Boolean =
            other is Position
                    && this.x == other.x
                    && this.y == other.y

        override fun hashCode(): Int {
            var result = x.hashCode()
            result = result * 31 + y.hashCode()
            return result
        }
    }

    fun heuristic(a: Position, b: Position) : Int {
        return abs(a.x - b.x) + abs(a.y - b.y)
    }

    fun Array<Array<Char>>.getNeighbors(current: Position): List<Position> {
        val neighbors = mutableListOf<Position>()
        val currentValue = current.value.getInt()

        // Check left
        if (current.x > 0 && currentValue.canMoveTo(this[current.y][current.x - 1].getInt())) {
            neighbors.add(Position(current.x - 1, current.y, this[current.y][current.x - 1], '<', 0))
        }

        // Check right
        if (current.x < (this[0].size - 1) && currentValue.canMoveTo(this[current.y][current.x + 1].getInt())) {
            neighbors.add(Position(current.x + 1, current.y, this[current.y][current.x + 1], '>', 0))
        }

        // Check up
        if (current.y > 0 && currentValue.canMoveTo(this[current.y - 1][current.x].getInt())) {
            neighbors.add(Position(current.x, current.y - 1, this[current.y - 1][current.x], '^', 0))
        }

        // Check down
        if (current.y < (this.size - 1) && currentValue.canMoveTo(this[current.y + 1][current.x].getInt())) {
            neighbors.add(Position(current.x, current.y + 1, this[current.y + 1][current.x], 'V', 0))
        }

        return neighbors
    }

    fun Array<Array<Char>>.print() {
        for (row in this) {
            for (spot in row) {
                print(spot)
            }
            println()
        }
    }

    fun printPath(height: Int, width: Int, path: List<Position>) {
        val map = Array(height) {Array(width) {'.'} }

        for (spot in path) {
            map[spot.y][spot.x] = spot.direction
        }

        map.print()
    }

    fun part1(input: List<String>): Int {
        val height = input.size
        val width = input[0].length
        val map = Array(height) {Array(width) {'a'} }

        var startPosition = Position(0, 0, ' ', 'S', 0)
        var endPosition = Position(0, 0, ' ', 'E', 0)

        for ((y, line) in input.withIndex()) {
            for ((x, square) in line.toCharArray().withIndex()) {
                if (square == 'S') {
                    startPosition = Position(x, y, square, square, 0)
                }
                if (square == 'E') {
                    endPosition = Position(x, y, square, square, 0)
                }

                map[y][x] = square
            }
        }

        map.print()

        var frontier = PriorityQueue<Position>()
        frontier.add(startPosition)

        val cameFrom = mutableMapOf<Position, Position?>()
        cameFrom[startPosition] = null

        val countSoFar = mutableMapOf<Position, Int>()
        countSoFar[startPosition] = 0

        while (frontier.isNotEmpty()) {
            val current = frontier.poll()

            if (current == endPosition) {
                break
            }

            for (next in map.getNeighbors(current)) {
                val newCost = countSoFar[current]?.plus(1) ?: 1
                if (!cameFrom.contains(next) || newCost < countSoFar[next]!!) {
                    countSoFar[next] = newCost
                    val nextPriority = Position(next.x, next.y, next.value, next.direction, newCost + heuristic(endPosition, next))
                    frontier.add(nextPriority)
                    cameFrom[nextPriority] = current
                }
            }
        }

        val path = mutableListOf<Position>()
        var currentPath = endPosition
        while (currentPath != startPosition) {
            path.add(currentPath)
            currentPath = cameFrom[currentPath]!!
        }

        printPath(height, width, path)

        return path.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
