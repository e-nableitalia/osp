package org.enableitalia;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class RTDataViewer extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	int samples;

	private final DynamicTimeSeriesCollection dataset;
	private final JFreeChart chart;

	public RTDataViewer(final String title, int s) {
		super(title);
		samples = s;
	    dataset = new DynamicTimeSeriesCollection(1, s, new Second());
	    dataset.setTimeBase(new Second(0, 0, 0, 1, 1, 2014));
	    dataset.addSeries(new float[1], 0, title);
	    chart = ChartFactory.createTimeSeriesChart(
	        title, "Time", title, dataset, true, true, false);
	    final XYPlot plot = chart.getXYPlot();
	    DateAxis axis = (DateAxis) plot.getDomainAxis();
	    axis.setAutoRange(true);
	    axis.setDateFormatOverride(new SimpleDateFormat("ss.SS"));
	    final ChartPanel chartPanel = new ChartPanel(chart);
	    add(chartPanel);
	}
		
	public void show(String label) {
		
		final JFreeChart chart = ChartFactory.createTimeSeriesChart(
				getTitle(), "hh:mm:ss", label, dataset, true, true, false);
		final XYPlot plot = chart.getXYPlot();
		ValueAxis domain = plot.getDomainAxis();
		domain.setAutoRange(true);
		ValueAxis range = plot.getRangeAxis();
		range.setAutoRange(true); //.setRange(-MINMAX, MINMAX);
		
		this.add(new ChartPanel(chart), BorderLayout.CENTER);
		JPanel btnPanel = new JPanel(new FlowLayout());
		//btnPanel.add(run);
		this.add(btnPanel, BorderLayout.SOUTH);
		
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);		
	}

	void addSerie(String values) {
		dataset.addSeries(new float[samples], 0, values);
	}
	
	void append(float data[]) {
		dataset.advanceTime();
		dataset.appendData(data);
	}

}
