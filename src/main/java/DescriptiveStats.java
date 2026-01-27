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


}


