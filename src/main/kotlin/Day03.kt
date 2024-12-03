fun main() {
    val data = InputData.read("day03.txt")

    solve("Part 1", 175015740L) {
        val regex = Regex("mul\\((\\d+),(\\d+)\\)")
        regex.findAll(data).sumOf(::calculateProduct)
    }

    solve("Part 2", 112272912L) {
        val regex = Regex("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)")
        regex.findAll(data)
            .fold(0L to true) { state, match: MatchResult ->
                var (sum, enabled) = state
                when (match.value) {
                    "do()" -> enabled = true
                    "don't()" -> enabled = false
                    else -> {
                        if (enabled) {
                            sum += calculateProduct(match)
                        }
                    }
                }
                sum to enabled
            }.first
    }
}

private fun calculateProduct(match: MatchResult) = match.groupValues
    .drop(1)
    .map { it.toLong() }
    .reduce(Long::times)