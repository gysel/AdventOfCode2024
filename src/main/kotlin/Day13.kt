fun main() {
    val input = InputData.read("day13.txt")
        .split("\n\n")
        .map(::parsePuzzle)

    val sampleInput: List<Puzzle> = """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400
    
            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176
    
            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450
    
            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279
        """.trimIndent().split("\n\n").map(::parsePuzzle)

    solve("Sample part 1", 480) {
        sampleInput.mapNotNull(Puzzle::solve)
            .sum()
    }
    solve("Part 1", 37901) {
        input.mapNotNull { it.solve() }
            .sum()
    }
    solve("Sample part 2", null) {
        sampleInput.correctConversionError()
            .mapNotNull(Puzzle::solve)
            .sum()
    }
    solve("Part 2", 77407675412647) {
        input.correctConversionError()
            .mapNotNull(Puzzle::solve)
            .sum()
    }

}

fun parsePuzzle(text: String): Puzzle {
    val regex = Regex(".*: .[+=](\\d+), .[+=](\\d+)")
    val (a, b, p) = text.lines().map {
        val match = regex.matchEntire(it) ?: error("Invalid format")
        val (x, y) = match.destructured
        Coordinates(x, y)
    }
    return Puzzle(a, b, p)
}

data class Puzzle(val buttonA: Coordinates, val buttonB: Coordinates, val prize: Coordinates) {
    /**
     * Disclaimer, this is NOT AI free code...
     * PROMPT: xa + yc = e AND xb + yd = f. Solve by x and y
     *
     * x=Button A, y=Button B
     * a,b=Button A factors
     * c,d=Button B factors
     * e,f=Puzzle prize
     *
     * x = (f * c - e * d) / (b * c - a * d)
     * y = (e * b - f * a) / (b * c - a * d)
     *
     * Returns the tokens needed to solve it, or null if impossible.
     */
    fun solve(): Long? {
        val x = (prize.y * buttonB.x - prize.x * buttonB.y) / (buttonA.y * buttonB.x - buttonA.x * buttonB.y)
        val y = (prize.x * buttonA.y - prize.y * buttonA.x) / (buttonA.y * buttonB.x - buttonA.x * buttonB.y)
        if (x * buttonA.x + y * buttonB.x != prize.x || x * buttonA.y + y * buttonB.y != prize.y) {
            return null
        }
        // println("pushing A $x times and B $y times")
        return x * 3 + y
    }
}

private fun List<Puzzle>.correctConversionError(): List<Puzzle> {
    val correction = 10_000_000_000_000
    return this.map {
        it.copy(
            prize = Coordinates(
                it.prize.x + correction,
                it.prize.y + correction
            )
        )
    }
}
