package org.enableitalia.telemetry;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;

public class SEMGWriter implements PacketProcessor {
	PrintWriter pw;
	int channels;
	
	public SEMGWriter(int channels, String filename) {
		try {
			pw = new PrintWriter(new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.channels  = channels;
		for (int i = 0; i < channels; i++) {
			pw.print(String.format("channel_%d,",i));
		}
		pw.println();
	}


	@Override
	public void process(RTPPacket p) {
		byte[] pd = p.getPayload(); 
		System.out.print("Data: ");
		for (int k = 0; k < p.getPayloadLength(); k++) {
			System.out.print(String.format("%x,", pd[k]));
		}
		System.out.println();
		
		ByteBuffer bb = ByteBuffer.wrap(p.getPayload());

		
		for (int j=0; j < p.getPayloadLength() / 3 / channels; j++) {
			for (int i = 0; i < channels; i++) {
				int data = unpack24(bb);
				String value = String.format("%d,", data);
				pw.print(value);
			}
			pw.println();
		}
	}
	
	int unpack24(ByteBuffer buf) {
		// big endian
		long value = buf.get();
        value = (value << 8) | (0xff & buf.get());
        value = (value << 8) | (0xff & buf.get());

        if (value >= 0x800000) {
        	value |= 0xff000000;
        	value = ~value + 1;
        }
        	
//        double uV = value * 0.286102294921875;
        
//        System.out.println(String.format("Value[%x], int[%d], Pow[%f]",value, (int)value, uV));
        
        return (int)value;
	}
	
	void flush() {
		pw.flush();
	}
}
