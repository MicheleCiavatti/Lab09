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
        final List<Double> mat = new ArrayList<Double>(matrix.length);
        for (var row: matrix) {
            for (var number: row) {
                mat.add(number);
            }
        }

        return 0;
    }
    
}