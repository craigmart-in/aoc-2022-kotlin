fun main() {
    fun part1(input: List<String>): Int {
        val data = input[0]

        var messageStart = 0
        for (i in 4 until data.length) {
            val marker = data.substring(i-4, i)
            val markerSet = marker.toSet()
            println("Checking marker: $marker. The set is: $markerSet")

            if (markerSet.count() == 4) {
                messageStart = i
                break
            }
        }

        return messageStart
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
