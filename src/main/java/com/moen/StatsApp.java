package com.moen;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class StatsApp extends Application {

    private DataSet currentData = new DataSet();
    private DataSet xData = new DataSet();
    private DataSet yData = new DataSet();

    private TextArea dataInputArea;
    private TextArea xInputArea;
    private TextArea yInputArea;
    private VBox resultsBox;
    private StackPane chartContainer;
    private TabPane mainTabs;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-pane");

        // Header
        Label header = new Label("Statistical Calculator");
        header.getStyleClass().add("header");
        HBox headerBox = new HBox(header);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20));
        headerBox.getStyleClass().add("header-box");

        // Main tabs
        mainTabs = new TabPane();
        mainTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab descriptiveTab = new Tab("Descriptive Stats", createDescriptivePane());
        Tab regressionTab = new Tab("Regression", createRegressionPane());
        Tab visualTab = new Tab("Visualizations", createVisualizationPane());

        mainTabs.getTabs().addAll(descriptiveTab, regressionTab, visualTab);

        root.setTop(headerBox);
        root.setCenter(mainTabs);

        Scene scene = new Scene(root, 900, 700);
        scene.getStylesheets().add(getClass().getResource("/styles.css") != null
                ? getClass().getResource("/styles.css").toExternalForm()
                : "");

        // Apply inline styles as fallback
        applyInlineStyles(scene);

        stage.setTitle("Statistical Calculator by Moen");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createDescriptivePane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(20));
        pane.getStyleClass().add("content-pane");

        // Input section
        Label inputLabel = new Label("Enter data (comma or newline separated):");
        inputLabel.getStyleClass().add("section-label");

        dataInputArea = new TextArea();
        dataInputArea.setPromptText("e.g., 12, 15, 18, 22, 25, 30...");
        dataInputArea.setPrefRowCount(4);
        dataInputArea.getStyleClass().add("data-input");

        HBox buttonBox = new HBox(10);
        Button calcButton = new Button("Calculate Statistics");
        calcButton.getStyleClass().add("primary-button");
        Button clearButton = new Button("Clear");
        clearButton.getStyleClass().add("secondary-button");
        Button sampleButton = new Button("Load Sample Data");
        sampleButton.getStyleClass().add("secondary-button");

        buttonBox.getChildren().addAll(calcButton, clearButton, sampleButton);

        // Results section
        Label resultsLabel = new Label("Results:");
        resultsLabel.getStyleClass().add("section-label");

        resultsBox = new VBox(8);
        resultsBox.getStyleClass().add("results-box");
        resultsBox.setPadding(new Insets(15));

        ScrollPane resultsScroll = new ScrollPane(resultsBox);
        resultsScroll.setFitToWidth(true);
        resultsScroll.setPrefHeight(350);
        resultsScroll.getStyleClass().add("results-scroll");

        // Event handlers
        calcButton.setOnAction(e -> calculateDescriptive());
        clearButton.setOnAction(e -> {
            dataInputArea.clear();
            resultsBox.getChildren().clear();
            currentData.clear();
        });
        sampleButton.setOnAction(e -> {
            dataInputArea.setText("12, 13, 12, 15, 18, 22, 22, 25, 28, 30, 35, 40, 44");
        });

        pane.getChildren().addAll(inputLabel, dataInputArea, buttonBox, resultsLabel, resultsScroll);
        return pane;
    }

    private VBox createRegressionPane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(20));
        pane.getStyleClass().add("content-pane");

        HBox inputSection = new HBox(20);

        // X data input
        VBox xBox = new VBox(8);
        Label xLabel = new Label("X Values:");
        xLabel.getStyleClass().add("section-label");
        xInputArea = new TextArea();
        xInputArea.setPromptText("1, 2, 3, 4, 5...");
        xInputArea.setPrefRowCount(5);
        xInputArea.setPrefWidth(300);
        xBox.getChildren().addAll(xLabel, xInputArea);

        // Y data input
        VBox yBox = new VBox(8);
        Label yLabel = new Label("Y Values:");
        yLabel.getStyleClass().add("section-label");
        yInputArea = new TextArea();
        yInputArea.setPromptText("2, 4, 5, 4, 5...");
        yInputArea.setPrefRowCount(5);
        yInputArea.setPrefWidth(300);
        yBox.getChildren().addAll(yLabel, yInputArea);

        inputSection.getChildren().addAll(xBox, yBox);

        HBox buttonBox = new HBox(10);
        Button calcButton = new Button("Calculate Regression");
        calcButton.getStyleClass().add("primary-button");
        Button clearButton = new Button("Clear");
        clearButton.getStyleClass().add("secondary-button");
        Button sampleButton = new Button("Load Sample Data");
        sampleButton.getStyleClass().add("secondary-button");
        buttonBox.getChildren().addAll(calcButton, clearButton, sampleButton);

        // Results and chart
        HBox resultsAndChart = new HBox(20);

        VBox regResultsBox = new VBox(8);
        regResultsBox.getStyleClass().add("results-box");
        regResultsBox.setPadding(new Insets(15));
        regResultsBox.setPrefWidth(250);

        StackPane scatterContainer = new StackPane();
        scatterContainer.setPrefSize(400, 300);
        scatterContainer.getStyleClass().add("chart-container");

        resultsAndChart.getChildren().addAll(regResultsBox, scatterContainer);
        HBox.setHgrow(scatterContainer, Priority.ALWAYS);

        // Event handlers
        calcButton.setOnAction(e -> calculateRegression(regResultsBox, scatterContainer));
        clearButton.setOnAction(e -> {
            xInputArea.clear();
            yInputArea.clear();
            regResultsBox.getChildren().clear();
            scatterContainer.getChildren().clear();
        });
        sampleButton.setOnAction(e -> {
            xInputArea.setText("1, 2, 3, 4, 5, 6, 7, 8, 9, 10");
            yInputArea.setText("2.1, 4.0, 5.2, 4.8, 6.5, 7.2, 8.0, 9.1, 9.8, 11.2");
        });

        pane.getChildren().addAll(inputSection, buttonBox, resultsAndChart);
        return pane;
    }

    private VBox createVisualizationPane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(20));
        pane.getStyleClass().add("content-pane");

        Label infoLabel = new Label("Visualize your descriptive statistics data:");
        infoLabel.getStyleClass().add("section-label");

        HBox buttonBox = new HBox(10);
        Button histButton = new Button("Histogram");
        histButton.getStyleClass().add("primary-button");
        Button boxButton = new Button("Box Plot Data");
        boxButton.getStyleClass().add("primary-button");
        Button barButton = new Button("Bar Chart");
        barButton.getStyleClass().add("primary-button");
        buttonBox.getChildren().addAll(histButton, boxButton, barButton);

        chartContainer = new StackPane();
        chartContainer.setPrefSize(800, 450);
        chartContainer.getStyleClass().add("chart-container");

        Label placeholder = new Label("Enter data in 'Descriptive Stats' tab, then select a visualization");
        placeholder.getStyleClass().add("placeholder-label");
        chartContainer.getChildren().add(placeholder);

        histButton.setOnAction(e -> showHistogram());
        boxButton.setOnAction(e -> showBoxPlotData());
        barButton.setOnAction(e -> showBarChart());

        pane.getChildren().addAll(infoLabel, buttonBox, chartContainer);
        VBox.setVgrow(chartContainer, Priority.ALWAYS);
        return pane;
    }

    private void calculateDescriptive() {
        try {
            parseData(dataInputArea.getText(), currentData);
            if (currentData.isEmpty()) {
                showError("Please enter valid numeric data");
                return;
            }

            DescriptiveStats stats = new DescriptiveStats(currentData);
            resultsBox.getChildren().clear();

            addResultSection("Central Tendency");
            addResult("Mean", String.format("%.4f", stats.mean()));
            addResult("Median", String.format("%.4f", stats.median()));
            addResult("Mode(s)", formatModes(stats.modes()));

            addResultSection("Range & Spread");
            addResult("Minimum", String.format("%.4f", stats.min()));
            addResult("Maximum", String.format("%.4f", stats.max()));
            addResult("Range", String.format("%.4f", stats.range()));

            addResultSection("Dispersion");
            addResult("Population Variance", String.format("%.4f", stats.variancePopulation()));
            addResult("Sample Variance", String.format("%.4f", stats.varianceSample()));
            addResult("Population Std Dev", String.format("%.4f", stats.stdDevPopulation()));
            addResult("Sample Std Dev", String.format("%.4f", stats.stdDevSample()));

            addResultSection("Quartiles");
            addResult("Q1 (25th percentile)", String.format("%.4f", stats.q1()));
            addResult("Q2 (50th percentile)", String.format("%.4f", stats.q2()));
            addResult("Q3 (75th percentile)", String.format("%.4f", stats.q3()));
            addResult("IQR", String.format("%.4f", stats.iqr()));

            addResultSection("Data Info");
            addResult("Sample Size", String.valueOf(currentData.size()));

        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    private void calculateRegression(VBox resultsBox, StackPane chartContainer) {
        try {
            xData.clear();
            yData.clear();
            parseData(xInputArea.getText(), xData);
            parseData(yInputArea.getText(), yData);

            if (xData.isEmpty() || yData.isEmpty()) {
                showError("Please enter valid X and Y data");
                return;
            }

            RegressionStats reg = new RegressionStats(xData, yData);
            resultsBox.getChildren().clear();

            Label titleLabel = new Label("Regression Results");
            titleLabel.getStyleClass().add("result-section");
            resultsBox.getChildren().add(titleLabel);

            addResultTo(resultsBox, "Equation", reg.equation());
            addResultTo(resultsBox, "Slope (m)", String.format("%.4f", reg.slope()));
            addResultTo(resultsBox, "Intercept (b)", String.format("%.4f", reg.intercept()));
            addResultTo(resultsBox, "Correlation (r)", String.format("%.4f", reg.correlation()));
            addResultTo(resultsBox, "R² ", String.format("%.4f", reg.rSquared()));

            // Interpretation
            double r = Math.abs(reg.correlation());
            String strength;
            if (r >= 0.9) strength = "Very strong";
            else if (r >= 0.7) strength = "Strong";
            else if (r >= 0.5) strength = "Moderate";
            else if (r >= 0.3) strength = "Weak";
            else strength = "Very weak";

            String direction = reg.correlation() >= 0 ? "positive" : "negative";
            Label interpLabel = new Label(strength + " " + direction + " correlation");
            interpLabel.getStyleClass().add("interpretation");
            resultsBox.getChildren().add(interpLabel);

            // Create scatter plot with regression line
            createScatterPlot(chartContainer, reg);

        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    private void createScatterPlot(StackPane container, RegressionStats reg) {
        container.getChildren().clear();

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("X");
        yAxis.setLabel("Y");

        ScatterChart<Number, Number> scatter = new ScatterChart<>(xAxis, yAxis);
        scatter.setTitle("Regression Analysis");
        scatter.setLegendVisible(true);

        XYChart.Series<Number, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Data Points");

        List<Double> xVals = xData.getValues();
        List<Double> yVals = yData.getValues();

        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        for (int i = 0; i < xVals.size(); i++) {
            dataSeries.getData().add(new XYChart.Data<>(xVals.get(i), yVals.get(i)));
            minX = Math.min(minX, xVals.get(i));
            maxX = Math.max(maxX, xVals.get(i));
        }

        // Regression line as series
        XYChart.Series<Number, Number> lineSeries = new XYChart.Series<>();
        lineSeries.setName("Regression Line");
        lineSeries.getData().add(new XYChart.Data<>(minX, reg.predict(minX)));
        lineSeries.getData().add(new XYChart.Data<>(maxX, reg.predict(maxX)));

        scatter.getData().addAll(dataSeries, lineSeries);
        container.getChildren().add(scatter);
    }

    private void showHistogram() {
        if (currentData.isEmpty()) {
            showError("Please calculate descriptive stats first");
            return;
        }

        chartContainer.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Value Range");
        yAxis.setLabel("Frequency");

        BarChart<String, Number> histogram = new BarChart<>(xAxis, yAxis);
        histogram.setTitle("Data Distribution");
        histogram.setLegendVisible(false);
        histogram.setCategoryGap(2);
        histogram.setBarGap(0);

        // Create bins
        List<Double> sorted = currentData.getSorted();
        double min = sorted.get(0);
        double max = sorted.get(sorted.size() - 1);
        int numBins = Math.min(10, currentData.size());
        double binWidth = (max - min) / numBins;

        if (binWidth == 0) binWidth = 1;

        int[] counts = new int[numBins];
        for (double val : sorted) {
            int bin = (int) ((val - min) / binWidth);
            if (bin >= numBins) bin = numBins - 1;
            counts[bin]++;
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < numBins; i++) {
            double binStart = min + i * binWidth;
            double binEnd = binStart + binWidth;
            String label = String.format("%.1f-%.1f", binStart, binEnd);
            series.getData().add(new XYChart.Data<>(label, counts[i]));
        }

        histogram.getData().add(series);
        chartContainer.getChildren().add(histogram);
    }

    private void showBoxPlotData() {
        if (currentData.isEmpty()) {
            showError("Please calculate descriptive stats first");
            return;
        }

        chartContainer.getChildren().clear();

        DescriptiveStats stats = new DescriptiveStats(currentData);

        VBox boxPlotInfo = new VBox(15);
        boxPlotInfo.setAlignment(Pos.CENTER);
        boxPlotInfo.setPadding(new Insets(20));

        Label title = new Label("Box Plot Statistics");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Visual representation using bars
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Five Number Summary");
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Min", stats.min()));
        series.getData().add(new XYChart.Data<>("Q1", stats.q1()));
        series.getData().add(new XYChart.Data<>("Median", stats.median()));
        series.getData().add(new XYChart.Data<>("Q3", stats.q3()));
        series.getData().add(new XYChart.Data<>("Max", stats.max()));

        chart.getData().add(series);

        // Text summary
        GridPane summary = new GridPane();
        summary.setHgap(30);
        summary.setVgap(10);
        summary.setAlignment(Pos.CENTER);

        String[][] data = {
                {"Minimum:", String.format("%.4f", stats.min())},
                {"Q1:", String.format("%.4f", stats.q1())},
                {"Median:", String.format("%.4f", stats.median())},
                {"Q3:", String.format("%.4f", stats.q3())},
                {"Maximum:", String.format("%.4f", stats.max())},
                {"IQR:", String.format("%.4f", stats.iqr())}
        };

        for (int i = 0; i < data.length; i++) {
            Label nameLabel = new Label(data[i][0]);
            nameLabel.setStyle("-fx-font-weight: bold;");
            Label valueLabel = new Label(data[i][1]);
            summary.add(nameLabel, 0, i);
            summary.add(valueLabel, 1, i);
        }

        boxPlotInfo.getChildren().addAll(chart, summary);
        chartContainer.getChildren().add(boxPlotInfo);
    }

    private void showBarChart() {
        if (currentData.isEmpty()) {
            showError("Please calculate descriptive stats first");
            return;
        }

        chartContainer.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Index");
        yAxis.setLabel("Value");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Data Values");
        barChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<Double> values = currentData.getValues();
        for (int i = 0; i < values.size(); i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i + 1), values.get(i)));
        }

        barChart.getData().add(series);
        chartContainer.getChildren().add(barChart);
    }

    private void parseData(String input, DataSet target) {
        target.clear();
        String[] parts = input.split("[,\\s\\n]+");
        for (String part : parts) {
            part = part.trim();
            if (!part.isEmpty()) {
                try {
                    target.add(Double.parseDouble(part));
                } catch (NumberFormatException ignored) {}
            }
        }
    }

    private void addResultSection(String title) {
        Label label = new Label(title);
        label.getStyleClass().add("result-section");
        if (!resultsBox.getChildren().isEmpty()) {
            label.setPadding(new Insets(10, 0, 0, 0));
        }
        resultsBox.getChildren().add(label);
    }

    private void addResult(String name, String value) {
        HBox row = new HBox(10);
        Label nameLabel = new Label(name + ":");
        nameLabel.getStyleClass().add("result-name");
        nameLabel.setMinWidth(150);
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("result-value");
        row.getChildren().addAll(nameLabel, valueLabel);
        resultsBox.getChildren().add(row);
    }

    private void addResultTo(VBox box, String name, String value) {
        HBox row = new HBox(10);
        Label nameLabel = new Label(name + ":");
        nameLabel.getStyleClass().add("result-name");
        nameLabel.setMinWidth(120);
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("result-value");
        row.getChildren().addAll(nameLabel, valueLabel);
        box.getChildren().add(row);
    }

    private String formatModes(List<Double> modes) {
        if (modes.size() > 5) {
            return modes.size() + " modes (multimodal)";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < modes.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(String.format("%.4f", modes.get(i)));
        }
        return sb.toString();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void applyInlineStyles(Scene scene) {
        scene.getRoot().setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #1a1a2e, #16213e);"
        );

        String css = """
            .root-pane {
                -fx-background-color: linear-gradient(to bottom right, #1a1a2e, #16213e);
            }
            .header {
                -fx-font-size: 28px;
                -fx-font-weight: bold;
                -fx-text-fill: #e94560;
            }
            .header-box {
                -fx-background-color: rgba(0,0,0,0.3);
            }
            .content-pane {
                -fx-background-color: transparent;
            }
            .section-label {
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-text-fill: #0f3460;
            }
            .data-input {
                -fx-background-color: #ffffff;
                -fx-border-color: #0f3460;
                -fx-border-radius: 5;
                -fx-background-radius: 5;
            }
            .primary-button {
                -fx-background-color: #e94560;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 10 20;
                -fx-background-radius: 5;
                -fx-cursor: hand;
            }
            .primary-button:hover {
                -fx-background-color: #ff6b6b;
            }
            .secondary-button {
                -fx-background-color: #0f3460;
                -fx-text-fill: white;
                -fx-padding: 10 20;
                -fx-background-radius: 5;
                -fx-cursor: hand;
            }
            .secondary-button:hover {
                -fx-background-color: #1a4a7a;
            }
            .results-box {
                -fx-background-color: rgba(255,255,255,0.95);
                -fx-background-radius: 10;
            }
            .result-section {
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-text-fill: #e94560;
            }
            .result-name {
                -fx-font-weight: bold;
                -fx-text-fill: #0f3460;
            }
            .result-value {
                -fx-text-fill: #333;
                -fx-font-family: monospace;
            }
            .chart-container {
                -fx-background-color: rgba(255,255,255,0.95);
                -fx-background-radius: 10;
            }
            .interpretation {
                -fx-font-style: italic;
                -fx-text-fill: #666;
                -fx-padding: 10 0 0 0;
            }
            .placeholder-label {
                -fx-text-fill: #888;
                -fx-font-size: 14px;
            }
            .tab-pane .tab-header-area .tab-header-background {
                -fx-background-color: #0f3460;
            }
            .tab {
                -fx-background-color: #1a4a7a;
            }
            .tab:selected {
                -fx-background-color: #e94560;
            }
            .tab .tab-label {
                -fx-text-fill: white;
                -fx-font-weight: bold;
            }
            """;
        scene.getRoot().getStyleClass().add("root-pane");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
