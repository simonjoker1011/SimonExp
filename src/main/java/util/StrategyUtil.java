package util;

import java.util.Collection;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class StrategyUtil {

    public static Double arithmeticMean(Collection<Float> collection) {
        double[] array = collection.stream().mapToDouble(d -> d).toArray();
        return new Mean().evaluate(array);
    }

    public static Double standardDeviation(Collection<Float> collection) {
        double[] array = collection.stream().mapToDouble(d -> d).toArray();
        return new StandardDeviation().evaluate(array);
    }
}
