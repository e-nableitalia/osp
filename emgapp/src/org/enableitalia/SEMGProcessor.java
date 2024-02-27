package org.enableitalia;

import org.enableitalia.chart.RTChartViewer;
import org.enableitalia.telemetry.SEMGReader;

public class SEMGProcessor {

	public void processFile(String file) {
		System.out.println("Opening file[" + file + "] for processing");
		SEMGReader reader = new SEMGReader(file);

		int value = 0;

		RTChartViewer raw = new RTChartViewer("raw", 4096, 0);
		RTChartViewer over = new RTChartViewer("filtered", 4096, 0);

		OpenBionicsEMGSensor sensor = new OpenBionicsEMGSensor();

		sensor.clear();
		do {
			value = reader.read();
			sensor.push(value);

			sensor.process();
			raw.push(value);
			over.push((int)sensor.getValue());
		} while (!reader.isEof());
	}

	public static void main(String args[]) throws Exception
	{
		int RATIO = 2;

		String file_obm = "\\emgdump\\20190621_015218_dump_OBM.csv";
		String file_plux = "\\emgdump\\20190621_021059_dump_Plux.csv";
		String file_myo = "\\emgdump\\20200223_021758_myo_dump.csv";

		String currentPath = new java.io.File(".").getCanonicalPath();
		System.out.println("Current dir:" + currentPath);
		
		String file = null;
		if (args.length > 0) {
			file = args[0];
		} else {
			file = currentPath + file_myo;
		}

		SEMGProcessor processor = new SEMGProcessor();

		if (file != null) {
			System.out.println("Opening file[" + file + "] for processing");
			processor.processFile(file);
		} else {
			//OBMEMGSensor sensor = new OBMEMGSensor(RATIO);
			//		OpenBionicsEMGSensor sensor = new OpenBionicsEMGSensor();	

			int value = 0;
			//		int max = 0;
			//		for (int i = 0; i < 4000; i++) {
			//			value = reader.read();
			//			sensor.push(value);
			//			if (sensor.getValue() > max)
			//				max = sensor.getValue();
			//		}
			//		System.out.println("Max = " + (max - 1023 * Math.pow(2, RATIO)/2));
			//
			//		int min = (int) (1023 * Math.pow(2, RATIO));
			//		sensor.clear();
			//		
			//		for (int i = 0; i < 4000; i++) {
			//			value = reader.read();
			//			sensor.push(value);
			//			if (sensor.getValue() < min)
			//				min = sensor.getValue();
			//		}
			//		System.out.println("Min = " + (-min/2));

			//		RTChartViewer raw = new RTChartViewer("raw", 4096, 0);
			//		RTChartViewer over = new RTChartViewer("filtered", 4096, 0);
			//		
			//		//Filter filter = new Filter(800,2000,Filter.PassType.Lowpass,1);
			//		OpenBionicsEMGSensor sensor = new OpenBionicsEMGSensor();
			//		
			//		sensor.clear();
			//		do {
			//			value = reader.read();
			//			sensor.push(value);
			//			
			//			sensor.process();
			//			//sensor.push((int)filter.getValue());
			//			raw.push(value);
			//			over.push((int)sensor.getValue());
			//		} while (!reader.isEof());
		}
	}
}
