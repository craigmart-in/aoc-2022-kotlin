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
//            println("Start of $cycle cycle x is $x")
            if (cycle == nextInterestingSignal) {
                val signalStrength = (cycle * x)
//                println("During the $cycle cycle, the signal strength is ")
                signalStrengthSum += signalStrength
                nextInterestingSignal += 40
            }

            if (operations[i].startsWith("addx")) {
                val addition = operations[i].split(" ")[1].toInt()
//                println("Adding $addition to $x")
                x += addition
            }

//            println("After $cycle cycle x is $x")
        }

        return signalStrengthSum
    }

    fun getPixel(spritePosition: Int, index: Int): String {
        val spritePositions = listOf<Int>(spritePosition - 1, spritePosition, spritePosition + 1)
        if (spritePositions.contains(index % 40)) {
            return "#"
        }
        return "."
    }

    fun part2(input: List<String>): String {
        val operations = parseInput(input)
        val crt = StringBuilder()
        var crtWidth = 40
        var crtRow = 0
        var spritePosition = 1

        for (i in operations.indices) {
            val cycle = i + 1

            crt.append(getPixel(spritePosition, i))

            if (cycle == crtWidth) {
                crt.appendLine()
                crtRow++
                crtWidth += 40
            }

            if (operations[i].startsWith("addx")) {
                val movement = operations[i].split(" ")[1].toInt()
                spritePosition += movement
            }
        }

        return crt.toString()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
