package edu;

import java.util.Comparator;

class PickComparator implements Comparator<Pick>{

	@Override
	public int compare(Pick o1, Pick o2) {
		return Float.compare(o1.getGain(), o2.getGain());
	}
	
}

public class Pick {
	private float gain;
	private int phase;
	
	public Pick(float f, int p) {
		gain = f;
		phase = p;
	}

	public float getGain() {
		return gain;
	}

	public void setGain(float Pick) {
		this.gain = Pick;
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}
}

