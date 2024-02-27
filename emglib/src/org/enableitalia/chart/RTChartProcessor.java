package org.enableitalia.chart;

import java.awt.Color;

import javax.swing.JFrame;

import org.enableitalia.telemetry.EMGUnpacker;
import org.enableitalia.telemetry.PacketProcessor;
import org.enableitalia.telemetry.RTPPacket;
import org.thingml.rtcharts.swing.GraphBuffer;
import org.thingml.rtcharts.swing.GraphPanel;
import org.thingml.rtcharts.swing.LineGraphPanel;

public class RTChartProcessor extends JFrame implements PacketProcessor {

	private static final long serialVersionUID = 1L;
	
	GraphBuffer graph_buffer;
    GraphPanel graph_panel;
    EMGUnpacker unpacker;
    
	int avail = 0;
	
	int channels = 0;

	public RTChartProcessor(int channels) {

		this.channels = channels;
		
		graph_buffer = new GraphBuffer(8,20000);
		graph_buffer.setColumn(3);
		unpacker = new EMGUnpacker(graph_buffer);
		//graph_panel =  new LineGraphPanel(graph_buffer, "sEMG", -100000, 50000, 500, Color.RED);
		graph_panel =  new LineGraphPanel(graph_buffer, "sEMG", -120000, 80000, 50000, Color.RED);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(graph_panel);

        this.setSize(1800, 900);

        this.setVisible(true);
        graph_panel.start();
	}

	@Override
	public void process(RTPPacket p) {
		
		unpacker.process(p);
		
//		ByteBuffer bb = ByteBuffer.wrap(p.getPayload());
//		
//		short data1 = 0;
//
//		for (int i=0; i < 500; i++) {
//			data1 = bb.getShort();
//			graph_buffer.insertData(data1);
//		}
	}
}
