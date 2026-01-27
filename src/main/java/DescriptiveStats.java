import java.util.HashMap;
import java.util.List;
import java.util.Map;


// mean, median, mode, std dev, variance
public class DescriptiveStats {

    private DataSet data;

    public DescriptiveStats(DataSet data) {
        this.data = data;
    }

    public double mean() {
        if (data.isEmpty()) {
            throw new IllegalStateException("Cannot calculate mean of empty dataset");
        }

        double sum = 0;
        for (double value : data.getValues()) {
            sum += value;
        }
        return sum / data.size();
    }

    public double median() {
        if(data.isEmpty()) {
            throw new IllegalStateException("Cannot calculate median of empty dataset")
        }

        List<Double> sorted = data.getSorted();
        int size = sorted.size();
        int middle = size / 2;

        if(size % 2 == 1) {
            return sorted.get(middle);
        }

        return (sorted.get(middle - 1 ) + sorted.get(middle)) / 2.0;
    }
// Finds the mode (most frequently occurring value).

    public double mode() {
        if(data.isEmpty()) {
            throw new IllegalStateException("Cannot calculate mode of empty dataset.")
        }

        Map<Double, Integer> frequency = new HashMap<>();
        for (double value: data.getValues()) {
            frequency.put(value, frequency.getOrDefault(value,0) + 1);
        }
        double mode = 0;
        int maxCount = 0;
        for( Map.Entry<Double, Integer> entry : frequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mode = entry.getKey();
            }
        }
        return mode;
    }

    public double min() {
        if (data.isEmpty()) {
            throw new IllegalStateException("Cannot find min on an empty dataset");
        }
        return data.getSorted().get(0);
    }

    public double max() {
        if (data.isEmpty()) {
            throw new IllegalStateException("Cannot find max on an empty dataset");
        }
        List<Double> sorted = data.getSorted();
        return sorted.get(sorted.size() - 1);
    }

    public double range() {
        return max() - min();
    }

    public double variancePopulation() {
        if (data.isEmpty()) {
            throw new IllegalStateException("Cannot find range on an empty dataset");
        }

        double mean = mean();
        double sumSquaredDiff = 0;

        for (double value : data.getValues()) {
            sumSquaredDiff += Math.pow(value - mean, 2);
        }
        return sumSquaredDiff / data.size();
    }

    public double varianceSample() {
        if (data.size() < 2) {
            throw new IllegalStateException("Need at least 2 values for sample variance");
        }

        double mean = mean();
        double sumSquaredDiff = 0;

        for(double value : data.getValues()) {
            sumSquaredDiff += Math.pow(value - mean, 2);
        }
        return sumSquaredDiff / (data.size() - 1);
    }

    public double stndDevSample() {
        return Math.sqrt(varianceSample());
    }
}


