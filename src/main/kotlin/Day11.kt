import java.util.*

fun main() {
    val sampleInput = listOf<Long>(125, 17)
    solve("Example 1", 22) {
        runBlinks(sampleInput, 6)
    }

    val input: List<Long> = InputData.read("day11.txt").split(" ").map { it.toLong() }
    solve("Part 1", 224529) {
        runBlinks(input, 25)
    }

    solve("Part 2", 266820198587914L) {
        runBlinks(input, 75)
    }
}

private fun runBlinks(input: List<Long>, totalBlinks: Int): Long {
    var stoneCount = 0L;
    val queue = LinkedList<StoneAtBlink>()
    queue.addAll(input.map { StoneAtBlink(it, 0) })
    val cache = mutableMapOf<StoneAtBlink, Long>()
    while (queue.isNotEmpty()) {
        val stoneAtBlink = queue.removeLast()
        if (stoneAtBlink.blink == totalBlinks) {
            cache[stoneAtBlink] = 1
            stoneCount++
        } else if (stoneAtBlink in cache) {
            stoneCount += cache[stoneAtBlink]!!
        } else {
            val nextStones = stoneAtBlink.processBlink()
            if (nextStones.all { it in cache }) {
                // all next stones are in the cache, add the current stone+blink to the cache!
                nextStones.sumOf { cache[it]!! }.let { stoneCountFromCache ->
                    cache[stoneAtBlink] = stoneCountFromCache
                    stoneCount += stoneCountFromCache
                }
            } else {
                queue.addAll(nextStones)
            }
        }
    }
    return stoneCount
}

data class StoneAtBlink(val stone: Long, val blink: Int) {
    fun processBlink(): List<StoneAtBlink> {
        val nextBlink = blink + 1
        return when {
            stone == 0L -> listOf(StoneAtBlink(1L, nextBlink))

            (stone.toString().length % 2 == 0) -> {
                val number = stone.toString()
                val middle = number.length / 2
                listOf(number.substring(0, middle).toLong(), number.substring(middle, number.length).toLong())
                    .map { StoneAtBlink(it, nextBlink) }
            }

            else -> listOf(StoneAtBlink(Math.multiplyExact(stone, 2024), nextBlink))
        }
    }
}