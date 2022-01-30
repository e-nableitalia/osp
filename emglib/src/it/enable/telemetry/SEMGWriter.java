package it.enable.telemetry;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;

public class SEMGWriter implements PacketProcessor {
	PrintWriter pw;
	
	public SEMGWriter(String filename) {
		try {
			pw = new PrintWriter(new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pw.println("value");
	}


	@Override
	public void process(RTPPacket p) {
		ByteBuffer bb = ByteBuffer.wrap(p.getPayload());

		System.out.println("Writing " + p.getPayloadLength()/2 + " samples");
		for (int i = 0; i < p.getPayloadLength(); i = i + 2) {
			short data = bb.getShort();
			String value = String.format("%d", data);
			pw.println(value);
		}
		
		pw.flush();
	}
	
	public void flush() {
		pw.flush();
	}

}
