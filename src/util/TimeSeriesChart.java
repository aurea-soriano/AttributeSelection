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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import static util.TimeUtils.*;

/**
 *
 * @author aurea
 */
public class TimeSeriesChart {

    List<double[]> listValues;
    Integer numberValues;
    double[] weekNumber;
    double[] timeOfWeek;
    List<String> listAttributes;

    public TimeSeriesChart(List<double[]> lv, List<String> la, double[] wn, double[] tow) {

        listValues = lv;
        if (lv != null) {
            numberValues = lv.size();
        } else {
            numberValues = 0;
        }
        listAttributes = la;
        weekNumber = wn;
        timeOfWeek = tow;
    }

    private JFreeChart createChart(XYDataset dataset) throws IOException {

        JFreeChart chart = ChartFactory.createTimeSeriesChart("", "", "", dataset, true, false, false);
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.setForegroundAlpha(1.0f);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(false);
            renderer.setBaseShapesFilled(false);
            renderer.setDrawSeriesLineAsPath(true);
        }

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setTickLabelsVisible(false);
        axis.setDateFormatOverride(new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss"));
        return chart;
    }

    private XYDataset createDataset() {

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        for (int n = 0; n < numberValues; n++) {
            TimeSeries timeSeriesValue = new TimeSeries(listAttributes.get(n));
            double[] values = listValues.get(n);
            if (weekNumber == null || timeOfWeek == null) {
                int month = 1;
                int year = 1900;

                for (int i = 0; i < values.length; i++) {

                    timeSeriesValue.add(new Month(month, year), values[i]);
                    if (month == 12) {
                        month = 1;
                        year++;
                    } else {
                        month++;
                    }
                }
            } else {
                for (int i = 0; i < values.length; i++) {
                    Date date = getDateFromGpsTime(weekNumber[i], timeOfWeek[i]);
                    try {

                        timeSeriesValue.add(new Millisecond(date), values[i]);
                    } catch (Exception e) {
                        System.out.println(date);
                    }
                }
            }

            dataset.addSeries(timeSeriesValue);
        }
        return dataset;
    }

    public JPanel createPanel() throws IOException {
        if (listValues != null) {
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
        JFreeChart chart = createChart(createDataset());
        BufferedImage img = draw(chart, width, height);
        return img;
    }
}
