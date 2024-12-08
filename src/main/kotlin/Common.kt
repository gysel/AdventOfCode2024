import kotlinx.coroutines.runBlocking

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

fun <T> solve(part: String, correctSolution: T?, function: suspend () -> T) {
    val start = System.currentTimeMillis()
    val solution = runBlocking {
        function()
    }
    if (correctSolution != null && solution != correctSolution) {
        throw IllegalStateException("Wrong solution for $part! Expected is $correctSolution, but result was $solution.")
    }
    val millis = System.currentTimeMillis() - start
    val seconds = millis / 1000
    val duration = if (seconds > 0) "${seconds}s ${millis % 1000}ms" else "${millis}ms"
    println("Solution of $part is '$solution', calculation took $duration")
}
