package it.enable.chart;

import java.awt.Color;

import javax.swing.JFrame;

import org.thingml.rtcharts.swing.GraphBuffer;
import org.thingml.rtcharts.swing.GraphPanel;
import org.thingml.rtcharts.swing.LineGraphPanel;

public class RTChartViewer extends JFrame {
	private static final long serialVersionUID = 1L;

	GraphBuffer graph_buffer = new GraphBuffer(20000);
    GraphPanel graph_panel;

	int avail = 0;

	public RTChartViewer(String name, int max, int min) {

		graph_panel =  new LineGraphPanel(graph_buffer, name, min, max, 500, Color.RED);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(graph_panel);

        this.setSize(1800, 300);

        this.setVisible(true);
        graph_panel.start();
	}

	public void push(int i) {
		graph_buffer.insertData(i);
	}
}
