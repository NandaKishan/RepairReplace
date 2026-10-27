
import javax.swing.*;
import java.awt.*;

public class RepairReplaceGUI extends JFrame {

    private final JTextField repairCost = new JTextField("2000", 12);
    private final JTextField replaceCost = new JTextField("8000", 12);

    private final JTextField repairLife = new JTextField("2", 12);
    private final JTextField replaceLife = new JTextField("5", 12);

    private final JTextField repairMaintenance = new JTextField("1000", 12);
    private final JTextField replaceMaintenance = new JTextField("1500", 12);

    private final JTextField repairWarranty = new JTextField("1", 12);
    private final JTextField replaceWarranty = new JTextField("3", 12);

    private final JTextField horizon = new JTextField("4", 12);
    private final JTextField penalty = new JTextField("500", 12);

    private final JTextArea results = new JTextArea(12, 42);
    private final CostChart chart = new CostChart();

    public RepairReplaceGUI() {

        setTitle("Repair vs Replace Decision Helper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));

        JLabel title = new JLabel(
                "REPAIR vs REPLACE", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(16, 8, 8, 8));

        add(title, BorderLayout.NORTH);

        JPanel inputs = new JPanel(new GridLayout(0, 2, 10, 8));
        inputs.setBorder(BorderFactory.createTitledBorder(
                "Enter device information"));

        addInput(inputs, "Repair cost (Rs.)", repairCost);
        addInput(inputs, "Replacement cost (Rs.)", replaceCost);

        addInput(inputs, "Repair lifespan (years)", repairLife);
        addInput(inputs, "Replacement lifespan (years)", replaceLife);

        addInput(inputs, "Repair maintenance/year (Rs.)",
                repairMaintenance);
        addInput(inputs, "Replacement maintenance/year (Rs.)",
                replaceMaintenance);

        addInput(inputs, "Repair warranty (years)", repairWarranty);
        addInput(inputs, "Replacement warranty (years)",
                replaceWarranty);

        addInput(inputs, "Comparison period (years)", horizon);
        addInput(inputs, "Disposal penalty (Rs.)", penalty);

        JButton compareButton = new JButton("Compare Options");
        compareButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        compareButton.addActionListener(e -> compareOptions());

       
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        center.add(inputs, BorderLayout.NORTH);

        // Give the button a dedicated row so it stays visible.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(compareButton);
        center.add(buttonPanel, BorderLayout.CENTER);


        results.setEditable(false);
        results.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        results.setText("Enter the values and click Compare Options.");

        JScrollPane scrollPane = new JScrollPane(results);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                "Decision report"));

        JPanel reportPanel = new JPanel(new BorderLayout(8, 8));

        reportPanel.add(scrollPane, BorderLayout.CENTER);
        reportPanel.add(chart, BorderLayout.SOUTH);

        center.add(reportPanel, BorderLayout.SOUTH);
        JScrollPane mainScroll = new JScrollPane(center);
        mainScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainScroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(mainScroll, BorderLayout.CENTER);

        setSize(850, 900);
        setLocationRelativeTo(null);
    }

    private void addInput(
            JPanel panel, String label, JTextField field) {

        panel.add(new JLabel(label));
        panel.add(field);
    }

    private double readValue(JTextField field, String name) {
        try {
            double value = Double.parseDouble(field.getText().trim());

            if (!Double.isFinite(value) || value < 0) {
                throw new IllegalArgumentException(
                        name + " must be a finite, non-negative number.");
            }

            return value;

        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "Enter a valid number for " + name + ".");
        }
    }

    private void compareOptions() {

        try {
            double rc = readValue(repairCost, "repair cost");
            double nc = readValue(replaceCost, "replacement cost");

            double rl = readValue(repairLife, "repair lifespan");
            double nl = readValue(replaceLife, "replacement lifespan");

            double rm = readValue(repairMaintenance,
                    "repair maintenance");
            double nm = readValue(replaceMaintenance,
                    "replacement maintenance");

            double rw = readValue(repairWarranty, "repair warranty");
            double nw = readValue(replaceWarranty, "replacement warranty");

            double h = readValue(horizon, "comparison period");
            double p = readValue(penalty, "disposal penalty");

            if (rl <= 0 || nl <= 0 || h <= 0
                    || h != Math.floor(h) || h > 1000) {
                throw new IllegalArgumentException(
                        "Lifespans must be positive and the "
                        + "comparison period must be a whole number "
                        + "between 1 and 1000.");
            }

            int years = (int) h;

            Device repair = new Device(
                    "Repair", rc, rl, rm, rw);

            Device replacement = new Device(
                    "Replace", nc, nl, nm, nw);

            double repairFinancial =
                    CostCalculator.calculateTotalCost(repair, years);

            double replaceFinancial =
                    CostCalculator.calculateTotalCost(
                            replacement, years);

            int repairDisposals =
                    EnvironmentalCalculator.estimateDisposals(
                            false, rl, years);

            int replaceDisposals =
                    EnvironmentalCalculator.estimateDisposals(
                            true, nl, years);

            double repairAdjusted = repairFinancial
                    + EnvironmentalCalculator.calculatePenalty(
                            repairDisposals, p);

            double replaceAdjusted = replaceFinancial
                    + EnvironmentalCalculator.calculatePenalty(
                            replaceDisposals, p);

            
            chart.setValues(
                    repairFinancial,
                    replaceFinancial,
                    repairAdjusted,
                    replaceAdjusted);


            String recommendation;

            if (repairAdjusted < replaceAdjusted) {
                recommendation = "REPAIR";
            } else if (replaceAdjusted < repairAdjusted) {
                recommendation = "REPLACE";
            } else {
                recommendation = "BOTH OPTIONS COST THE SAME";
            }

            String financialRecommendation;

            if (repairFinancial < replaceFinancial) {
                financialRecommendation = "REPAIR";
            } else if (replaceFinancial < repairFinancial) {
                financialRecommendation = "REPLACE";
            } else {
                financialRecommendation = "BOTH COST THE SAME";
            }

            String report = String.format(
                    "COMPARISON PERIOD: %d YEARS%n%n"
                    + "REPAIR STRATEGY%n"
                    + "Financial cost: Rs. %.2f%n"
                    + "Estimated disposals: %d%n"
                    + "Adjusted cost: Rs. %.2f%n%n"
                    + "REPLACEMENT STRATEGY%n"
                    + "Financial cost: Rs. %.2f%n"
                    + "Estimated disposals: %d%n"
                    + "Adjusted cost: Rs. %.2f%n%n"
                    + "RECOMMENDATION (INCLUDING PENALTY): %s%n%n"
                    + "FINANCIAL-ONLY RESULT: %s%n%n"
                    + "Environmental penalty is a user-defined "
                    + "assumption, not a measured impact.%n"
                    + "Estimates depend on the model assumptions.",
                    years,
                    repairFinancial, repairDisposals, repairAdjusted,
                    replaceFinancial, replaceDisposals, replaceAdjusted,
                    recommendation, financialRecommendation);

            results.setText(report);
            results.setCaretPosition(0);

            
            StringBuilder scenarios = new StringBuilder();

            double[] repairLifespans = {
                rl * 1.5,
                rl,
                rl * 0.5
            };

            String[] scenarioNames = {
                "OPTIMISTIC",
                "EXPECTED",
                "PESSIMISTIC"
            };

            scenarios.append("\n\n=== SENSITIVITY ANALYSIS ===\n");

            for (int i = 0; i < repairLifespans.length; i++) {

                Device scenarioRepair = new Device(
                        "Repair",
                        rc,
                        repairLifespans[i],
                        rm,
                        rw
                );

                double scenarioRepairCost =
                        CostCalculator.calculateTotalCost(
                                scenarioRepair, years);

                int scenarioDisposals =
                        EnvironmentalCalculator.estimateDisposals(
                                false, repairLifespans[i], years);

                double scenarioAdjusted =
                        scenarioRepairCost
                        + EnvironmentalCalculator.calculatePenalty(
                                scenarioDisposals, p);

                String scenarioRecommendation;

                if (scenarioAdjusted < replaceAdjusted) {
                    scenarioRecommendation = "REPAIR";
                } else if (scenarioAdjusted > replaceAdjusted) {
                    scenarioRecommendation = "REPLACE";
                } else {
                    scenarioRecommendation = "TIE";
                }

                scenarios.append(String.format(
                        "%s: repair lifespan = %.1f years%n"
                        + "Repair adjusted cost = Rs. %.2f%n"
                        + "Recommendation = %s%n%n",
                        scenarioNames[i],
                        repairLifespans[i],
                        scenarioAdjusted,
                        scenarioRecommendation
                ));
            }

            results.append(scenarios.toString());


        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RepairReplaceGUI app = new RepairReplaceGUI();
            app.setVisible(true);
        });
    }
}
