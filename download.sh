source .env
DAY=$1
curl -v https://adventofcode.com/2018/day/$DAY/input --cookie "session=${SESSION}" > src/main/resources/day$DAY.txt