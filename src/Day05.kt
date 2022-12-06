import java.util.*

fun main() {
    fun part1(input: MovesAndStacks): String {
        for (move in input.moves) {
            for (number in 0 until move.quantity) {
                val crate = input.stacks[move.from].pop()
                input.stacks[move.to].add(crate)
            }
        }

        var output = ""
        for (stack in input.stacks) {
            output += stack.pop()
        }

        return output
    }

    fun part2(input: MovesAndStacks): String {
        for (move in input.moves) {
            val crates = Stack<String>()
            for (number in 0 until move.quantity) {
                if (!input.stacks[move.from].peek().isNullOrEmpty()) {
                    crates.add(input.stacks[move.from].pop())
                }
            }

            for (number in 0 until move.quantity) {
                input.stacks[move.to].add(crates.pop())
            }
        }

        var output = ""
        for (stack in input.stacks) {
            try {
                output += stack.pop()
            }
            catch (e: EmptyStackException) {}
        }

        return output
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(getMovesAndStacks(testInput)) == "CMZ")
    check(part2(getMovesAndStacks(testInput)) == "MCD")

    val input = readInput("Day05")
    println(part1(getMovesAndStacks(input)))
    println(part2(getMovesAndStacks(input)))
}

data class Move(val quantity: Int, val from: Int, val to: Int)

data class MovesAndStacks(val moves: List<Move>, val stacks: Array<Stack<String>>)

fun getMovesAndStacks(input: List<String>) : MovesAndStacks {
    var stacks = Array<Stack<String>>(0){Stack()}
    val moves = mutableListOf<Move>()

    for (line in input.reversed()) {
        // Read a block
        if (line.contains("[")) {
            line.chunked(4).forEachIndexed { index, it ->
                val crate = "(\\w)".toRegex().find(it)?.groups?.get(1)?.value
                if (!crate.isNullOrEmpty()) {
                    stacks[index].add(crate)
                }
            }
        }
        // Handle the moves
        else if (line.startsWith("move")) {
            val match = Regex("move (\\d+) from (\\d+) to (\\d+)").find(line)
            if (match == null || match.groups.count() != 4) {
                continue
            }
            moves.add(Move(match.groups[1]?.value?.toInt()!!, match.groups[2]?.value?.toInt()!!-1, match.groups[3]?.value?.toInt()!!-1))        } else if (line.isNotEmpty()) {
            val size = line.last()
            stacks = Array(size.digitToInt()){ Stack() }
        }
    }

    return MovesAndStacks(moves.reversed(), stacks)
}