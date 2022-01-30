package it.enable.chart;

import java.awt.Color;
import java.nio.ByteBuffer;

import javax.swing.JFrame;

import org.thingml.rtcharts.swing.GraphBuffer;
import org.thingml.rtcharts.swing.GraphPanel;
import org.thingml.rtcharts.swing.LineGraphPanel;

import it.enable.telemetry.PacketProcessor;
import it.enable.telemetry.RTPPacket;

public class RTChartProcessor extends JFrame implements PacketProcessor {

	private static final long serialVersionUID = 1L;
	
	GraphBuffer graph_buffer = new GraphBuffer(20000);
    GraphPanel graph_panel =  new LineGraphPanel(graph_buffer, "sEMG", 0, 4096, 500, Color.RED);

	int avail = 0;

	public RTChartProcessor() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(graph_panel);

        this.setSize(1800, 900);

        this.setVisible(true);
        graph_panel.start();
	}

	@Override
	public void process(RTPPacket p) {
		
		ByteBuffer bb = ByteBuffer.wrap(p.getPayload());
		
		short data1 = 0;

		for (int i=0; i < 500; i++) {
			data1 = bb.getShort();
			graph_buffer.insertData(data1);
		}
	}
}
