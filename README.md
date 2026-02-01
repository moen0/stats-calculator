# Statistical Calculator Mini-Project by Moen

A JavaFX-based statistical calculator with visualizations for descriptive statistics, regression analysis, and data visualization.

## Features

### Load your own Data Sets

### Descriptive Statistics
- **Central Tendency**: Mean, Median, Mode(s)
- **Range**: Min, Max, Range
- **Dispersion**: Population/Sample Variance, Population/Sample Standard Deviation
- **Quartiles**: Q1, Q2, Q3, IQR

### Regression Analysis
- Linear regression 
- Slope and intercept calculation
- Pearson correlation coefficient (r)
- R-squared (coefficient of determination)
- Scatter plot with regression line visualization
- Correlation strength interpretation

### Visualizations
- Histogram (data distribution)
- Box plot statistics (five-number summary)
- Bar chart (individual values)

### Hypothesis Testing (HypothesisStats class)
- One-sample t-test
- Two-sample t-test
- Z-test
- Confidence intervals
- Cohen's d effect size


## Building & Running

### With Maven

```bash
# Navigate to project directory
cd stats-calculator

# Compile and run
mvn clean javafx:run

# Or build JAR
mvn clean package
```
 
## License

MIT License - Feel free to use and modify.
