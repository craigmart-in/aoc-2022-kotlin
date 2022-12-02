fun main() {
    fun part1(input: List<String>): Int {
        // A/X is Rock
        // B/Y is Paper
        // C/Z is Scissors
        var totalScore = 0

        for (line in input) {
            val round = line.split(" ")

            val moveScore = when (round[1]) {
                "X" -> 1
                "Y" -> 2
                else -> 3
            }

            val roundScore = when (round[1]) {
                "X" -> {
                    when (round[0]) {
                        "A" -> 3
                        "B" -> 0
                        else -> 6
                    }
                }
                "Y" -> {
                    when (round[0]) {
                        "A" -> 6
                        "B" -> 3
                        else -> 0
                    }
                }
                else -> {
                    when (round[0]) {
                        "A" -> 0
                        "B" -> 6
                        else -> 3
                    }
                }
            }

            totalScore += moveScore + roundScore
        }

        return totalScore
    }

    fun part2(input: List<String>): Int {
        // A is Rock = 1
        // B is Paper = 2
        // C is Scissors = 3

        // X is Lose = 0
        // Y is draw = 3
        // Z is win = 6
        var totalScore = 0

        for (line in input) {
            val round = line.split(" ")

            val roundScore = when (round[1]) {
                "X" -> 0
                "Y" -> 3
                else -> 6
            }

            val moveScore = when (round[1]) {
                "X" -> {
                    when (round[0]) {
                        "A" -> 3
                        "B" -> 1
                        else -> 2
                    }
                }
                "Y" -> {
                    when (round[0]) {
                        "A" -> 1
                        "B" -> 2
                        else -> 3
                    }
                }
                else -> {
                    when (round[0]) {
                        "A" -> 2
                        "B" -> 3
                        else -> 1
                    }
                }
            }

            totalScore += moveScore + roundScore
        }

        return totalScore
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

