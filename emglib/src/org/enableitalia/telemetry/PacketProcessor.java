package org.enableitalia.telemetry;

public interface PacketProcessor {
	void process(RTPPacket p);
}
