data class FileSystem(private val root: File.Directory, var current: File.Directory, val totalSize: Long) {

    fun mkdir(name: String) {
        if (current.data[name] == null)
            current.data[name] = File.Directory(name = name, parent = current)
    }

    fun newFile(name: String, size: Long) {
        current.data[name] = File.Other(name = name, size = size, parent = current)
    }

    fun findDir(filter: Filter): List<File> {
        return when (filter) {
            is Filter.Size.LTE -> find(filter)
            is Filter.Size.GTE -> find(filter)
        }
    }

    fun freeSpace() = totalSize - root.size

    fun exec(cmd: Command) {
        when (cmd) {
            is Command.CD -> cd(cmd.arg)
            is Command.LS -> ls(recursive = cmd.recursive)
        }
    }

    private fun cd(arg: String) {
        if (arg == "/") {
            current = root
        } else if (arg == "..") {
            current.parent?.let { current = it }
        } else if (current.data[arg] != null && current.data[arg] is File.Directory) {
            current = current.data[arg] as File.Directory
        }
    }

    private fun ls(dir: File.Directory = current, recursive: Boolean = false, indent: String = "") {
        println("$indent- ${dir.name} (dir, size=${dir.size})")
        dir.data.forEach {
            if (recursive && it.value is File.Directory) {
                ls(it.value as File.Directory, true, indent + '\t')
            } else {
                println("\t$indent- ${it.key} (${it.value.javaClass.simpleName}, size=${it.value.size}}")
            }
        }
    }

    private fun find(filter: Filter.Size.LTE): List<File> {
        return flattenDirs().filter { it.size <= filter.size }
    }

    private fun find(filter: Filter.Size.GTE): List<File> {
        return flattenDirs().filter { it.size >= filter.size }
    }

    private fun flattenDirs(dir: File.Directory = root, result: MutableList<File> = mutableListOf(root)): List<File> {
        val dirs = dir.data.filter { it.value is File.Directory }.values
        result.addAll(dirs)
        dirs.forEach {
            flattenDirs(it as File.Directory, result)
        }
        return result
    }

    private fun updateDirSize(dir: File.Directory = current, size: Long) {
        dir.size += size
        if (dir.parent != null)
            updateDirSize(dir.parent, size)
    }

    companion object {
        fun init(input: List<String>, totalSize: Long = 70000000): FileSystem {
            val root: File.Directory = File.Directory(name = "/", parent = null)
            val fs = FileSystem(root, root, totalSize)
            input.drop(1).filter { it != "$ ls" }.forEach {
                if (it.startsWith("$")) {
                    Command.parseCommand(it)?.let { fs.exec(it) }
                } else if (it.startsWith("dir ")) {
                    fs.mkdir(it.split(' ').component2())
                } else {
                    val (size, name) = it.split(' ')
                    fs.newFile(name, size.toLong())
                    fs.updateDirSize(size = size.toLong())
                }
            }
            return fs
        }
    }
}

sealed class File(open val name: String, open var size: Long = 0, open val parent: Directory?) {
    data class Directory(
        override val name: String,
        override var size: Long = 0,
        override val parent: Directory?,
        val data: MutableMap<String, File> = mutableMapOf(),
    ) : File(name, size, parent)
    data class Other(
        override val name: String,
        override var size: Long,
        override val parent: Directory?
    ) : File(name, size, parent)
}

sealed interface Command {
    data class CD(val arg: String) : Command
    class LS(val recursive: Boolean = false) : Command

    companion object {
        fun parseCommand(command: String): Command? {
            val tokens = command.split(' ')
            return when (tokens.component2()) {
                "cd" -> CD(tokens.component3())
                "ls" -> LS()
                else -> null
            }
        }
    }
}

sealed interface Filter {
    sealed interface Size : Filter {
        data class LTE(val size: Long): Size
        data class GTE(val size: Long): Size
    }
}

fun main() {
    fun part1(fs: FileSystem) = fs.findDir(Filter.Size.LTE(100000)).sumOf { it.size }

    fun part2(fs: FileSystem) = fs.findDir(Filter.Size.GTE(30000000.toLong() - fs.freeSpace())).minBy { it.size }.size

    // test if implementation meets criteria from the description, like:
    val fsTest = FileSystem.init(readInput("Day07_test"))
    check(part1(fsTest) == 95437.toLong())
    check(part2(fsTest) == 24933642.toLong())

    val fs = FileSystem.init(readInput("Day07"))
    println(part1(fs))
    println(part2(fs))
}
