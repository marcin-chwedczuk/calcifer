package pl.marcinchwedczuk.calcifer.gui;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Map;

public class TrayIconPainter {
    static BufferedImage textToImage(String text) {
        // hardcoded, it's hard to get the real font used.
        var defaultFont = javafx.scene.text.Font.getDefault();
        var awtFont = java.awt.Font.decode(String.format("%s-PLAIN-%s",
                defaultFont.getName(), "24")); // too big font to scale image up
        return textToImage(text, awtFont, awtFont.getSize());
    }

    static BufferedImage textToImage(String Text, Font font, float size) {
        font = font.deriveFont(size);

        FontRenderContext frc = new FontRenderContext(null, false, true);

        LineMetrics lm = font.getLineMetrics(Text, frc);
        Rectangle2D r2d = font.getStringBounds(Text, frc);

        int offset = 5;
        BufferedImage img = new BufferedImage(
                (int) Math.ceil(r2d.getWidth()) + 2*offset,
                (int) Math.ceil(r2d.getHeight()) + 2*offset,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        Map<?, ?> desktopHints =
                (Map<?, ?>) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
        if (desktopHints != null) {
            g2d.setRenderingHints(desktopHints);
        }
        g2d.setBackground(new Color(0, 0, 0, 0));
        g2d.setColor(Color.BLACK);

        g2d.clearRect(0, 0, img.getWidth(), img.getHeight());
        g2d.setFont(font);
        g2d.drawString(Text, offset, offset + lm.getAscent());
        g2d.dispose();

        return img;
    }
}
