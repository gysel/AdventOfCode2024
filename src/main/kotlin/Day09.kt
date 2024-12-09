import java.util.*

fun main() {
    val input = InputData.read("day09.txt").let(::parseInput)
    val sampleInput = "2333133121414131402".let(::parseInput)

    solve("Sample 1", 1928) {
        sampleInput.compact().calculateChecksum()
    }
    solve("Part 1", 6395800119709) {
        input.compact().calculateChecksum()
    }
    solve("Sample part 2", 2858) {
        sampleInput.compactFiles().calculateChecksum()
    }
    solve("Part 2", null) {
        input.compactFiles().calculateChecksum()
    }

}

private fun parseInput(input: String): List<DiskMapEntry> {
    var id = 0
    return input.trim().map(Char::digitToInt).chunked(2).flatMap { chunk: List<Int> ->
        val blockSize = chunk[0]
        val currentId = id++
        val entries = (1..blockSize).map { DiskMapEntry(currentId.toLong(), blockSize) }
        val empty = if (chunk.size == 2) {
            val freeSize = chunk[1]
            (1..freeSize).map { DiskMapEntry.EMPTY }
        } else emptyList()
        entries + empty
    }
}

private fun List<DiskMapEntry>.compact(): List<DiskMapEntry> {
    val compacted = mutableListOf<DiskMapEntry>()
    val toProcess = LinkedList(this.toMutableList())
    while (toProcess.isNotEmpty()) {
        val item = toProcess.removeFirst()
        if (item.isEmpty) {
            if (toProcess.isNotEmpty()) {
                var toMove: DiskMapEntry
                do {
                    toMove = toProcess.removeLast()
                } while (toMove.isEmpty && toProcess.isNotEmpty())
                compacted.add(toMove)
            }
        } else {
            compacted.add(item)
        }
    }
    return compacted.toList()
}


private fun List<DiskMapEntry>.compactFiles(): List<DiskMapEntry> {
    val compacted = mutableListOf<DiskMapEntry>()
    compacted.addAll(this)
    // Attempt to move each file exactly once in order of decreasing file ID number
    // starting with the file with the highest file ID number.
    val filesToMove = this.distinct().filter { !it.isEmpty }.sortedByDescending { it.id }
    // compacted.map { it.toString() }.joinToString("").let(::println)
    filesToMove.forEach { fileToMove ->
        // println("Moving ${fileToMove.id}")
        val currentIndex = compacted.asSequence()
            .mapIndexed { index, diskMapEntry -> index to diskMapEntry }
            .first { it.second == fileToMove }.first
        // TODO improve the calculation of freeSlotStartIndex
        val freeSlotStartIndex: Int? =
            compacted.zip(0..<compacted.size).asSequence().windowed(fileToMove.fileSize) { window ->
                val isMoveLeft = window.first().second < currentIndex
                val fits = lazy { window.all { it.first.isEmpty } }
                (isMoveLeft && fits.value) to window.first().second
            }.firstOrNull { it.first }?.second
        if (freeSlotStartIndex != null) {
            compacted.replaceAll { if (it.id == fileToMove.id) DiskMapEntry.EMPTY else it }
            (freeSlotStartIndex..<freeSlotStartIndex + fileToMove.fileSize).forEach { index ->
                compacted[index] = fileToMove
            }
        }
        // compacted.map { it.toString() }.joinToString("").let(::println)
    }
    return compacted.toList()
}

private fun List<DiskMapEntry>.calculateChecksum(): Long {
    return this.mapIndexed { index, entry ->
        (entry.id ?: 0L) * index
    }.sum()
}

data class DiskMapEntry(val id: Long?, val fileSize: Int) {
    val isEmpty = id == null

    override fun toString(): String {
        return if (isEmpty) "." else id.toString()
    }

    companion object {
        val EMPTY = DiskMapEntry(null, 0)
    }
}