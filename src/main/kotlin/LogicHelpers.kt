fun <E> Collection<E>.cartesianProduct(other: Collection<E>): Collection<Pair<E, E>> {
    return this.flatMap { left -> other.map { right -> left to right } }
}