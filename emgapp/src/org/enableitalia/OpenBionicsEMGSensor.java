package org.enableitalia;

public class OpenBionicsEMGSensor {

	// config
	final static int dampening = 3;		// used to reduce the noise floor creeping upwards too quickly when the muscle is held tensed
	int	peakThresh;	
	int holdTime;
	
	int internalTimer;

	
	final static int NOISE_BUFFER_SIZE = 128;
	final static int NOISE_BUFFER_DEFAULT = 925;
	
	DataBuffer noise;
	
	boolean active;
	int sample;
	int signal;
	int prevSignal;

	boolean peak;
	boolean hold;
	
	boolean timer_started;
	long	timer_startTime;

	public OpenBionicsEMGSensor() {
		System.out.println("OpenBionicsEMGSensor created");
		
		peakThresh = 0;
		
		noise = new DataBuffer(NOISE_BUFFER_SIZE, NOISE_BUFFER_DEFAULT);
		
		peak = hold = timer_started = active = false;
		
		signal = prevSignal = sample = 0;
		timer_startTime = 0;
		
		holdTime = 300; 	// 300 ms
		peakThresh = 2400;	// 600/1023
		internalTimer = 0;
	}
	
	void startTimer() {
		timer_startTime = internalTimer;
	}
	
	boolean isTimerStarted() {
		return (timer_startTime != 0);
	}
	
	void stopTimer( ) {
		timer_startTime = 0;
	}
	
	boolean isPeakStarted(int current, int prev) {
		return ((prev < peakThresh) && (current >= peakThresh));
	}
	
	boolean isPeakEnded(int current, int prev) {
		return ((prev >= peakThresh) && (current < peakThresh));
	}
	
	boolean elapsed(long value) {
		// every tick is 0.5 ms
		if ((timer_startTime > 0) && ((internalTimer - timer_startTime) > (value * 2)))
			return true;
		else
			return false;
	}
	
	public void push(int value) {
		internalTimer++;
		calcNoiseFloor(value);
		prevSignal = signal;
		signal = value - (int)noise.getMean();
		
		//System.out.println("Signal = " + value + ", normalized signal = " + signal);
		
		if (signal < 0)
			signal = 0;
		
		if (signal > 0)
			active = true;
		else
			active = false;
	}

	public void process() {
		if (!active) {
			peak = false;
			hold = false;
			stopTimer();
		}
		
		if (isTimerStarted()) {
			if (elapsed(holdTime)) {
				if (hold) {
					System.out.println("Peak");
					peak = true;
				} else {
					System.out.println("Peak down");
					peak = false;
				}
				stopTimer();
			}
		}
		
		if (isPeakStarted(signal, prevSignal)) {
			if (isTimerStarted()) {
				stopTimer();
			} else {
				startTimer();
				hold = true;
				System.out.println("Threshold cross up @" + internalTimer / 2);
			}
			// #2 channels 
			//peak = true;
		} else if (isPeakEnded(signal,prevSignal)) {
			if (isTimerStarted()) {
				stopTimer();
			} else {
				startTimer();
				hold = false;
				System.out.println("Threshold cross down @" + internalTimer / 2);
			}
		}
	}
	
	public int getValue() {
		return (peak ? 3000 : 1000);
	}

	public void clear() {
	}
	
	void calcNoiseFloor(int  muscleVal)
	{		
		// if muscle value is less than the noise floor + sensitivity offset / dampening
		// (this reduces upwards creep of the noise floor when the muscle is held tensed)
		if (muscleVal < (noise.getMean() + (peakThresh / dampening)))
		{
			noise.write(muscleVal);		// add to noise floor buffer
		}
	}
}
