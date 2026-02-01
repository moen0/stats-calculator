package com.moen;

public class HypothesisStats {

    private DataSet data;
    private DescriptiveStats stats;

    public HypothesisStats(DataSet data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Dataset cannot be empty");
        }
        this.data = data;
        this.stats = new DescriptiveStats(data);
    }


     // One-sample t-test: tests if sample mean differs from hypothesized population mean. //

    public double tStatistic(double populationMean) {
        if (data.size() < 2) {
            throw new IllegalStateException("Need at least 2 data points for t-test");
        }
        double sampleMean = stats.mean();
        double sampleStdDev = stats.stdDevSample();
        double standardError = sampleStdDev / Math.sqrt(data.size());
        return (sampleMean - populationMean) / standardError;
    }


     // Degrees of freedom for one-sample t-test. //
    public int degreesOfFreedom() {
        return data.size() - 1;
    }


      //Z-test statistic: used when population standard deviation is known. //
    public double zStatistic(double populationMean, double populationStdDev) {
        if (populationStdDev <= 0) {
            throw new IllegalArgumentException("Population std dev must be positive");
        }
        double sampleMean = stats.mean();
        double standardError = populationStdDev / Math.sqrt(data.size());
        return (sampleMean - populationMean) / standardError;
    }


     // standard error of the mean (using sample std dev). //
    public double standardError() {
        return stats.stdDevSample() / Math.sqrt(data.size());
    }


     // Confidence interval for the mean (approximate using t-distribution critical values).
     // Returns [lower, upper] bounds. //
    public double[] confidenceInterval(double confidenceLevel) {
        if (confidenceLevel <= 0 || confidenceLevel >= 1) {
            throw new IllegalArgumentException("Confidence level must be between 0 and 1");
        }

        double mean = stats.mean();
        double se = standardError();

        // Approximate t-critical values for common confidence levels
        double tCritical = getTCritical(confidenceLevel, degreesOfFreedom());

        double margin = tCritical * se;
        return new double[]{mean - margin, mean + margin};
    }

    // approximate t value
    private double getTCritical(double confidence, int df) {
        //  critical values approximation
        if (df >= 30) {
            // Use z approximation for large df
            if (confidence >= 0.99) return 2.576;
            if (confidence >= 0.95) return 1.96;
            if (confidence >= 0.90) return 1.645;
            return 1.28;
        }

        //  sample approximation
        double base;
        if (confidence >= 0.99) base = 2.576;
        else if (confidence >= 0.95) base = 1.96;
        else if (confidence >= 0.90) base = 1.645;
        else base = 1.28;

        // Adjust for small df
        double adjustment = 1 + (3.0 / df);
        return base * adjustment;
    }

    // 2 sample t test
    public static double twoSampleTStatistic(DataSet sample1, DataSet sample2) {
        if (sample1.size() < 2 || sample2.size() < 2) {
            throw new IllegalStateException("Each sample needs at least 2 data points");
        }

        DescriptiveStats stats1 = new DescriptiveStats(sample1);
        DescriptiveStats stats2 = new DescriptiveStats(sample2);

        double mean1 = stats1.mean();
        double mean2 = stats2.mean();
        double var1 = stats1.varianceSample();
        double var2 = stats2.varianceSample();
        int n1 = sample1.size();
        int n2 = sample2.size();

        // Pooled variance
        double pooledVar = ((n1 - 1) * var1 + (n2 - 1) * var2) / (n1 + n2 - 2);
        double standardError = Math.sqrt(pooledVar * (1.0 / n1 + 1.0 / n2));

        return (mean1 - mean2) / standardError;
    }

    /**
     * Degrees of freedom for two-sample t-test (pooled).
     */
    public static int twoSampleDF(DataSet sample1, DataSet sample2) {
        return sample1.size() + sample2.size() - 2;
    }

    /**
     * Cohen's d effect size for one sample.
     */
    public double cohensD(double populationMean) {
        return (stats.mean() - populationMean) / stats.stdDevSample();
    }

    /**
     * Cohen's d effect size for two samples.
     */
    public static double cohensD(DataSet sample1, DataSet sample2) {
        DescriptiveStats stats1 = new DescriptiveStats(sample1);
        DescriptiveStats stats2 = new DescriptiveStats(sample2);

        double mean1 = stats1.mean();
        double mean2 = stats2.mean();
        double var1 = stats1.varianceSample();
        double var2 = stats2.varianceSample();
        int n1 = sample1.size();
        int n2 = sample2.size();

        // Pooled standard deviation
        double pooledStd = Math.sqrt(((n1 - 1) * var1 + (n2 - 1) * var2) / (n1 + n2 - 2));

        return (mean1 - mean2) / pooledStd;
    }
}
