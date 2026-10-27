
public class Device {

    private String name;
    private double initialCost;
    private double lifespan;
    private double annualMaintenance;
    private double warrantyYears;

    public Device(
            String name,
            double initialCost,
            double lifespan,
            double annualMaintenance,
            double warrantyYears) {

        this.name = name;
        this.initialCost = initialCost;
        this.lifespan = lifespan;
        this.annualMaintenance = annualMaintenance;
        this.warrantyYears = warrantyYears;
    }

    public String getName() {
        return name;
    }

    public double getInitialCost() {
        return initialCost;
    }

    public double getLifespan() {
        return lifespan;
    }

    public double getAnnualMaintenance() {
        return annualMaintenance;
    }

    public double getWarrantyYears() {
        return warrantyYears;
    }
}
