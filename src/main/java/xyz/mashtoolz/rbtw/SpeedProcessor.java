package xyz.mashtoolz.rbtw;

import java.util.*;

public class SpeedProcessor {

    private static final LinkedList<Double> buffer = new LinkedList<>();
    private static final int maxSize = 60;

    public static void clear() {
        buffer.clear();
    }

    public static void add(double value) {
        if (buffer.size() >= maxSize)
            buffer.poll();
        buffer.add(value);
    }

    public static double getAverage() {
        Map<Double, List<Double>> clusters = new HashMap<>();

        for (double value : buffer) {
            boolean added = false;
            for (Map.Entry<Double, List<Double>> entry : clusters.entrySet()) {
                if (Math.abs(entry.getKey() - value) < 0.002) {
                    entry.getValue().add(value);
                    added = true;
                    break;
                }
            }
            if (!added) {
                clusters.put(value, new ArrayList<>(List.of(value)));
            }
        }

        Map.Entry<Double, List<Double>> dominantCluster = clusters.entrySet().stream()
                .max(Comparator.comparingInt(entry -> entry.getValue().size()))
                .orElseThrow();

        return dominantCluster.getKey();
    }
}
