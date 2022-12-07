fun main() {
    fun part1(input: List<String>): Int {
        var rootDirectory = Directory("/", null)
        getFileStructure(rootDirectory, input)

        println(getDirectoryStructureString(rootDirectory))

        val directories = getDirectorySizes(rootDirectory)

        return directories.filter { it.calculateSize() <= 100000 }.sumOf { it.calculateSize() }
    }

    fun part2(input: List<String>): Int {
        val totalDiskSpaceAvailable = 70000000
        val spaceRequired = 30000000

        var rootDirectory = Directory("/", null)
        getFileStructure(rootDirectory, input)

        val directories = getDirectorySizes(rootDirectory)
        val sortedDirectories = directories.sortedBy { it.calculateSize() }

        val usedSpace = rootDirectory.calculateSize()
        val unusedSpace = totalDiskSpaceAvailable - usedSpace
        var directoryToDeleteSize = 0

        for (directory in sortedDirectories) {
            val directorySize = directory.calculateSize()

            if (directorySize == 0) {
                continue
            }

            val remainingSpace = unusedSpace + directorySize
            if (remainingSpace >= spaceRequired) {
                directoryToDeleteSize = directorySize
                break
            }
        }

        return directoryToDeleteSize
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

open class File(val name: String, val size: Int) {
    override fun toString(): String {
        return "$name (file, size=$size)"
    }
}

class Directory(name: String, val parent: Directory?, val files: MutableList<File> = mutableListOf(), var totalSize: Int = 0) : File(name, 0) {

    fun addFile(file: File) {
        totalSize += file.size
        files.add(file)
    }

    fun calculateSize() : Int {
        var size = totalSize

        for (directory in files.filterIsInstance<Directory>()) {
            size += directory.calculateSize()
        }

        return size
    }

    override fun toString(): String {
        return "$name (dir, size=${calculateSize()})"
    }
}

fun getFileStructure(rootDirectory: Directory, input: List<String>) {
    var currentDirectory = rootDirectory

    for (line in input) {
        if (line.startsWith("$ cd")) {
            val directoryName = line.split(" ").last()

            if (directoryName == "..") {
                if (currentDirectory.parent != null) {
                    currentDirectory = currentDirectory.parent!!
                }
            } else if (currentDirectory.name != directoryName) {
                val directory = currentDirectory.files.find { it.name == directoryName } as Directory

                if (directory != null) {
                    currentDirectory = directory
                }
            }
        } else if (line.equals("$ ls")) {
            // Ignore this line. The next few is a list of files/directories
        } else {
            if (line.startsWith("dir")) {
                currentDirectory.files.add(Directory(line.split(" ")[1], currentDirectory))
            } else {
                val fileInfo = line.split(" ")
                currentDirectory.addFile(File(fileInfo[1], fileInfo[0].toInt()))
            }
        }
    }
}

fun getDirectoryStructureString(directory: Directory, depth: Int = 0) : String {
    var stringBuilder = StringBuilder()
    stringBuilder.appendLine("- $directory".filePad(depth))
    for (file in directory.files) {
        if (file is Directory) {
            stringBuilder.append(getDirectoryStructureString(file, depth + 1))
        } else {
            stringBuilder.appendLine("- $file".filePad(depth + 1))
        }
    }

    return stringBuilder.toString()
}

fun String.filePad(depth: Int) : String {
    var padding = ""
    for (i in 0 until depth * 2) {
        padding += " "
    }

    return padding + this
}

fun getDirectorySizes(directory: Directory) : List<Directory> {
    var directories = mutableListOf<Directory>()

    directories.add(directory)

    for (temp in directory.files.filterIsInstance<Directory>()) {
        directories.addAll(getDirectorySizes(temp))
    }

    return directories
}