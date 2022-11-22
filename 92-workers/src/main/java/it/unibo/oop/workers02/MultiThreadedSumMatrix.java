package it.unibo.oop.workers02;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MultiThreadedSumMatrix implements SumMatrix {

    private final int nThreads; 

    public MultiThreadedSumMatrix(final int nThreads) {
        if (nThreads < 1) {
            throw new IllegalArgumentException();
        }
        this.nThreads = nThreads;
    }

    private static class Worker extends Thread {
        private final double[][] matrix;
        private final int startPos;
        private final int nElems;
        private double res;

        Worker(final double[][] matrix, final int startPos, final int nElems) {
            super();
            this.matrix = matrix;
            this.startPos = startPos;
            this.nElems = nElems;
        }

        public void run() {
            System.out.println("Working on " + (this.startPos + this.nElems) + " numbers from each row");
            for (int i = this.startPos; i < this.matrix.length && i < this.startPos + this.nElems; i++) {
                for (final var number: this.matrix[i]) {
                    this.res += number;
                }
            }
        }

        public double getResult() {
            return this.res;
        }

    }
    @Override
    public double sum(double[][] matrix) {
        Objects.requireNonNull(matrix);
        final int size = matrix.length / nThreads + matrix.length % nThreads;
        final List<Worker> workers = new ArrayList<>(this.nThreads);
        for (int i = 0; i < matrix.length; i += size) {
            workers.add(new Worker(matrix, i, size));
        }
        for (final var worker: workers) {
            worker.start();
        }
        double sum = 0.0;
        for (final var worker: workers) {
            try{
                worker.join();
                sum += worker.getResult();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return sum;
    }
}