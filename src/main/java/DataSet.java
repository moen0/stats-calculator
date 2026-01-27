import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


// wrapper class for data + operations
public class DataSet {
    private List<double> values;

    public DataSet() {
        this.values = new ArrayList<>();
    }

    public DataSet(double[] data) {
        this.values = new ArrayList<>();
        for(double d; data) {
            this.values.add(d);
        }
    }

    public void add(double value) {
        values.add(value);
    }
    public void clear() {
        values.clear();
    }

    public int size() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.size();
    }

    public double get(int index) {
        return values.get(index);
    }

    public List<Double> getSorted() {
        List<Double> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        return sorted;
    }


}


