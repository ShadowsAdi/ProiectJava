package simulare_liga.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ModernTable {
    private static final Color HEADER_BG = new Color(33, 41, 51);
    private static final Color HEADER_FG = new Color(255, 255, 255);
    private static final Color ROW_BG = new Color(245, 247, 250);
    private static final Color ALT_ROW_BG = new Color(255, 255, 255);
    private static final Color SELECTION_BG = new Color(51, 153, 255);

    public static void setupModernStyle(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(HEADER_BG);
        header.setForeground(HEADER_FG);
        header.setBorder(BorderFactory.createEmptyBorder());

        // Row styling
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(SELECTION_BG);
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setReorderingAllowed(false);

        // Alternate row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? ROW_BG : ALT_ROW_BG);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
    }
}