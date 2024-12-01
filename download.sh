#!/usr/bin/env bash

source .env

DAY="$1"
DAY_FORMATTED="0$DAY"
curl -v https://adventofcode.com/2024/day/$DAY/input --cookie "session=${SESSION}" > src/main/resources/day$DAY_FORMATTED.txt

cat << EOF > src/main/kotlin/Day$DAY_FORMATTED.kt
fun main() {
    val lines = InputData.readLines("day$DAY_FORMATTED.txt")
    solve("Part 1", null) {
    }
    solve("Part 2", null) {
    }

}
EOF
