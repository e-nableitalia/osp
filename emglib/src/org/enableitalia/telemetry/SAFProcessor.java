package org.enableitalia.telemetry;

public class SAFProcessor extends SEMGWriter implements PacketProcessor {

	PacketProcessor proc;
	
	public SAFProcessor(int channels, String filename, PacketProcessor pp) {
		super(channels, filename);
		proc = pp;
	}

	@Override
	public void process(RTPPacket p) {
		super.process(p);
		proc.process(p);
	}

	public void flush() {
		super.flush();
	}
}
