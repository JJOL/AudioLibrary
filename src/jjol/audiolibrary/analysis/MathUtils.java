package jjol.audiolibrary.analysis;

public class MathUtils {

	
	public static float map(float start, float end, float vstart, float vend, float val) {
		float ret = (val/vend)*end; 
		ret = (ret > end) ? end : (ret < start) ? start : ret;
		return ret;
	}
}
