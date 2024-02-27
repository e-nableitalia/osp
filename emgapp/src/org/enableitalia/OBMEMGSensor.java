package org.enableitalia;

public class OBMEMGSensor {
	int samplingWindow;
	int sample;

	int overRatio;
	int divider;
	
	int min;
	int max;
	int threshold;
	int currentValue;
	int overSampledValue;
	
	public OBMEMGSensor(int overRatio) {
		this.overRatio = overRatio;
		samplingWindow = (int) Math.pow(4,overRatio);
		divider = (int) Math.pow(2,overRatio);
//		buffer = new int[samplingWindow];
//		for (int i = 0; i < samplingWindow; i++)
//			buffer[i] = 0;
		overSampledValue = sample = currentValue = 0;
		System.out.println("OBMEMGSensor created, oversample window = " + samplingWindow);
	}
	
	public void push(int value) {
//		System.out.println("Storing[" + value + "] @ sample[" + sample + "]");
//		buffer[sample++] = value;
		
		//System.out.println("Processing[" + value + "], current value[" + currentValue+ "], sample[" + sample + "]");
		currentValue += value;
		sample++;
		
		if (sample == samplingWindow) {
			evaluate();
		}
	}

	private void evaluate() {
		overSampledValue = currentValue / divider;
		currentValue = 0;
		// reset window
		sample = 0;
		
		//System.out.println("Oversampled Value[" + overSampledValue + "]");
	}
	
	public int getValue() {
		return overSampledValue;
	}

	public void clear() {
		sample = 0;
		currentValue = 0;
		overSampledValue = 0;
	}
}
