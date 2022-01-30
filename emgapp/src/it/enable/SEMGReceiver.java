package it.enable;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import it.enable.chart.RTChartProcessor;
import it.enable.telemetry.DebugProcessor;
import it.enable.telemetry.Receiver;
import it.enable.telemetry.SAFProcessor;

public class SEMGReceiver {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	
	public static void main(String args[]) throws Exception
    {
		Receiver r = new Receiver("192.168.1.8",32000);
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		String fileName = "C:\\work\\penta\\SEMG\\" + sdf.format(timestamp) + "_myo_dump.csv";
		
		System.out.println("Dumping 180 seconds of data in file: " + fileName);
		
		DebugProcessor dp = new DebugProcessor();
		r.addProcessor(dp, 0);
		
		//SEMGWriter sew = new SEMGWriter(args[0]);
		//ChartProcessor sew = new ChartProcessor();
		RTChartProcessor sew = new RTChartProcessor();
		SAFProcessor saf = new SAFProcessor(fileName,sew);
		r.addProcessor(saf, 1);
		
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
