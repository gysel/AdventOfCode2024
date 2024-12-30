/**
 * zeroIsTop == false -> 0,0 is bottom left
 * zeroIsTop == true -> 0,0 is top left
 */
fun parseCharacterGrid(lines: List<String>, zeroIsTop: Boolean = false): Map<Coordinates, Char> {
    return lines
        .let { if (zeroIsTop) it else it.reversed() }
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