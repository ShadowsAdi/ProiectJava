package simulare_liga.ui;

import javax.swing.*;
import java.awt.*;

public class ModernScrollPane {
    public static void setupModernStyle(JScrollPane pane) {
        pane.setBackground(Color.WHITE);

        JScrollBar verticalBar = pane.getVerticalScrollBar();
        verticalBar.setUI(new ModernScrollBarUI());
        verticalBar.setPreferredSize(new Dimension(8, 0));

        JScrollBar horizontalBar = pane.getHorizontalScrollBar();
        horizontalBar.setUI(new ModernScrollBarUI());
        horizontalBar.setPreferredSize(new Dimension(0, 8));
    }
}