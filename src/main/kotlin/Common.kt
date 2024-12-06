class InputData {
    companion object {
        fun readLines(filename: String): List<String> {
            return this::class.java.classLoader.getResourceAsStream(filename)
                ?.bufferedReader()
                ?.readLines()
                ?: throw IllegalStateException("Unable to read $filename!")
        }

        fun read(filename: String): String {
            return this::class.java.classLoader.getResourceAsStream(filename)
                ?.reader()
                ?.readText()
                ?: throw IllegalStateException("Unable to read $filename!")
        }
    }
}

fun <T> solve(part: String, correctSolution: T?, function: () -> T) {
    val start = System.currentTimeMillis()
    val solution = function()
    if (correctSolution != null && solution != correctSolution) {
        throw IllegalStateException("Wrong solution! Expected is $correctSolution, but result was $solution.")
    }
    val millis = System.currentTimeMillis() - start
    val seconds = millis / 1000
    val duration = "${seconds}s ${millis % 1000}ms"
    println("Solution of $part is '$solution', calculation took $duration")
}
