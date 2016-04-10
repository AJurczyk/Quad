package utils;

import java.util.Arrays;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public class MathUtils {

    /**
     * Hidden constructor.
     */
    private MathUtils() {

    }

    /**
     * Calculate median from the set.
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
}
