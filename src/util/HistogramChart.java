/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

/**
 *
 * @author aurea
 */
public class HistogramChart {

    double[] values;
    int block;

    public HistogramChart(double[] v, int b) {
        values = v;
        block = b;
    }

    private JFreeChart createChart(HistogramDataset dataset) throws IOException {

        JFreeChart chart = ChartFactory.createHistogram(
                "",
                null,
                null,
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );
        chart.setAntiAlias(true);
        chart.setBorderVisible(true);
        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainCrosshairVisible(false);
        plot.setRangeCrosshairVisible(false);
        plot.setDomainGridlinePaint(Color.blue);
        plot.setOutlinePaint(Color.blue);
        plot.setRangeGridlinePaint(Color.blue);
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        plot.setForegroundAlpha(1.0f);

        return chart;
    }

    private HistogramDataset createDataset() {
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);
        dataset.addSeries("", values, block);

        return dataset;
    }

    public JPanel createPanel() throws IOException {
        if (values != null) {
            JFreeChart chart = createChart(createDataset());
            return new ChartPanel(chart);
        } else {
            return null;
        }
    }

    protected BufferedImage draw(JFreeChart chart, int width, int height) {
        BufferedImage img
                = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        chart.draw(g2, new Rectangle2D.Double(0, 0, width, height));
        g2.dispose();
        return img;
    }

    public BufferedImage createBufferedImage(int width, int height) throws IOException {
        if (values != null) {
            JFreeChart chart = createChart(createDataset());
            BufferedImage img = draw(chart, width, height);
            return img;
        } else {
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            return img;
        }
    }

}
