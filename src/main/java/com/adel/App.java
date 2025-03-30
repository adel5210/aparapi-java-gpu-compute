package com.adel;

import com.aparapi.Kernel;
import com.aparapi.Range;

import java.util.stream.IntStream;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        final int[] intA = IntStream.range(0, 5_000_000).toArray();
        final int[] intB = IntStream.range(0, 5_000_000).toArray();
        final int[] res = new int[intA.length];

        {
            long time = System.currentTimeMillis();
            for (int i = 0; i < intA.length; i++) {
                res[i] = intA[i] + intB[i];
            }
            System.out.println("Regular: " + (System.currentTimeMillis() - time));
        }
        {
            long time = System.currentTimeMillis();
            final Kernel kernel = new Kernel() {
                @Override
                public void run() {
                    int i = getGlobalId();
                    res[i] = intA[i] + intB[i];
                }
            };
            final Range range = Range.create(res.length);
            kernel.execute(range);
            System.out.println("Aparapi: " + (System.currentTimeMillis() - time));
        }
    }
}
