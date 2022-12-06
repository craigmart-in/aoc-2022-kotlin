import java.util.Stack

fun main() {
    fun part1(input: List<String>): String {
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
                moves.add(Move(match.groups[1]?.value?.toInt()!!, match.groups[2]?.value?.toInt()!!, match.groups[3]?.value?.toInt()!!))
            } else if (line.isNotEmpty()) {
                val size = line.last()
                stacks = Array(size.digitToInt()){ Stack() }
            }
        }

        for (move in moves.reversed()) {
            for (number in 0 until move.quantity) {
                val crate = stacks[move.from-1].pop()
                stacks[move.to-1].add(crate)
            }
        }

        var output = ""
        for (stack in stacks) {
            output += stack.pop()
        }

        return output
    }

    fun part2(input: List<String>): String {
        return input.size.toString()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

data class Move(val quantity: Int, val from: Int, val to: Int)