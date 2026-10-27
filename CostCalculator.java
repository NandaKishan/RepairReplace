
public class CostCalculator {

    public static double calculateTotalCost(
            Device device, int horizon) {

        double totalCost = 0;

        for (double time = 0;
             time < horizon;
             time += device.getLifespan()) {

            double serviceYears = Math.min(
                    device.getLifespan(), horizon - time);

            double uncoveredYears = Math.max(
                    0, serviceYears - device.getWarrantyYears());

            totalCost += device.getInitialCost()
                    + device.getAnnualMaintenance() * uncoveredYears;
        }

        return totalCost;
    }
}
