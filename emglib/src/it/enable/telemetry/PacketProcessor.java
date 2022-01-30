package it.enable.telemetry;

public interface PacketProcessor {
	void process(RTPPacket p);
}
