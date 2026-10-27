
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("=== Repair vs Replace Helper ===");

        System.out.print("Repair cost: ");
        double repairCost = sc.nextDouble();

        System.out.print("Replacement cost: ");
        double replacementCost = sc.nextDouble();

        System.out.print("Repair lifespan (years): ");
        double repairLife = sc.nextDouble();

        System.out.print("Replacement lifespan (years): ");
        double replacementLife = sc.nextDouble();

        System.out.print("Repair maintenance per year: ");
        double repairMaintenance = sc.nextDouble();

        System.out.print("Replacement maintenance per year: ");
        double replacementMaintenance = sc.nextDouble();

        System.out.print("Repair warranty (years): ");
        double repairWarranty = sc.nextDouble();

        System.out.print("Replacement warranty (years): ");
        double replacementWarranty = sc.nextDouble();

        System.out.print("Comparison period (years): ");
        int horizon = sc.nextInt();

        System.out.print("Penalty per discarded device: ");
        double penalty = sc.nextDouble();

        if (repairCost < 0 || replacementCost < 0
                || repairLife <= 0 || replacementLife <= 0
                || repairMaintenance < 0 || replacementMaintenance < 0
                || repairWarranty < 0 || replacementWarranty < 0
                || horizon <= 0 || penalty < 0) {

            System.out.println("Invalid input.");
            sc.close();
            return;
        }

        Device repair = new Device(
                "Repair", repairCost, repairLife,
                repairMaintenance, repairWarranty);

        Device replacement = new Device(
                "Replace", replacementCost, replacementLife,
                replacementMaintenance, replacementWarranty);

        double repairCostTotal =
                CostCalculator.calculateTotalCost(repair, horizon);

        double replacementCostTotal =
                CostCalculator.calculateTotalCost(replacement, horizon);

        int repairDisposals =
                EnvironmentalCalculator.estimateDisposals(
                        false, repairLife, horizon);

        int replacementDisposals =
                EnvironmentalCalculator.estimateDisposals(
                        true, replacementLife, horizon);

        double repairPenalty =
                EnvironmentalCalculator.calculatePenalty(
                        repairDisposals, penalty);

        double replacementPenalty =
                EnvironmentalCalculator.calculatePenalty(
                        replacementDisposals, penalty);

        double repairAdjusted = repairCostTotal + repairPenalty;
        double replacementAdjusted =
                replacementCostTotal + replacementPenalty;

        System.out.println("\n=== DECISION REPORT ===");

        System.out.printf("Repair financial cost: Rs. %.2f%n",
                repairCostTotal);

        System.out.printf("Replacement financial cost: Rs. %.2f%n",
                replacementCostTotal);

        System.out.println("Repair disposal estimate: " + repairDisposals);
        System.out.println("Replacement disposal estimate: "
                + replacementDisposals);

        System.out.printf("Repair adjusted cost: Rs. %.2f%n",
                repairAdjusted);

        System.out.printf("Replacement adjusted cost: Rs. %.2f%n",
                replacementAdjusted);

        if (repairAdjusted < replacementAdjusted) {
            System.out.println("Recommendation: REPAIR");
        } else if (replacementAdjusted < repairAdjusted) {
            System.out.println("Recommendation: REPLACE");
        } else {
            System.out.println("Recommendation: COSTS ARE EQUAL");
        }

        System.out.println(
                "\nEnvironmental penalties are user-defined estimates.");

        sc.close();
    }
}
