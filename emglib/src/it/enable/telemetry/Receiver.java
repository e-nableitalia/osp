package it.enable.telemetry;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Receiver implements Runnable {
	private final static int MAX_PROCESSORS = 5;
	
	private DatagramSocket serverSocket;
	byte[] receiveData;
	PacketProcessor processors[];

	boolean _stop = false;

	public Receiver(String host, int port) {
		try {
			InetAddress localIP = InetAddress.getByName(host);
			serverSocket = new DatagramSocket(port, localIP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		receiveData = new byte[1500];
		
		processors = new PacketProcessor[MAX_PROCESSORS];
	}
	
	public void addProcessor(PacketProcessor p, int pt) {
		if (pt < MAX_PROCESSORS) {
			processors[pt] = p;
		}
	}
	
	public void start() {
		_stop = false;
		new Thread(this).start();
	}
	
	public void stop() {
		_stop = true;
	}

	@Override
	public void run() {
		while (!_stop) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				serverSocket.receive(receivePacket);
				RTPPacket p = new RTPPacket(receiveData, receivePacket.getLength());

				if (processors[p.getPT()] != null)
					processors[p.getPT()].process(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
