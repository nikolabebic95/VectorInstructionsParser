package rs.ac.bg.etf.par.vectorinstructionparser.helpers;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.StyledDocument;
import java.awt.*;

/**
 * Class providing the text pane with no word wrapping
 */
public class NonWrappingTextPane extends JTextPane {
    public NonWrappingTextPane(StyledDocument doc) {
        super(doc);
    }

    // Override getScrollableTracksViewportWidth
    // to preserve the full width of the text
    public boolean getScrollableTracksViewportWidth() {
        Component parent = getParent();
        ComponentUI ui = getUI();

        return parent == null || (ui.getPreferredSize(this).width <= parent.getSize().width);
    }

    public void setBounds(int x, int y,int width, int height) {
        Dimension size = this.getPreferredSize();
        super.setBounds(x, y, Math.max(size.width, width), height
        );
    }
}
