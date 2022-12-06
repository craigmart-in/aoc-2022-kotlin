fun main() {
    fun part1(input: List<String>): Int {
        val data = input[0]

        return getMarkerPosition(data, 4)
    }

    fun part2(input: List<String>): Int {
        val data = input[0]

        return getMarkerPosition(data, 14)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}

fun getMarkerPosition(input: String, markerLength: Int): Int {
    var messageStart = 0
    for (i in markerLength until input.length) {
        val marker = input.substring(i-markerLength, i)
        val markerSet = marker.toSet()
        //println("Checking marker: $marker. The set is: $markerSet")

        if (markerSet.count() == markerLength) {
            messageStart = i
            break
        }
    }

    return messageStart
}