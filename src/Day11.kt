fun main() {
    data class Monkey(val worryLevels: MutableList<Int>, val operation: String, val test: Int, val nextMonkeyIfTrue: Int, val nextMonkeyIfFalse: Int, var inspectCount: Int = 0)

    fun getMonkeys(input: List<String>): List<Monkey> {
        var monkeys = mutableListOf<Monkey>()
        var currentMonkey: Monkey


        for (index in input.indices step 7) {
            val startingItems = input[index+1].removePrefix("  Starting items: ").split(", ").map { it.toInt() }
            val operation = input[index+2].removePrefix("  Operation: new = ")
            val test = input[index+3].removePrefix("  Test: divisible by ").toInt()
            val ifTrue = input[index+4].removePrefix("    If true: throw to monkey ").toInt()
            val ifFalse = input[index+5].removePrefix("    If false: throw to monkey ").toInt()

            monkeys.add(Monkey(startingItems.toMutableList(), operation, test, ifTrue, ifFalse))
        }

        return monkeys
    }

    fun inspectItem(currentWorryLevel: Int, operation: String): Int {
        var newWorryLevel = 0
        var typeOfOperation = ""
        var amount = 0

        val tokens = operation.split(" ")

        amount = if (tokens[2].toIntOrNull() == null) {
            currentWorryLevel
        } else {
            tokens[2].toInt()
        }

        newWorryLevel = when(tokens[1]) {
            "*" -> {
                typeOfOperation = "multiplied"
                return currentWorryLevel * amount
            }
            "/" -> {
                typeOfOperation = "divisible"
                return currentWorryLevel / amount
            }
            "+" -> {
                typeOfOperation = "increases"
                return currentWorryLevel + amount
            }
            else -> {
                typeOfOperation = "decreases"
                return currentWorryLevel - amount
            }
        }

        println("    Worry level is $typeOfOperation by $amount to $newWorryLevel")
        return newWorryLevel
    }

    fun testItem(worryLevel: Int, test: Int): Boolean {
        if (worryLevel % test == 0) {
            println("   Current worry level is divisible by $test")
            return true
        }
        println("   Current worry level is not divisible by $test")
        return false
    }

    fun part1(input: List<String>): Int {
        val monkeys = getMonkeys(input)

        for (round in 0 until 20) {
            for ((index, monkey) in monkeys.withIndex()) {
                println("Monkey $index:")
                for (worryLevel in monkey.worryLevels) {
                    println("  Monkey inspects an item with a worry level of $worryLevel.")
                    val newWorryLevel: Int = inspectItem(worryLevel, monkey.operation) / 3

                    println("    Monkey gets bored with item. Worry level is divided by 3 to $newWorryLevel")

                    val divisble = testItem(newWorryLevel, monkey.test)
                    val nextMonkey = if (divisble) monkey.nextMonkeyIfTrue else monkey.nextMonkeyIfFalse

                    println("    Item with worry level $newWorryLevel is thrown to monkey $nextMonkey")
                    monkeys[nextMonkey].worryLevels.add(newWorryLevel)
                }
                monkey.inspectCount += monkey.worryLevels.size
                monkey.worryLevels.clear()
            }

            println()
            println("After round ${round+1}, the monkeys are holding items with these worry levels:")
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

        val sortedMonkeys = monkeys.sortedByDescending { it.inspectCount }

        return sortedMonkeys[0].inspectCount * sortedMonkeys[1].inspectCount
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
