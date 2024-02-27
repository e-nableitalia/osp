package org.enableitalia;

import java.util.ArrayList;
import java.util.List;

import org.enableitalia.io.CSVArrayReader;

import com.github.psambit9791.jdsp.signal.Smooth;
import com.github.psambit9791.jdsp.transform.Hilbert;

public class EMGSignalProcessor {

	static RealtimePanel panel;

	public static void main(String[] args) throws InterruptedException {
		//CSVReader fileReader = new CSVReader("C:\\work\\priv\\penta\\SEMG\\Cleaned_Signals\\20230817_174932_bicep_RLD_FF_Channel_3.csv");
		CSVArrayReader fileReader = new CSVArrayReader("C:\\work\\priv\\penta\\SEMG\\Cleaned_Signals\\20230817_174932_bicep_RLD_FF.csv");

		List<long[]> values = fileReader.read();


		//    	CSVReader reader30hz = new CSVReader("C:\\Users\\anavatta\\Desktop\\30hz.csv");
		//    	List<Double> sample30hz = reader30hz.read();

		RealtimeChart channelsChart = new RealtimeChart("Channels Raw Signals", 4000);
		RealtimeChart rawChart = new RealtimeChart("Channel #3 Raw Signal", 4000);
		RealtimeChart cleanChart = new RealtimeChart("Normalized Signal (removed offset)", 4000);
		RealtimeChart filterChart = new RealtimeChart("Envelope", 4000);
		RealtimeChart rectifiedChart = new RealtimeChart("Smooth", 4000);
		RealtimeChart frequencyChart = new RealtimeChart("Frequency", 4000);
		
		double samplingFrequency = 2000; // Sampling frequency in Hz
		//        double lowCutoffFrequency = 60; // Low cutoff frequency for high-pass filter in Hz
		//        double highCutoffFrequency = 500; // High cutoff frequency for band-pass filter in Hz
		//        int order = 4;


		double channelsValues[][] = new double[values.size()][8];
		double rawValues[] = new double[values.size()];
		double normalizedValues[] = new double[values.size()];
		double rectifiedValues[] = new double[values.size()];
		double smoothed0Values[] = new double[values.size()];

		double normalized_value = 0.0;
		long last_value = 0;
		long shifted_filter = -10000;

		int smooth_length = 20;
		double[] smooth_memory = new double[smooth_length];
		double smooth_current = 0;
		int smooth_mem_pos = 0;
		for (int i = 0; i < values.size(); i++) {
			long[] channels = values.get(i);
			
			for (int j = 0; j < channels.length; j++)
				channelsValues[i][j] = (channels[j] > 100000 ? 100000 : (channels[j] < -100000) ? -100000 : channels[j]) + 400000 - j*100000;

			long current_value = channels[3];

			long shiftedFCL = shifted_filter + (long)((current_value-last_value)<<8);
			shifted_filter = shiftedFCL - (shiftedFCL>>8);
			normalized_value = (shifted_filter+128)>>8;

			last_value = current_value;

			rawValues[i] = current_value;

			normalizedValues[i] = normalized_value;

			rectifiedValues[i] = normalized_value > 0 ? normalized_value : -normalized_value;
			
			// smooting values
			double normalized_current = (double)rectifiedValues[i]/(double)smooth_length;
			smooth_current = smooth_current + normalized_current - smooth_memory[smooth_mem_pos];
			smoothed0Values[i] = smooth_current;
			smooth_memory[smooth_mem_pos++] = normalized_current;
			if (smooth_mem_pos >= smooth_length)
				smooth_mem_pos = 0;
		};

		//double width = 2;
		//int taps = 5;
		//double[] cutoff = {100.0, 150.0};
		//FIRWin1 fw = new FIRWin1(taps, width, samplingFrequency);
		//double[] outCoeffs = fw.computeCoefficients(cutoff, FIRWin1.FIRfilterType.BANDPASS, true);
		//double[] filteredValues = fw.firfilter(outCoeffs, normalizedValues);
		
		Hilbert hilbert = new Hilbert(normalizedValues);
		hilbert.transform();
		double[] envelopeValues = hilbert.getAmplitudeEnvelope();
		double[] frequencies = hilbert.getInstantaneousFrequency(samplingFrequency);
		double[] phase = hilbert.getInstantaneousPhase();
		
		// smooth using 10ms windows
		Smooth smooth1 = new Smooth(envelopeValues, 20, "rectangular");
		double[] smoothed1Values = smooth1.smoothSignal(); 

		Smooth smooth2 = new Smooth(normalizedValues, 20, "rectangular");
		double[] smoothed2Values = smooth1.smoothSignal(); 


		//        Butterworth flt = new Butterworth(samplingFrequency);
		//        double filteredValues[] = flt.bandPassFilter(rawValues, order, lowCutoffFrequency, highCutoffFrequency); //get the result after filtering
		//        double f1[] = flt.highPassFilter(rawValues, order, lowCutoffFrequency); //get the result after filtering
		//        double f2[] = flt.lowPassFilter(f1, order, highCutoffFrequency); //get the result after filtering
		//        double filteredValue = flt.bandStopFilter(f2, order, lowCutoffFrequency, highCutoffFrequency)

		if (!values.isEmpty()) {
			channelsChart.addSeries("#0");
			channelsChart.addSeries("#1");
			channelsChart.addSeries("#2");
			channelsChart.addSeries("#3");
			channelsChart.addSeries("#4");
			channelsChart.addSeries("#5");
			channelsChart.addSeries("#6");
			channelsChart.addSeries("#7");
			channelsChart.getChart().getStyler().setYAxisMin(-400000.0);
			channelsChart.getChart().getStyler().setYAxisMax(400000.0);
			
			rawChart.addSeries("RAW Signal");
			rawChart.getChart().getStyler().setYAxisMin(-100000.0);
			rawChart.getChart().getStyler().setYAxisMax(100000.0);
			cleanChart.addSeries("0 Offset RAW Sig");
			cleanChart.getChart().getStyler().setYAxisMin(-40000.0);
			cleanChart.getChart().getStyler().setYAxisMax(40000.0);
			filterChart.addSeries("Envelope");
			filterChart.addSeries("Specular Rectified values");
			filterChart.getChart().getStyler().setYAxisMin(-40000.0);
			filterChart.getChart().getStyler().setYAxisMax(40000.0);
			
			rectifiedChart.addSeries("Smooth Envelope");
			rectifiedChart.addSeries("Smooth Specular Rectified");
			rectifiedChart.getChart().getStyler().setYAxisMin(-40000.0);
			rectifiedChart.getChart().getStyler().setYAxisMax(40000.0);

			frequencyChart.addSeries("Frequency");
//			rectifiedChart.getChart().getStyler().setYAxisMin(-100000.0);
//			rectifiedChart.getChart().getStyler().setYAxisMax(100000.0);


			panel = new RealtimePanel(2,2).setTitle("e-Nable Italia EMG Signal Processing, data from recording file: 20230817_174932_bicep_RLD_FF.csv");
			panel.addChart(channelsChart);
			panel.addChart(rawChart);
			panel.addChart(cleanChart);        	
			panel.addChart(filterChart);
			panel.addChart(rectifiedChart);
			panel.addChart(frequencyChart);
			//    		
			panel.displayChartMatrix();

			int BULK_SIZE = 1;
			int OFFSET = 1000;

			double[][] envelopes = new double[values.size()][2];

			for (int i=0; i < values.size(); i++) {
				envelopes[i][0] = envelopeValues[i] + OFFSET;
				envelopes[i][1] = -rectifiedValues[i] - OFFSET;
			}
			
			double[][] smoothed = new double[values.size()][2];

			for (int i=0; i < values.size(); i++) {
				smoothed[i][0] = smoothed1Values[i] + OFFSET;
				smoothed[i][1] = -smoothed0Values[i] - OFFSET;
			}
			
			for (int i=0; i < values.size()/BULK_SIZE ; i++) {

				channelsChart.bulkAppend(i*BULK_SIZE, BULK_SIZE, channelsValues);
				
				//channelsChart.append(channelsValues[i]);

				rawChart.bulkAppend(i*BULK_SIZE, BULK_SIZE,rawValues);
				
				cleanChart.bulkAppend(i*BULK_SIZE, BULK_SIZE,normalizedValues);

				filterChart.bulkAppend(i*BULK_SIZE, BULK_SIZE,envelopes);

				rectifiedChart.bulkAppend(i*BULK_SIZE, BULK_SIZE,smoothed);
				
				frequencyChart.bulkAppend(i*BULK_SIZE, BULK_SIZE,frequencies);
				
				javax.swing.SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {

						panel.repaintCharts();
					}
				});

	
				Thread.sleep(1);
			}

		} else {
			System.out.println("Nessun valore intero nel file.");
		}
	}

}
