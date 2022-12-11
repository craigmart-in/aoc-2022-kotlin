fun main() {
    fun parseInput(input: List<String>): List<String> {
        val output = mutableListOf<String>()
        for (line in input) {
            if (line.startsWith("addx")) {
                output.add("noop")
            }
            output.add(line)
        }

        return output
    }

    fun part1(input: List<String>): Int {
        val operations = parseInput(input)
        var signalStrengthSum = 0
        var nextInterestingSignal = 20
        var x = 1

        for (i in operations.indices) {
            val cycle = i + 1
            println("Start of $cycle cycle x is $x")
            if (cycle == nextInterestingSignal) {
                val signalStrength = (cycle * x)
                println("During the $cycle cycle, the signal strength is ")
                signalStrengthSum += signalStrength
                nextInterestingSignal += 40
            }

            if (operations[i].startsWith("addx")) {
                val addition = operations[i].split(" ")[1].toInt()
                println("Adding $addition to $x")
                x += addition
            }

            println("After $cycle cycle x is $x")
        }

        return signalStrengthSum
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
