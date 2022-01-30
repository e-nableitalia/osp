package it.enable;

public class DataBuffer {
	int size;
	int mask;
	int writePos;
	int readPos;
	float total;
	float mean;
	float max;
	float min;
	
	int data[];
	
	public DataBuffer(int sz, int _default) {
		while (!isPowerOfTwo(sz))
		{
			sz++;
		}
		size = sz;
		mask = sz - 1;
		writePos = readPos = 0;
		total = mean = 0;
		max = 0;
		min = Float.MIN_VALUE;
		data = new int[size];
		
		for (int i = 0; i < size; i++) {
			data[i] = _default;
		}
	}
	
	private boolean isPowerOfTwo(int sz) {
		// TODO Auto-generated method stub
		return (sz & (sz - 1)) == 0;
	}

	int read() {
		return data[readPos++ & mask];
	}
	
	boolean write(int value)				
	{
		if (data == null)
			return false;

		min = Math.min(min, value);
		max = Math.max(max, value);

		// remove first value from total, then add new value and calculate mean
		total -= data[writePos & mask];		// remove last value from total
		total += value;						// add new value to total

		// calculate new mean
		if (total == 0)
		{
			mean = 0;
		}
		else
		{
			mean = total / size;
		}

		data[writePos++ & mask] = value;				// add new value to buffer	

		// return true to indicate successful write
		return true;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		this.mask = mask;
	}

	public int getWritePos() {
		return writePos;
	}

	public void setWritePos(int writePos) {
		this.writePos = writePos;
	}

	public int getReadPos() {
		return readPos;
	}

	public void setReadPos(int readPos) {
		this.readPos = readPos;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public float getMean() {
		return mean;
	}

	public void setMean(float mean) {
		this.mean = mean;
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}

	public float getMin() {
		return min;
	}

	public void setMin(float min) {
		this.min = min;
	}
	
}
