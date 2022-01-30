package it.enable.telemetry;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Transmitter implements Runnable {
	private DatagramSocket serverSocket;
	
	private SEMGReader reader;
	
	InetAddress remoteIP;
	int			remotePort;
	
	boolean _stop = false;

	public Transmitter(String host, int port) {
		try {
			InetAddress localIP = InetAddress.getByName(host);
			serverSocket = new DatagramSocket(port, localIP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
		
	public void start(String destination, int remote_port, String file) {
		
		reader = new SEMGReader(file);
		try {
			remoteIP = InetAddress.getByName(destination);
			remotePort = remote_port;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_stop = false;
		new Thread(this).start();
	}
	
	public void stop() {
		_stop = true;
	}
	
	public synchronized void waitDone() throws InterruptedException {
		this.wait();
	}
	
	public synchronized void notifyDone() {
		this.notify();
	}

	@Override
	public void run() {
		
		RTPPacket	packet = new RTPPacket();
		byte emg_values[] = new byte[1000];
		int sn = 0;
		
		while ((!_stop)&& (!reader.isEof())) {
			
			int _index = 0;
			
			while ((_index < 1000) && (!reader.isEof())) {
				int value = reader.read();
				emg_values[_index++] = (byte)(value & 0xff);
				emg_values[_index++] = (byte)((value >> 8) & 0xff);	
			}
			
			packet.init(1,sn++,1234,4567,false,emg_values,_index);
				
			DatagramPacket receivePacket = new DatagramPacket(packet.toByte(), packet.getByteLength(), remoteIP, remotePort);
			try {
				System.out.println("sending packet...");
				serverSocket.send(receivePacket);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Done.");
		
		notifyDone();
	}
}
