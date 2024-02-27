package org.enableitalia.telemetry;

import java.nio.ByteBuffer;

import org.thingml.rtcharts.swing.GraphBuffer;

public class EMGUnpacker implements PacketProcessor {
	GraphBuffer buffer;
	int row[];
	
	public EMGUnpacker(GraphBuffer buffer) {
		this.buffer = buffer;
		row = new int[8];
	}

	@Override
	public void process(RTPPacket p) {
		ByteBuffer bb = ByteBuffer.wrap(p.getPayload());
		
		for (int j=0; j < p.getPayloadLength() / 3 / 8; j++) {
			for (int i = 0; i < 8; i++) {
				row[i] = unpack24(bb);
			}
			buffer.insertData(row);

	        System.out.println(String.format("int[%d]",row[3]));

			
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
        	
        double uV = value * 0.286102294921875;
                
        return (int)value;
	}
	
	

}
