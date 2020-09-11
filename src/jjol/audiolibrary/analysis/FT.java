package jjol.audiolibrary.analysis;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class FT {
	
	static class Comp {
		double re, im;
		
		public Comp(double re) {
			this.re = re;
		}
		
		public Comp(double re, double im) {
			this.re = re;
			this.im = im;
		}
		
		public static Comp unitFromArg(double th) {
			return new Comp(Math.cos(th), Math.sin(th));
		}
		
		public void add(Comp other) {
			this.re += other.re;
			this.im += other.im;
		}
		
		public void mult(Comp other) {
			this.re = this.re*other.re - this.im*other.im;
			this.im = this.re*other.im + this.im*other.re;
		}
		
		public double mag() {
			return Math.sqrt(re*re + im*im);
		}
	}

	Comp X[];
	
	public FT(double samples[]) {
		var cx = Arrays.stream(samples)
				.mapToObj(x -> new Comp((double)x))
				.collect(Collectors.toList());
		
		int N = cx.size();
		Comp iu = new Comp(0, 1);
		
		double W = -2*Math.PI / N;
		
		X = new Comp[N];
		
		for (int k = 0; k < N; k++) {
			Comp total = new Comp(0);
			for (int n = 0; n < N; n++) {
				double th = W*k*n;
				
				Comp e = Comp.unitFromArg(th);
				e.mult(cx.get(n));
				
				total.add(e);
			}
			
			X[k] = total;
		}
		
		
		
				
	}
	
	
	
	
	
	
	
	
	public static void main(String args[]) {
		
//		AudioLoader loader = null;
//		try {
//			loader = new AudioLoader("./hitnrun.wav");
//			loader.loadMusicArray();
//			
//			
//			int samples[] = loader.getSamples();
//			
//			FT ft = new FT(samples);
//			
//			
//			
//			
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
		int N = 200;
		
//		
//		for (int i=0; i < N; i++) {
//			x[i] = 2*Math.sin(i) + 4*Math.sin(i);
//			FT ft = new FT(x);
//			
//		}
	}
}
