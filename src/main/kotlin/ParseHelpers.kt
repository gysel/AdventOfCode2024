/**
 * 0,0 is bottom left
 */
fun parseCharacterGrid(lines: List<String>): Map<Coordinates, Char> {
    return lines
        .reversed()
        .flatMapIndexed { y: Int, line: String ->
            line.mapIndexed { x, c ->
                Coordinates(x.toLong(), y.toLong()) to c
            }
        }
        .toMap()
}
/**
 * 0,0 is bottom left
 */
fun parseNumberGrid(lines: List<String>): Map<Coordinates, Int> {
    return lines
        .reversed()
        .flatMapIndexed { y: Int, line: String ->
            line.mapIndexed { x, c ->
                Coordinates(x.toLong(), y.toLong()) to c.digitToInt()
            }
        }
        .toMap()
}