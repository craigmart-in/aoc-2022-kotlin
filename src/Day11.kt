fun main() {
    data class Monkey(val worryLevels: MutableList<ULong>, val operation: String, val test: Int, val nextMonkeyIfTrue: Int, val nextMonkeyIfFalse: Int, var inspectCount: Long = 0)

    fun getMonkeys(input: List<String>): List<Monkey> {
        var monkeys = mutableListOf<Monkey>()

        for (index in input.indices step 7) {
            val startingItems = input[index+1].removePrefix("  Starting items: ").split(", ").map { it.toULong() }
            val operation = input[index+2].removePrefix("  Operation: new = ")
            val test = input[index+3].removePrefix("  Test: divisible by ").toInt()
            val ifTrue = input[index+4].removePrefix("    If true: throw to monkey ").toInt()
            val ifFalse = input[index+5].removePrefix("    If false: throw to monkey ").toInt()

            monkeys.add(Monkey(startingItems.toMutableList(), operation, test, ifTrue, ifFalse))
        }

        return monkeys
    }

    fun inspectItem(currentWorryLevel: ULong, operation: String): ULong {
        var newWorryLevel = 0UL
        var typeOfOperation = ""
        var amount = 0UL

        val tokens = operation.split(" ")

        amount = if (tokens[2].toIntOrNull() == null) {
            currentWorryLevel
        } else {
            tokens[2].toULong()
        }

        newWorryLevel = when(tokens[1]) {
            "*" -> {
                typeOfOperation = "is multiplied"
                currentWorryLevel * amount
            }
            else -> {
                typeOfOperation = "increases"
                currentWorryLevel + amount
            }
        }

        println("    Worry level $typeOfOperation by $amount to $newWorryLevel.")
        return newWorryLevel
    }

    fun testItem(worryLevel: ULong, test: Int): Boolean {
        if (worryLevel % test.toULong() == 0UL) {
            println("    Current worry level is divisible by $test.")
            return true
        }
        println("    Current worry level is not divisible by $test.")
        return false
    }

    fun simulateRounds(monkeys: List<Monkey>, count: Int, isWorried: Boolean, modulus: ULong = 1UL) {
        for (round in 0 until count) {
            for ((index, monkey) in monkeys.withIndex()) {
                println("Monkey $index:")
                for (worryLevel in monkey.worryLevels) {
                    println("  Monkey inspects an item with a worry level of $worryLevel.")
                    var newWorryLevel: ULong = inspectItem(worryLevel, monkey.operation)
                    if (isWorried) {
                        newWorryLevel %= modulus
                    } else {
                        newWorryLevel /= 3UL
                        println("    Monkey gets bored with item. Worry level is divided by 3 to $newWorryLevel.")
                    }

                    val divisible = testItem(newWorryLevel, monkey.test)
                    val nextMonkey = if (divisible) monkey.nextMonkeyIfTrue else monkey.nextMonkeyIfFalse

                    println("    Item with worry level $newWorryLevel is thrown to monkey $nextMonkey.")
                    monkeys[nextMonkey].worryLevels.add(newWorryLevel)
                }
                monkey.inspectCount += monkey.worryLevels.size
                monkey.worryLevels.clear()
            }

            println()
            println("After round ${round + 1}, the monkeys are holding items with these worry levels:")
            for ((index, monkey) in monkeys.withIndex()) {
                print("Monkey $index: ")
                for ((index, item) in monkey.worryLevels.withIndex()) {
                    if (index == monkey.worryLevels.size - 1) {
                        print("$item")
                    } else {
                        print("$item, ")
                    }
                }
                println()
            }
        }
    }

    fun part1(input: List<String>): Long {
        val monkeys = getMonkeys(input)

        simulateRounds(monkeys, 20, false)

        val sortedMonkeys = monkeys.sortedByDescending { it.inspectCount }

        return sortedMonkeys[0].inspectCount * sortedMonkeys[1].inspectCount
    }

    fun part2(input: List<String>): Long {
        val monkeys = getMonkeys(input)

        var modulus = 1UL
        for (monkey in monkeys) {
            modulus *= monkey.test.toULong()
        }

        simulateRounds(monkeys, 10000, true, modulus)

        val sortedMonkeys = monkeys.sortedByDescending { it.inspectCount }

        return (sortedMonkeys[0].inspectCount * sortedMonkeys[1].inspectCount)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
