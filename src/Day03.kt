fun main() {
    val priorityMap = ('a'..'z').union('A'..'Z')

    fun part1(input: List<String>): Int {
        var prioritySum = 0

        for (line in input) {
            val rucksack = line.chunked(line.length / 2)
            val duplicate = rucksack[0].toCharArray().intersect(rucksack[1].toList()).first()
            prioritySum += priorityMap.indexOf(duplicate) + 1
        }

        return prioritySum
    }

    fun part2(input: List<String>): Int {
        var prioritySum = 0
        val elfGroups = input.chunked(3)

        for (elfGroup in elfGroups) {
            val duplicate = elfGroup[0].toCharArray().intersect(elfGroup[1].toList()).intersect(elfGroup[2].toList()).first()
            prioritySum += priorityMap.indexOf(duplicate) + 1
        }

        return prioritySum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
