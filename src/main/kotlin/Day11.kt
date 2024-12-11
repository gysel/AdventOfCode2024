import java.util.*

fun main() {
    val sampleInput = listOf<Long>(125, 17)
    solve("Example 1", 22) {
        runBlinksOptimized(sampleInput, 6)
    }

    val input: List<Long> = InputData.read("day11.txt").split(" ").map { it.toLong() }
    solve("Part 1", 224529) {
        runBlinksOptimized(input, 25)
    }

    solve("Part 2", 266820198587914L) {
        runBlinksOptimized(input, 75)
    }
}

private fun processStone(stone: Long) = when {
    stone == 0L -> listOf(1L)

    (stone.toString().length % 2 == 0) -> {
        val number = stone.toString()
        val middle = number.length / 2
        listOf(number.substring(0, middle).toLong(), number.substring(middle, number.length).toLong())
    }

    else -> listOf(Math.multiplyExact(stone, 2024))
}

private fun runBlinksOptimized(input: List<Long>, totalBlinks: Int): Long {
    var stoneCount = 0L;
    val queue = LinkedList<Pair<Long, Int>>()
    queue.addAll(input.map { it to 0 })
    val cache = mutableMapOf<Pair<Long, Int>, Long>()
    while (queue.isNotEmpty()) {
        val pair = queue.removeLast()
        val (stone, currentBlink) = pair
        if (currentBlink == totalBlinks) {
            // println("Add to cache: $pair (totalBlinks reached)")
            cache[pair] = 1
            stoneCount++
        } else if (pair in cache) {
            stoneCount += cache[pair]!!
        } else {
            val nextStones = processStone(stone).map { it to currentBlink + 1 }
            if (nextStones.all { it in cache }) {
                // all next stones are in the cache, add the current stone+blink to the cache!
                nextStones.sumOf { cache[it]!! }.let { stoneCountFromCache ->
                    // println("Add to cache: $pair (all $stoneCountFromCache next stones are in cache)")
                    cache[pair] = stoneCountFromCache
                    stoneCount += stoneCountFromCache
                }
            } else {
                queue.addAll(nextStones)
            }
        }
    }
    return stoneCount
}
