package it.enable.telemetry;

public class RTPPacket {
	
	private final static int HEADER_FIXED_LEN = 12;
	
	public RTPPacket() { };
	
	public RTPPacket(int _pt, int _sn, int _ts, int _ssrc, boolean _marker, byte data[], int len) {
		
		marker = _marker;
		pt = _pt;
		sn = _sn;
		ts = _ts;
		ssrc = _ssrc;
		buffer = new byte[len];
		for (int i = 0; i < len; i++)
			buffer[i] = data[i];
	}
	
	public RTPPacket(byte data[], int len) {
		marker = ((data[1] & 0x80) != 0);
		pt = (data[1] & 0x7f);
		sn = data[2] + (int)data[3] << 8;
		ts = data[4] + data[5] << 8 + data[6] << 16 + data[7] << 24;
		ssrc = data[8] + data[9] << 8 + data[10] << 16 + data[11] << 24;
		buffer = new byte[len-HEADER_FIXED_LEN];
		for (int i = 0; i < (len - HEADER_FIXED_LEN); i++)
			buffer[i] = data[i+HEADER_FIXED_LEN];
	}
	
	public void init(int _pt, int _sn, int _ts, int _ssrc, boolean _marker, byte data[], int len) {
		marker = _marker;
		pt = _pt;
		sn = _sn;
		ts = _ts;
		ssrc = _ssrc;
		buffer = new byte[len];
		for (int i = 0; i < len; i++)
			buffer[i] = data[i];
	}
	
	public boolean getM() {
		return marker;
	}
	
	public int getPT() {
		return pt;
	}
	
	public void setPT(int _pt) {
		pt = _pt;
	}
	
	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public int getTs() {
		return ts;
	}

	public void setTs(int ts) {
		this.ts = ts;
	}

	public int getSsrc() {
		return ssrc;
	}

	public void setSsrc(int ssrc) {
		this.ssrc = ssrc;
	}

	public byte[] getPayload() {
		return buffer;
	}
	
	public int getPayloadLength() {
		return buffer.length;
	}
	
	public byte[] toByte() {
		if (bytebuffer == null) {
			// allocate
			bytebuffer = new byte[buffer.length + HEADER_FIXED_LEN];
		} else {
			// check size
			if (bytebuffer.length != (buffer.length + HEADER_FIXED_LEN))
				// reallocate
				bytebuffer = new byte[buffer.length + HEADER_FIXED_LEN];
		}
		// serialize
		bytebuffer[0] = (byte)0x80; // V=2
		bytebuffer[1] = (marker ? (byte)0x80 : (byte)0x0);
		bytebuffer[1] += (byte)(pt & 0x7f);
		bytebuffer[3] = (byte)( sn & 0xff);
		bytebuffer[2] = (byte)( (sn >> 8) & 0xff);
		bytebuffer[7] = (byte)( ts & 0xff);
		bytebuffer[6] = (byte)( (ts >> 8) & 0xff);
		bytebuffer[5] = (byte)( (ts >> 16) & 0xff);
		bytebuffer[4] = (byte)( (ts >> 24) & 0xff);
		bytebuffer[11] = (byte)( ssrc & 0xff);
		bytebuffer[10] = (byte)( (ssrc >> 8) & 0xff);
		bytebuffer[9] = (byte)( (ssrc >> 16) & 0xff);
		bytebuffer[8] = (byte)( (ssrc >> 24) & 0xff);
		for (int i = 0; i < buffer.length; i++)
			bytebuffer[HEADER_FIXED_LEN + i] = buffer[i];
		
		return bytebuffer;
	}
	
	public int getByteLength() {
		return bytebuffer.length;
	}
	
	byte buffer[];
	boolean marker;
	int pt;
	int sn;	
	int ts;
	int ssrc;
	
	byte bytebuffer[];
	int	 bytebyffer_len;
}
