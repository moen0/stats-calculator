package com.moen;

public class Main {

    public static void main(String[] args) {

        double[] sampleData = {12, 13, 12, 15, 18, 22, 22, 25, 28, 30, 35, 40, 44};
        DataSet data = new DataSet(sampleData);

        DescriptiveStats stats = new DescriptiveStats(data);

        System.out.println("=== Statistical Calculator Demo ===");
        System.out.println("Data: 12, 15 12, 15, 18, 22, 22, 25, 28, 30, 35, 40, 44");
        System.out.println("Sample size: " + data.size());
        System.out.println();

        // basic measures
        System.out.println("--- Central Tendency ---");
        System.out.println("Mean:   " + stats.mean());
        System.out.println("Median: " + stats.median());
        System.out.println("Modes:   " + stats.modes());
        System.out.println();

        // range measures
        System.out.println("--- Range ---");
        System.out.println("Min:   " + stats.min());
        System.out.println("Max:   " + stats.max());
        System.out.println("Range: " + stats.range());
        System.out.println();

        // dispersion measures
        System.out.println("--- Dispersion ---");
        System.out.println("Population Variance: " + stats.variancePopulation());
        System.out.println("Sample Variance:     " + stats.varianceSample());
        System.out.println("Population Std Dev:  " + stats.stdDevPopulation());
        System.out.println("Sample Std Dev:      " + stats.stdDevSample());
        System.out.println();

        // demo adding values dynamically
        System.out.println("--- Dynamic Data Demo ---");
        DataSet dynamic = new DataSet();
        dynamic.add(5);
        dynamic.add(10);
        dynamic.add(15);
        DescriptiveStats dynamicStats = new DescriptiveStats(dynamic);
        System.out.println("Added: 5, 10, 15");
        System.out.println("Mean: " + dynamicStats.mean());
    }
}
