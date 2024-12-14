import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {

    val sampleInput = """
        p=0,4 v=3,-3
        p=6,3 v=-1,-3
        p=10,3 v=-1,2
        p=2,0 v=2,-1
        p=0,0 v=1,3
        p=3,0 v=-2,-2
        p=7,6 v=-1,-3
        p=3,0 v=-1,-2
        p=9,3 v=2,3
        p=7,3 v=-1,2
        p=2,4 v=2,-3
        p=9,5 v=-3,-3
    """.trimIndent().lines().map(::parse)

    solve("Very small example part 1", Coordinates(1, 3)) {
        val sampleDimensions = 11 to 7
        val smallSample = listOf("p=2,4 v=2,-3").map(::parse)
        val state = move(smallSample, 5, sampleDimensions)
        printMap(state, sampleDimensions)
        state.first().first
    }

    solve("Example part 1", 12) {

        val sampleDimensions = 11 to 7
        val state = move(sampleInput, 100, sampleDimensions)
        printMap(state, sampleDimensions)
        // TODO use cartesian product?
        val quadrants = listOf(
            // x to y
            0..<5 to 0..<3,
            6..<11 to 0..<3,
            0..<5 to 4..<7,
            6..<11 to 4..<7
        )
        val byq = quadrants.map { quadrant ->
            val p = state.filter { (p, _) ->
                p.x in quadrant.first && p.y in quadrant.second
            }
            p.size
        }
        byq.reduce(Int::times)
    }
    val input = InputData.readLines("day14.txt").map(::parse)
    solve("Part 1", 210587128) {
        val inputDimensions = 101 to 103
        val state = move(input, 100, inputDimensions)
        // TODO use cartesian product?
        val quadrants = listOf(
            // x to y
            0..<50 to 0..<51,
            51..<101 to 0..<51,
            0..<50 to 52..<103,
            51..<101 to 52..<103
        )
        val byq = quadrants.map { quadrant ->
            val p = state.filter { (p, _) ->
                p.x in quadrant.first && p.y in quadrant.second
            }
            p.size.toLong()
        }
        byq.reduce(Long::times)
    }
    solve("Part 2", 7286) {
        val inputDimensions = 101 to 103
        move(input, 10_000, inputDimensions) { s ->
            // now you need to look at the generated png files and find the Christmas tree
            // true
            // reduce result set after detecting a pattern
            (s - 14) % 101 == 0
        }
        7286
    }

}

val pattern = Regex(""".=(-?\d+),(-?\d+) .=(-?\d+),(-?\d+)""")

private fun parse(line: String): Pair<Coordinates, Coordinates> {
    val (px, py, vx, vy) = (pattern.matchEntire(line) ?: error("unexpected line: $line")).destructured
    return Coordinates(px, py) to Coordinates(vx, vy)
}

private fun move(
    state: List<Pair<Coordinates, Coordinates>>,
    times: Int,
    dimensions: Pair<Int, Int>,
    createImage: (Int) -> Boolean = { _ -> false },
): List<Pair<Coordinates, Coordinates>> {
    var result = state
    repeat(times) { i ->
        result = result.map { (p, v) ->
            teleport(p + v, dimensions) to v
        }
        if (createImage(i + 1)) {
            createImage(result, i + 1, dimensions)
        }
    }
    return result
}

private fun teleport(coordinates: Coordinates, dimensions: Pair<Int, Int>): Coordinates {
    val (maxX, maxY) = dimensions
    return if (coordinates.x !in 0..<maxX || coordinates.y !in 0..<maxY) {
        coordinates.copy(x = (coordinates.x + maxX) % maxX, y = (coordinates.y + maxY) % maxY)
    } else coordinates
}

private fun printMap(state: List<Pair<Coordinates, Coordinates>>, dimensions: Pair<Int, Int>) {
    val (maxX, maxY) = dimensions
    (0..<maxY).forEach { y ->
        (0..<maxX).map { x ->
            state.count { it.first.x.toInt() == x && it.first.y.toInt() == y }
        }.map { if (it == 0) '.' else it.digitToChar() }.joinToString("").let(::println)
    }
}

fun createImage(state: List<Pair<Coordinates, Coordinates>>, elapsedSeconds: Int, dimensions: Pair<Int, Int>) {

    val (width, height) = dimensions
    // Create a new BufferedImage with RGB color model
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    state.forEach { s ->
        image.setRGB(s.first.x.toInt(), s.first.y.toInt(), Color.WHITE.rgb)
    }

    // Save the image to a file
    try {
        val outputFile = File("$elapsedSeconds.png") // Save as PNG
        ImageIO.write(image, "png", outputFile)
        // println("Image saved to ${outputFile.absolutePath}")
    } catch (e: Exception) {
        println("Error saving image: ${e.message}")
    }
}