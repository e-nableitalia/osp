package org.enableitalia;

import org.enableitalia.telemetry.Transmitter;

public class SEMGTrasmitter {

public static void main(String args[]) throws Exception
{
	Transmitter r = new Transmitter("127.0.0.1",33000);
	
	String fileName = "C:\\Dati\\dump_emg\\20190621_015218_dump.csv";
	
	r.start("127.0.0.1",32000,fileName);

	r.waitDone();
	
	System.out.println("Done");
}
}
