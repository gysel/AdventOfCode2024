fun main() {
    val data = InputData.read("day03.txt")

    solve("Part 1", 175015740L) {
        val regex = Regex("mul\\((\\d+),(\\d+)\\)")
        regex.findAll(data).sumOf { match ->
            match.groupValues
                .drop(1)
                .map { it.toLong() }
                .reduce(Long::times)
        }
    }

    solve("Part 2", 112272912L) {
        val regex = Regex("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)")
        var result = 0L
        var enabled = true
        regex.findAll(data).forEach { match ->
            when (match.value) {
                "do()" -> enabled = true
                "don't()" -> enabled = false
                else -> {
                    if (enabled) {
                        result += match.groupValues
                            .drop(1)
                            .map { it.toLong() }
                            .reduce(Long::times)
                    }
                }
            }
        }
        result
    }
}