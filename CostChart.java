
import javax.swing.*;
import java.awt.*;

public class CostChart extends JPanel {

    private double repairFinancial;
    private double replaceFinancial;
    private double repairAdjusted;
    private double replaceAdjusted;

    public CostChart() {
        setPreferredSize(new Dimension(520, 260));
        setBackground(Color.WHITE);
    }

    public void setValues(
            double repairFinancial,
            double replaceFinancial,
            double repairAdjusted,
            double replaceAdjusted) {

        this.repairFinancial = repairFinancial;
        this.replaceFinancial = replaceFinancial;
        this.repairAdjusted = repairAdjusted;
        this.replaceAdjusted = replaceAdjusted;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("SansSerif", Font.BOLD, 15));
        g2.drawString("Estimated Cost Comparison", 15, 22);

        double[] values = {
            repairFinancial,
            replaceFinancial,
            repairAdjusted,
            replaceAdjusted
        };

        String[] labels = {
            "Repair",
            "Replace",
            "Repair + penalty",
            "Replace + penalty"
        };

        double maxValue = 1;

        for (double value : values) {
            maxValue = Math.max(maxValue, value);
        }

        int chartLeft = 35;
        int chartTop = 45;
        int chartHeight = Math.max(80, getHeight() - 100);
        int chartWidth = Math.max(100, getWidth() - 55);

        g2.setColor(Color.LIGHT_GRAY);
        g2.drawLine(
                chartLeft,
                chartTop + chartHeight,
                chartLeft + chartWidth,
                chartTop + chartHeight);

        int slotWidth = chartWidth / values.length;
        int barWidth = Math.min(48, slotWidth - 12);

        for (int i = 0; i < values.length; i++) {

            int barHeight = (int) (
                    values[i] / maxValue * (chartHeight - 25));

            int x = chartLeft + i * slotWidth
                    + (slotWidth - barWidth) / 2;

            int y = chartTop + chartHeight - barHeight;

            g2.setColor(i % 2 == 0
                    ? new Color(65, 130, 210)
                    : new Color(235, 155, 65));

            g2.fillRoundRect(x, y, barWidth,
                    barHeight, 8, 8);

            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));

            String amount = String.format("%.0f", values[i]);
            FontMetrics fm = g2.getFontMetrics();

            g2.drawString(amount,
                    x + (barWidth - fm.stringWidth(amount)) / 2,
                    Math.max(chartTop + 12, y - 5));

            String label = labels[i];
            int labelX = x + (barWidth
                    - fm.stringWidth(label)) / 2;

            g2.drawString(label, labelX,
                    chartTop + chartHeight + 18);
        }

        g2.dispose();
    }
}
