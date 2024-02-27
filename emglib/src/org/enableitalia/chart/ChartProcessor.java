package org.enableitalia.chart;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;

import org.enableitalia.telemetry.PacketProcessor;
import org.enableitalia.telemetry.RTPPacket;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class ChartProcessor implements PacketProcessor, Runnable {
	static TimeSeries ts = new TimeSeries("sEMG Signal", Millisecond.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	//ByteBuffer bb;
	int avail = 0;

	public ChartProcessor() {
		//bb = ByteBuffer.allocate(40000);

		TimeSeriesCollection dataset = new TimeSeriesCollection(ts);
		dataset.addSeries(ts);
		JFreeChart chart = ChartFactory.createTimeSeriesChart("sEMG", "Time", "Value", dataset, true, true, false);
		final XYPlot plot = chart.getXYPlot();
		ValueAxis axis = plot.getDomainAxis();
		// axis.setRange(new Range(0,4096));
		axis.setAutoRange(true);
		axis.setFixedAutoRange(20000.0);

		JFrame frame = new JFrame("sEMG");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ChartPanel label = new ChartPanel(chart);
		frame.getContentPane().add(label);
		// Suppose I add combo boxes and buttons here later

		frame.pack();
		frame.setVisible(true);

		//new Thread(this).start();
	}

	@Override
	public void process(RTPPacket p) {
		
		ByteBuffer bb = ByteBuffer.wrap(p.getPayload());
		
		short data1 = 0;
		short data2 = 0;
		
		for (int i=0; i < 500; i++) {
			data1 = bb.getShort();
			//data2 = bb.getShort();
			ts.addOrUpdate(new Millisecond(), data1 );
		}

/*		Date now = new Date();
		String strDate = sdf.format(now);
		System.out.println(strDate + " queuing 500 samples, free " + bb.position());

		synchronized (this) {
			avail += p.getPayloadLength();
			bb.put(p.getPayload());
			bb.compact();
		}
*/
	}

	@Override
	public void run() {
		int count = 0;
		short data1 = 0;
		short data2 = 0;
		boolean produced;
/*		while (true) {
			produced = false;
			synchronized (this) {
				if (avail > 4) {
					avail -= 4;
					data1 = bb.getShort();
					data2 = bb.getShort();
					produced = true;
				}
			}
			if (produced) {
				if ((count++ % 100) == 0) {
					Date now = new Date();
					String strDate = sdf.format(now);
					System.out.println(strDate + " read 200 samples");
				}
				ts.addOrUpdate(new Millisecond(), (data1 + data2) / 2);
			}

		}
*/
	}

}
