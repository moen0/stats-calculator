package com.moen;

import java.util.List;

public class RegressionStats {

    private DataSet xData;
    private DataSet yData;

    // regression calc for 2 dataSets
    public RegressionStats(DataSet xData, DataSet yData) {
        if (xData.size() != yData.size()) {
            throw new IllegalArgumentException("Datasets must be the same size");
        }
        if (xData.size() < 2) {
            throw new IllegalArgumentException("Need at least 2 data points");
        }
        this.xData = xData;
        this.yData = yData;
    }


    // calculate the mean of a dataset
    private double mean(DataSet data) {
        double sum = 0;
        for (double value : data.getValues()) {
            sum += value;
        }
        return sum / data.size();
    }

    // calculates the slope (m) of the regression line
    public double slope() {
        double xMean = mean(xData);
        double yMean = mean(yData);

        List<Double> xVals = xData.getValues();
        List<Double> yVals = yData.getValues();

        double numerator = 0;
        double denominator = 0;

        for (int i = 0; i < xData.size(); i++) {
            double xDiff = xVals.get(i) - xMean;
            double yDiff = yVals.get(i) - yMean;
            numerator += xDiff * yDiff;
            denominator += xDiff * xDiff;
        }

        return numerator / denominator;
    }


     // Calculates the y-intercept (b) of the regression line. //
    public double intercept() {
        return mean(yData) - slope() * mean(xData);
    }


     // Predicts y value for a given x using the regression line. //
    public double predict(double x) {
        return slope() * x + intercept();
    }


    public double correlation() {
        double xMean = mean(xData);
        double yMean = mean(yData);

        List<Double> xVals = xData.getValues();
        List<Double> yVals = yData.getValues();

        double numerator = 0;
        double xSumSquares = 0;
        double ySumSquares = 0;

        for (int i = 0; i < xData.size(); i++) {
            double xDiff = xVals.get(i) - xMean;
            double yDiff = yVals.get(i) - yMean;
            numerator += xDiff * yDiff;
            xSumSquares += xDiff * xDiff;
            ySumSquares += yDiff * yDiff;
        }

        return numerator / Math.sqrt(xSumSquares * ySumSquares);
    }

     // Calculates R-squared (coefficient of determination).//
    public double rSquared() {
        double r = correlation();
        return r * r;
    }

    // return eq as String
    public String equation() {
        double m = slope();
        double b = intercept();
        String sign = b >= 0 ? "+" : "-";
        return String.format("y = %.4fx %s %.4f", m, sign, Math.abs(b));
    }
}

