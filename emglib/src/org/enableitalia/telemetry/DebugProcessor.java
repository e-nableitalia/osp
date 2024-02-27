package org.enableitalia.telemetry;

public class DebugProcessor implements PacketProcessor {

	@Override
	public void process(RTPPacket p) {
		System.out.println(p.getPayload());
	}
}
