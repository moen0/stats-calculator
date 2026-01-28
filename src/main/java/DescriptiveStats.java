import java.util.*;


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
            throw new IllegalStateException("Cannot calculate median of empty dataset");
        }

        List<Double> sorted = data.getSorted();
        int size = sorted.size();
        int middle = size / 2;

        if(size % 2 == 1) {
            return sorted.get(middle);
        }

        return (sorted.get(middle - 1 ) + sorted.get(middle)) / 2.0;
    }

    public List<Double> modes() {
        if (data.isEmpty()) {
            throw new IllegalStateException("Cannot calculate mode of empty dataset");
        }

        Map<Double, Integer> frequency = new HashMap<>();
        for (double value : data.getValues()) {
            frequency.put(value, frequency.getOrDefault(value, 0) + 1);
        }

        int maxCount = 0;
        for (int count : frequency.values()) {
            if (count > maxCount) {
                maxCount = count;
            }
        }

        List<Double> modes = new ArrayList<>();
        for (Map.Entry<Double, Integer> entry : frequency.entrySet()) {
            if (entry.getValue() == maxCount) {
                modes.add(entry.getKey());
            }
        }
        Collections.sort(modes);
        return modes;
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

    public double stdDevPopulation() {
        return Math.sqrt(variancePopulation());
    }

    public double stdDevSample() {
        return Math.sqrt(varianceSample());
    }

    public double percentile(double percentile) {
        if (data.isEmpty()) {
            throw new IllegalStateException("Cannot calculate percentile of empty dataset");
        }
        if (percentile < 0 || percentile > 100) {
            throw new IllegalArgumentException("Percentile must be between 0 and 100");
        }

        List<Double> sorted = data.getSorted();

        if (percentile == 0) return sorted.get(0);
        if (percentile == 100) return sorted.get(sorted.size() - 1);

        double position = (percentile / 100) * (sorted.size()) -1 ;
        int lower = (int) Math.floor(position);
        int upper = (int) Math.ceil(position);

        if (lower == upper) {
            return sorted.get(lower);
        }
        double fraction = position - lower;
        return sorted.get(lower) + fraction * (sorted.get(upper) - sorted.get(lower));
    }

    public double q1() {
        return percentile(25);
    }

    public double q2() {
        return percentile(50);
    }


    public double q3() {
        return percentile(75);
    }


    public double iqr() {
        return q3() - q1();
    }
}

