package utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public final class MathUtils {

    /**
     * Hidden constructor.
     */
    private MathUtils() {

    }

    /**
     * Calculate median from the array.
     *
     * @param args values
     * @return median
     */
    public static double median(double... args) {
        Arrays.sort(args);
        if (args.length % 2 == 0) {
            return (args[args.length / 2 - 1] + args[args.length / 2]) / 2;
        } else {
            return args[(args.length + 1) / 2 - 1];
        }
    }

    /**
     * Calculate median from the list.
     *
     * @param args values
     * @return median
     */
    public static double median(List<Double> args) {
        Collections.sort(args);

        if (args.size() % 2 == 0) {
            return (args.get(args.size() / 2 - 1) + args.get(args.size() / 2)) / 2;
        } else {
            return args.get((args.size() + 1) / 2 - 1);
        }
    }
}
