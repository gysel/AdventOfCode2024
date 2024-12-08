/**
 * 0,0 is bottom left
 */
fun parseCharacterGrid(lines: List<String>): Map<Coordinates, Char> {
    return lines
        .reversed()
        .flatMapIndexed { y: Int, line: String ->
            line.mapIndexed { x, c ->
                Coordinates(x, y) to c
            }
        }
        .toMap()
}
