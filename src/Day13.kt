sealed class Packet {
    data class Integer(val value: Int) : Packet() {
        override fun toString(): String {
            return value.toString()
        }
    }

    data class Data(val data: List<Packet> = listOf()) : Packet() {
        override fun toString(): String {
            return data.toString()
        }
    }
}

data class Compare(val first: Packet, val second: Packet)

fun String.findClosingBracketIndex(openBracketPos: Int): Int {
    var leftBracketCount = 0
    var rightBracketIndex = 0

    for ((index, char) in this.substring(openBracketPos + 1).withIndex()) {
        if (char == '[') {
            leftBracketCount++
            continue
        }

        if (char == ']') {
            if (leftBracketCount > 0) {
                leftBracketCount--
                continue
            }

            if (leftBracketCount == 0) {
                rightBracketIndex = index
                break
            }
        }
    }

    return rightBracketIndex
}

fun parsePacketData(input: String): Packet {
    if (input.isEmpty()) {
        return Packet.Data(listOf())
    }

    val currentPacket = mutableListOf<Packet>()

    var index = 0
    while (index < input.length) {
        var currentChar = input[index]
        if (currentChar == '[') {
            val rightBracketIndex = input.findClosingBracketIndex(index) + 1
            val remainingInput = input.subSequence(index + 1, index + rightBracketIndex)

            var child = parsePacketData(remainingInput.toString())
            currentPacket.add(child)
            index += rightBracketIndex + 1
            continue
        }
        // Found an array split
        else if (currentChar == ',') {
            index++
            continue
        }
        else {
            var digitToParse = ""

            while (currentChar.isDigit()) {
                digitToParse += currentChar
                index++

                if (index == input.length) {
                    break
                }

                currentChar = input[index]
                continue
            }

            currentPacket.add(Packet.Integer(digitToParse.toInt()))

            continue
        }
    }

    return Packet.Data(currentPacket)
}

fun Packet.Integer.compare(other: Packet.Integer, depth: Int = 0): Int {
    log("- Compare $this vs $other", depth)
    if (this.value == other.value) {
        return 0
    } else if (this.value < other.value) {
        log("- Left side is smaller, so inputs are in the right order", depth + 1)
        return 1
    }
    log("- Right side is smaller, so inputs are not in the right order", depth + 1)
    return -1
}

fun Packet.compare(other: Packet, depth: Int = 0): Int {
    return when {
        this is Packet.Integer && other is Packet.Integer -> this.compare(other, depth + 1)
        this is Packet.Data && other is Packet.Data -> this.compare(other, depth)
        this is Packet.Integer -> {
            log("- Mixed types; convert left to [$this] and retry comparison", depth + 1)
            Packet.Data(listOf(this)).compare(other, depth + 1)
        }
        other is Packet.Integer -> {
            log("- Mixed types; convert right to [$other] and retry comparison", depth + 1)
            this.compare(Packet.Data(listOf(other)), depth + 1)
        }
        else -> error("No other scenarios")
    }
}

fun Packet.Data.compare(other: Packet.Data, depth: Int = 0): Int {
    log("- Compare $this vs $other", depth)
    val firstIterator = this.data.iterator()
    val secondIterator = other.data.iterator()

    while (firstIterator.hasNext() && secondIterator.hasNext()) {
        val firstNext = firstIterator.next()
        val otherNext = secondIterator.next()

        when (val result = firstNext.compare(otherNext)) {
            0 -> continue
            else -> return result
        }
    }

    if (firstIterator.hasNext() && !secondIterator.hasNext()) {
        log("- Right side ran out of items, so inputs are not in the right order", depth + 1)
        return -1
    } else if (!firstIterator.hasNext() && secondIterator.hasNext()) {
        log("- Left side ran out of items, so inputs are in the right order", depth + 1)
        return 1
    } else {
        return 0
    }
}

fun log(input: String, depth: Int) {
    for (i in 0 until depth*2) {
        print(" ")
    }
    println(input)
}

fun main() {
    fun part1(input: List<String>): Int {
        var correctPacketCount = 0

        for ((index, packets) in input.chunked(3).withIndex()) {
            println("== Pair ${index + 1} ==")
            val firstPacket = parsePacketData(packets[0].substring(1, packets[0].length - 1))
            val secondPacket = parsePacketData(packets[1].substring(1, packets[1].length - 1))
            if (firstPacket.compare(secondPacket) == 1) {
                correctPacketCount += index + 1
            }
        }

        println("Correct Packet Count: $correctPacketCount")
        return correctPacketCount
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}