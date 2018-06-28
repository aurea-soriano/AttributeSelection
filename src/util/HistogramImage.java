package util;

/**
 *
 * @author asoriano
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JPanel;

public class HistogramImage extends JPanel {

    ArrayList<Integer> listValues;
    Integer blocks;
    int w;
    int h;
    Integer minValue;
    Integer maxValue;

    public HistogramImage(ArrayList<Integer> listValuesHistogram, int b, int width, int height) {
        setBackground(Color.WHITE);
        listValues = listValuesHistogram;
        blocks = b;
        w = width;
        h = height;

        if (listValuesHistogram != null) {
            minValue = Collections.min(listValuesHistogram);
            maxValue = Collections.max(listValuesHistogram);
        } else {
            minValue = 0;
            maxValue = 0;
        }
    }

    public void setListValues(ArrayList<Integer> listValues) {
        setBackground(Color.WHITE);
        this.listValues = listValues;
        if (listValues != null) {
            minValue = Collections.min(listValues);
            maxValue = Collections.max(listValues);
        } else {
            minValue = 0;
            maxValue = 0;
        }
    }

    public void setBlocks(Integer blocks) {
        this.blocks = blocks;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }

    public BufferedImage createOffscreenImage() {
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        super.paintComponent((Graphics) g2);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        drawPoints(g2);
        drawAxis(g2);
        g2.dispose();
        return bi;
    }

    private void drawAxis(Graphics2D graphic) {
        graphic.setPaint(Color.BLUE);
        // axe x
        graphic.draw(new Line2D.Double(10, h - 20, w - 20, h - 20));
        graphic.draw(new Line2D.Double(10, 10, w - 20, 10));
        // axe y
        graphic.draw(new Line2D.Double(10, 10, 10, h - 20));
        graphic.draw(new Line2D.Double(w - 20, 10, w - 20, h - 20));

    }

    private void drawPoints(Graphics2D graphic) {
        if (listValues != null) {
            int blockWidth = (int) ((w) / blocks);
            int blockHeight = (h - 30);
            int countWidth = 10;
            graphic.setPaint(Color.BLACK);
            for (int i = 0; i < listValues.size(); i++) {
                int elev = Utils.minmax(maxValue, minValue, blockHeight, 20, listValues.get(i));
                //System.out.println("value "+listValues.get(i), )
                graphic.setPaint(Color.BLACK);
                graphic.fillRect(countWidth, h - 20 - elev, blockWidth, elev);
                graphic.setPaint(Color.RED);
                graphic.draw(new Rectangle(countWidth, h - 20 - elev, blockWidth, elev));
                countWidth += blockWidth;

            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        super.paintComponent(g);
        image = createOffscreenImage();
        g2.drawImage(image, 0, 0, this);

    }

    public BufferedImage createImage() {
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        this.paint(g);
        return bi;
    }
}
