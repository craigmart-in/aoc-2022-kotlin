import kotlin.math.abs

fun main() {
    data class Move(val direction: String, val steps: Int)
    data class Position(val x: Int, val y: Int)

    fun getMoves(input: List<String>): List<Move> {
        val moves = mutableListOf<Move>()
        for (line in input) {
            val splitLine = line.split(" ")
            moves.add(Move(splitLine[0], splitLine[1].toInt()))
        }
        return moves
    }

    fun Move.move(currentPosition: Position): Position {
        return when (this.direction) {
            "R" -> Position(currentPosition.x + 1, currentPosition.y)
            "L" -> Position(currentPosition.x - 1, currentPosition.y)
            "U" -> Position(currentPosition.x, currentPosition.y + 1)
            else -> Position(currentPosition.x, currentPosition.y - 1)
        }
    }

    fun isTailNearHead(headPosition: Position, tailPosition: Position): Boolean {
        if (headPosition == tailPosition) {
            return true
        }

        if (abs(headPosition.x - tailPosition.x) <= 1 &&
            abs(headPosition.y - tailPosition.y) <= 1) {
            return true
        }

        return false
    }

    fun getAdditionValue(x: Int): Int {
        if (x > 0) {
            return 1
        }
        return -1
    }

    fun moveTailNearHead(headPosition: Position, tailPosition: Position): Position {
        val dx = headPosition.x - tailPosition.x
        val dy = headPosition.y - tailPosition.y

        var newX = tailPosition.x
        var newY = tailPosition.y

        // Directly above or below
        if (abs(dx) > 1 && dy == 0) {
            newX += getAdditionValue(dx)
        }
        // Directly left or right
        else if (abs(dy) > 1 && dx == 0) {
            newY += getAdditionValue(dy)
        }
        // 2 up or down and 1 left or right
        else if (abs(dy) > 1 && dx != 0) {
            newX += getAdditionValue(dx)
            newY += getAdditionValue(dy)
        }
        // 2 left or right and 1 up or down
        else if (abs(dx) > 1 && dy != 0) {
            newX += getAdditionValue(dx)
            newY += getAdditionValue(dy)
        }

        return Position(newX, newY)
    }

    fun part1(input: List<String>): Int {
        var headPosition = Position(0, 0)
        var tailPosition = Position(0, 0)
        val tailPositionsSet = mutableSetOf<Position>(Position(0, 0))

        val moves = getMoves(input)

        moves.forEach {
            for (step in 0 until it.steps) {
                println("===")
                println("$it; Step ${step + 1} of ${it.steps}; Head: $headPosition; Tail: $tailPosition")
                headPosition = it.move(headPosition)

                if (!isTailNearHead(headPosition, tailPosition)) {
                    println("Tail is not touching.")
                    tailPosition = moveTailNearHead(headPosition, tailPosition)
                    tailPositionsSet.add(tailPosition)
                }

                println("After move: Head: $headPosition; Tail: $tailPosition")
                println("")
            }
        }

        return tailPositionsSet.size
    }

    fun part2(input: List<String>): Int {
        var headPosition = Position(0, 0)
        var knotPositions = Array(9) {Position(0, 0)}
        val tailPositionsSet = mutableSetOf(Position(0, 0))

        val moves = getMoves(input)

        moves.forEach {
            for (step in 0 until it.steps) {
                //println("$it; Step ${step + 1} of ${it.steps}; Head: $headPosition; Tail: $tailPosition")
                headPosition = it.move(headPosition)

                if (!isTailNearHead(headPosition, knotPositions[0])) {
                    knotPositions[0] = moveTailNearHead(headPosition, knotPositions[0])
                }

                for (knot in 1 until 8) {
                    if (!isTailNearHead(knotPositions[knot - 1], knotPositions[knot])) {
                        knotPositions[knot] = moveTailNearHead(knotPositions[knot - 1], knotPositions[knot])
                    }
                }

                if (!isTailNearHead(knotPositions[7], knotPositions[8])) {
                    knotPositions[8] = moveTailNearHead(knotPositions[7], knotPositions[8])
                    tailPositionsSet.add(knotPositions[8])
                }
            }
        }

        return tailPositionsSet.size
    }

    val test1 = moveTailNearHead(Position(3, 4), Position(2, 2))
    check(test1 == Position(3, 3))

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)

    val testInput2 = readInput("Day09_test2")
    check(part2(testInput2) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

