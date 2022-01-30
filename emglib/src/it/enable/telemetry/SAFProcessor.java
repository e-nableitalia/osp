package it.enable.telemetry;

public class SAFProcessor extends SEMGWriter implements PacketProcessor {

	PacketProcessor proc;
	
	public SAFProcessor(String filename, PacketProcessor pp) {
		super(filename);
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
