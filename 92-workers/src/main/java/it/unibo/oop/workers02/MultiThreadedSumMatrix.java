package it.unibo.oop.workers02;

import java.util.ArrayList;
import java.util.List;

public final class MultiThreadedSumMatrix implements SumMatrix {

    private final int nThreads; 

    public MultiThreadedSumMatrix(final int nThreads) {
        this.nThreads = nThreads;
    }

    private static class Worker extends Thread {
        private final List<Double> list;
        private final int startPos;
        private final int nElems;
        private double res;

        Worker(final List<Double> list, final int startPos, final int nElems) {
            super();
            this.list = list;
            this.startPos = startPos;
            this.nElems = nElems;
        }

        public void run() {
            System.out.println("Working from positon " + this.startPos + " to position " + (this.startPos + nElems - 1));
            for (int i = this.startPos; i < this.list.size() && i < this.startPos + this.nElems; i++) {
                this.res += this.list.get(i);
            }
        }

        public double getResult() {
            return this.res;
        }

    }
    @Override
    public double sum(double[][] matrix) {
        //The matrix is converted in a list
        final List<Double> list = new ArrayList<Double>(matrix.length);
        for (var row: matrix) { 
            for (var number: row) {
                list.add(number);
            }
        }
        final int size = list.size() % this.nThreads + list.size() / this.nThreads;
        final List<Worker> workers = new ArrayList<>(this.nThreads);
        for (int i = 0; i < list.size(); i += size) {
            workers.add(new Worker(list, i, size));
        }
        for (final var worker: workers) {
            worker.start();
        }
        double sum = 0.0;
        for (final var worker: workers) {
            try {
                worker.join();
                sum += worker.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return sum;
    }
    
}