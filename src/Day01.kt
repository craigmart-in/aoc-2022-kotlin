fun main() {
    fun part1(input: List<String>): Int {
        val elves = parseInput(input)

        val elfWithMostCalories = elves.maxBy { it.totalCalories }

        return elfWithMostCalories.totalCalories
    }

    fun part2(input: List<String>): Int {
        val elves = parseInput(input)

        val sortedElves = elves.sortedByDescending { it.totalCalories }
        val topThreeCalories = sortedElves.take(3).sumOf { it.totalCalories }

        return topThreeCalories
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

data class Elf(val calories: List<Int>, val totalCalories: Int)

fun parseInput(input: List<String>) : List<Elf> {
    val elves = mutableListOf<Elf>()
    var calories = mutableListOf<Int>()

    for (line in input) {
//        println("Current line: $line")
        if (line.isEmpty()) {
//            println("Creating new elf with calories: $calories")
            val elf = Elf(calories, calories.sum())
//            println("Elf: $elf")
            elves.add(elf)
            calories = mutableListOf<Int>()
            continue
        }

        calories.add(line.toInt())
    }

//    println("Elves: $elves")
    return elves
}