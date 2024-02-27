package org.enableitalia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class RealtimeChart {
	String title;
	int width;

	private XYChart xyChart;
	private RealtimePanel wrapper;
	private int position;

	Map<String, List<Double>> series = new HashMap<String, List<Double>>();

	public RealtimeChart(String t, int w) {
		title = t;
		width = w;

		// Create Chart
		xyChart = new XYChartBuilder().width(500).height(400).theme(ChartTheme.GGPlot2).title(title).build();	
	}
	
	public void setWrapper(RealtimePanel w, int p) {
		wrapper = w;
		position = p;
	}

	XYChart getChart() {
		return xyChart;
	}

	public void addSeries(String name) {
		List<Double> sData = new ArrayList<Double>();
		for(int i=0; i < width; i++)
			sData.add((double)0);

		series.put(name, sData);

		XYSeries s = xyChart.addSeries(name, null, sData);
		s.setMarker(SeriesMarkers.NONE);
	}
	
	public void append(double value) {
		Iterator<List<Double>> iterator = series.values().iterator();
		if (iterator.hasNext()) {
			List<Double> i = iterator.next();
			i.add(value);
			if (i.size() > width)
				i.remove(0);
		};

		series.forEach( (s,i) -> {
			xyChart.updateXYSeries(s, null, i, null);
		});

		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				wrapper.repaintChart(position);
			}
		});
		
	}
	
	public void append(double value[]) {

		int j = 0;
		Iterator<List<Double>> iterator = series.values().iterator();
		while (iterator.hasNext()) {
			List<Double> i = iterator.next();
			i.add(value[j++]);
			if (i.size() > width)
				i.remove(0);
		};

		series.forEach( (s,i) -> {
			xyChart.updateXYSeries(s, null, i, null);
		});

		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				wrapper.repaintChart(position);
			}
		});
	}

	public void bulkAppend(int offset, int chunk, double[][] buffer) {
		Iterator<List<Double>> iterator = series.values().iterator();
		
		int k = 0;
		
		while (iterator.hasNext()) {
			List<Double> i = iterator.next();
					
			for (int j = 0; j < chunk; j++) {
				// get values
				i.add(buffer[offset + j][k]);
				if (i.size() > width)
					i.remove(0);
			}
			k++;
		}
		
		series.forEach( (s,i) -> {
			xyChart.updateXYSeries(s, null, i, null);
		});
	}
	
	public void bulkAppend(int offset, int chunk, double[] buffer) {
		Iterator<List<Double>> iterator = series.values().iterator();
		
		if (iterator.hasNext()) {
			List<Double> i = iterator.next();
					
			for (int j = 0; j < chunk; j++) {
				// get values
				i.add(buffer[offset + j]);
				if (i.size() > width)
					i.remove(0);
			}
		}

		series.forEach( (s,i) -> {
			xyChart.updateXYSeries(s, null, i, null);
		});
	}
}
