package org.enableitalia;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.enableitalia.chart.RTChartProcessor;
import org.enableitalia.telemetry.DebugProcessor;
import org.enableitalia.telemetry.Receiver;
import org.enableitalia.telemetry.SAFProcessor;

public class SEMGReceiver {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	
	public static void main(String args[]) throws Exception
    {
		Receiver r = new Receiver("192.168.1.16",32000);
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		String fileName = "C:\\work\\priv\\penta\\SEMG\\" + sdf.format(timestamp) + "_RLD_Measure_bicep.csv";
		
		System.out.println("Dumping 180 seconds of data in file: " + fileName);
		
		DebugProcessor dp = new DebugProcessor();
		r.addProcessor(dp, 0);
		
		//SEMGWriter sew = new SEMGWriter(args[0]);
		//ChartProcessor sew = new ChartProcessor();
		RTChartProcessor sew = new RTChartProcessor(8);
		SAFProcessor saf = new SAFProcessor(8, fileName,sew);
		r.addProcessor(saf, 96);
		
		r.start();

		int loops = 0;
		while(loops < 180)
        {
			loops++;
			Thread.sleep(1000);
			saf.flush();
        }

		System.out.println("Done");
    }
}
