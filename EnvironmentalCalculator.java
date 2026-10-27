
public class EnvironmentalCalculator {

    public static int estimateDisposals(
            boolean replacing,
            double lifespan,
            int horizon) {

        // Repairing does not automatically discard a device.
        if (!replacing) {
            return 0;
        }

        // Replacing now discards the original device.
        int disposals = 1;

        // Count subsequent devices discarded when replaced.
        for (double time = lifespan;
             time < horizon;
             time += lifespan) {

            disposals++;
        }

        return disposals;
    }

    public static double calculatePenalty(
            int disposals,
            double penaltyPerDevice) {

        return disposals * penaltyPerDevice;
    }
}
