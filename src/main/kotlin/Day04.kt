fun main() {
    val lines = InputData.readLines("day04.txt")

    val size = 140
    val grid: Array<Array<Char>> = Array(size) { Array(size) { '.' } }

    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            grid[x][y] = char
        }
    }

    solve("Part 1", 2468) {
        var count = 0
        var currentGrid = grid
        repeat(4) {
            currentGrid = rotate(currentGrid)
            // horizontal, vertical, written backwards
            (0..<size).forEach { y ->
                (0..<size - 3).forEach { x ->
                    // search XMAS
                    if (currentGrid[x][y] == 'X'
                        && currentGrid[x + 1][y] == 'M'
                        && currentGrid[x + 2][y] == 'A'
                        && currentGrid[x + 3][y] == 'S'
                    ) {
                        count++
                    }
                }
            }
            // diagonal
            (3..<size).forEach { y ->
                (0..<size - 3).forEach { x ->
                    // search XMAS
                    if (currentGrid[x][y] == 'X'
                        && currentGrid[x + 1][y - 1] == 'M'
                        && currentGrid[x + 2][y - 2] == 'A'
                        && currentGrid[x + 3][y - 3] == 'S'
                    ) {
                        count++
                    }
                }
            }
        }
        count
    }
    solve("Part 2", null) {
        var count = 0
        var currentGrid = grid
        repeat(4) {
            currentGrid = rotate(currentGrid)
            // horizontal, vertical, written backwards
            (0..<size - 2).forEach { y ->
                (0..<size - 2).forEach { x ->
                    // search X-MAS
                    if (currentGrid[x][y] == 'M'
                        && currentGrid[x + 1][y + 1] == 'A'
                        && currentGrid[x + 2][y + 2] == 'S'
                        && currentGrid[x][y + 2] == 'M'
                        && currentGrid[x + 2][y] == 'S'
                    ) {
                        count++
                    }
                }
            }
        }
        count
    }
}

/**
 * Rotate the grid 90 degree clockwise
 */
fun rotate(currentGrid: Array<Array<Char>>): Array<Array<Char>> {
    val size = 140
    val grid: Array<Array<Char>> = Array(size) { Array(size) { '.' } }
    (0..<size).forEach { y ->
        (0..<size).forEach { x ->
            grid[y][size - x - 1] = currentGrid[x][y]
        }
    }
    return grid
}
