package br.dev.marcoalmeida.service;

public class IdempotentPair<T> {

    private T first;
    private T second;

    public IdempotentPair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    @Override
    public int hashCode() {
        return first.hashCode() ^ second.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof IdempotentPair)) return false;
        IdempotentPair pairo = (IdempotentPair) o;
        return (
            this.first.equals(pairo.getFirst()) &&
            this.second.equals(pairo.getSecond()) ||
            this.first.equals(pairo.getSecond()) &&
            this.second.equals(pairo.getFirst())
        );
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
