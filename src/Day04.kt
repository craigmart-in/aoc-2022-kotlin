fun main() {
    fun part1(input: List<String>): Int {
        var totalCount = 0

        for (line in input) {
            val elvesSections = getElfSections(line)
            val elfSectionOneRange = elvesSections[0]
            val elfSectionTwoRange = elvesSections[1]

            println("Elf 1 = ${elfSectionOneRange.toList()}; Elf 2 ${elfSectionTwoRange.toList()}")

            var difference = listOf<Int>()
            if (elfSectionOneRange.count() < elfSectionTwoRange.count()) {
                difference = elfSectionOneRange.minus(elfSectionTwoRange)
            } else {
                difference = elfSectionTwoRange.minus(elfSectionOneRange)
            }

            println("Difference: ${difference}")
            if (difference.isEmpty()) {
                totalCount++
            }
        }

        println("Total Count = $totalCount")
        return totalCount
    }

    fun part2(input: List<String>): Int {
        var totalCount = 0

        for (line in input) {
            val elvesSections = getElfSections(line)
            val elfSectionOneRange = elvesSections[0]
            val elfSectionTwoRange = elvesSections[1]

            println("Elf 1 = ${elfSectionOneRange.toList()}; Elf 2 ${elfSectionTwoRange.toList()}")

            val intersection = elfSectionOneRange.intersect(elfSectionTwoRange)

            if (intersection.isNotEmpty()) {
                totalCount++
            }
        }

        println("Total Count = $totalCount")
        return totalCount
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

fun getElfSections(line: String) : List<IntRange> {
    val elvesSections = line.split(",")
    val elfSectionOne = elvesSections[0].split("-")
    val elfSectionOneRange = elfSectionOne[0].toInt()..elfSectionOne[1].toInt()
    val elfSectionTwo = elvesSections[1].split("-")
    val elfSectionTwoRange = elfSectionTwo[0].toInt()..elfSectionTwo[1].toInt()

    return listOf(elfSectionOneRange, elfSectionTwoRange)
}
