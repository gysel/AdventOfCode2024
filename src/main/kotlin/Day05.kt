fun main() {
    val lines = InputData.readLines("day05.txt")

    val rules = lines.takeWhile { it != "" }
        .map { it.split("|").map(String::toInt) }
    val updates: List<List<Int>> = lines.drop(rules.size + 1)
        .map { it.split(",").map(String::toInt) }

    val (correctUpdates, incorrectUpdates) = updates.partition { update ->
        update.all { page ->
            matchesAllRules(update, page, rules)
        }
    }
    solve("Part 1", 4662) {
        correctUpdates.sumOf { it[(it.size - 1) / 2] }
    }
    solve("Part 2", 5900) {
        incorrectUpdates
            .map { sort(it, rules) }
            .sumOf { it[(it.size - 1) / 2] }
    }

}

fun sort(update: List<Int>, rules: List<List<Int>>): List<Int> {
    return update.sortedWith(comparator = { o1, o2 ->
        val relevantRule = rules.find { (x, y) -> (x == o1 && y == o2) or (x == o2 && y == o1) }
        if (relevantRule != null) {
            val (x, _) = relevantRule;
            if (x == o1) {
                -1
            } else {
                1
            }
        } else 0
    })
}

fun matchesAllRules(update: List<Int>, page: Int, rules: List<List<Int>>): Boolean {
    val index = update.indexOf(page)
    val before = update.subList(0, index)
    val after = update.subList(index + 1, update.size)
    val relevantRulesForBefore = rules.filter { it[1] == page }
    val beforeRulesAreCorrect = before.all { pageBefore ->
        // is there any page that does not have a rule mandating is must be before?
        !relevantRulesForBefore.none { it[0] == pageBefore }
    }
    val relevantRulesForAfter = rules.filter { it[0] == page }
    val afterRulesAreCorrect = after.all { pageAfter ->
        // is there any page that does not have a rule mandating is must be after?
        !relevantRulesForAfter.none { it[1] == pageAfter }
    }
    return beforeRulesAreCorrect && afterRulesAreCorrect
}
