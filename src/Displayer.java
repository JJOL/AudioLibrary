import java.awt.Graphics2D;

public abstract class Displayer {

	// Drawing Properties
		protected final int displayWidth;
		protected final int displayHeight;
		protected final int mediumHeight;
		protected int currentSecondLineWidth = 2;
		protected float currentSecondLineX = 0;
		protected final int middleDisplay;
		
		// Audio Sample Properties
		protected AudioLoader loader;
		protected float[] sampleHeights;
		protected int pixelsPerSample;
		protected float pixelsPerSecond;
		protected int sampleRate = 44100;
		protected int GSamplesInDisplay;
		protected int totalSeconds;
		
		// Config and Change
		protected int secondLineSize = 2; // How Wide is the line of seconds
		protected float secondsInDisplay = 0;
		protected int AUSamplesInGSample = 0;
		protected float guiScale;
		protected float scale;
		protected float xOffset;
		protected int speed;
		
		// Move
		protected boolean started = false;
		protected int sR = 0;
		protected int seconds = 0;
		protected float ff = 1f;
	
	public Displayer(int displayWidth, int displayHeight, AudioLoader loader, AudioDisplayConfig config) {
		this.displayWidth = displayWidth;
		this.displayHeight = displayHeight;
		this.mediumHeight = displayHeight/2;
		this.loader = loader;
		guiScale = config.guiScale;
		scale = config.scale;
		xOffset = 0;
		speed = 1;
		middleDisplay = displayWidth/2;
		
		init();
	}
	
	protected void init() {
		// Get Config Values
		System.out.println("Initializating WindowAudioDisplayer!");
		//DoubleFFT_1D fft = new DoubleFFT_1D(10000);
		loadSamplesWithScale();
		int[] samples = loader.getSamples();
		//samples = null;
		//samples = newSamples;
		int mh = loader.getMaxHigh();
		int ml = loader.getMaxLow();
		int sl = loader.getStreamSampleLength();
		sl = samples.length;
		//sl = 44100*30;
		System.out.println("Theorical Seconds: "+(sl / 44100));
		int maxA = 25000;
		int maxY = 150;
		//pixelsPerSample = 1;
		// Column Width 2px, 
		//int width = displayWidth/pixelsPerSample;
		int startX = 0;
		//System.out.println("Medium Height: " );
		int xIndex = 0;
		//int incr = sl / width;
		
		int sampleCount = 0;
		//int seconds = 0;
		totalSeconds = sl/44100;
		System.out.println("It should last " + totalSeconds + "seconds");
		//int lastRecordCount = 0;
		//g.drawString("Game", 10, 10);
		float mAve = 0;
		sampleHeights = //new float[(int)(sl/AUSamplesInGSample)+1];
		new float[(int)(loader.getStreamSampleLength() / AUSamplesInGSample) + 1];
		System.out.println(sampleHeights.length + " vs " + (sl/AUSamplesInGSample));
		int k = 0;

		
		// Go Through Samples Array To get the total samples
				
		for(int sample = 0; sample < sl; sample += AUSamplesInGSample, sampleCount += AUSamplesInGSample) {
			
			int tY;
			// Get The Sample 
			// By End
			//tY =  (int)(((float)samples[sample]/10000.0)*45.0);
			// By middle
			//tY =  (int)(((float)samples[(sample+incr)/2]/mh)*150.0);
			// By Average
			int total = 0;
			int left = ((sl - sample) > AUSamplesInGSample) ? AUSamplesInGSample : sl-sample;
			int j;
			int maxNote = 0, minNote = 0;
			for(j = sample; j < sample+left; j++) {
				int abS = Math.abs(samples[j]);
				if(abS > maxNote) maxNote = abS;
				if(abS < minNote) minNote = abS;
				total += abS;
			}
			float average = total/left;
			//float average = maxNote+minNote/2;
			if(average > mAve) 
				mAve = average;
			//tY = total/left;
			//tY = (int)((average/mh)*150.0);//*/
			tY = (int)((average/11863)*150.0);//*/
			//tY = total / incr;
			//int ty= getTY(sl, sample, mAve);
			//if(tY > 0)
				//g.fillRect(xIndex, oldY, 2, tY);
			//else {
				tY = Math.abs(tY);
				sampleHeights[k++] = tY;
				//g.fillRect(xIndex, oldY-tY, pixelsPerSample, tY);
			//}
			xIndex += pixelsPerSample;
			
			//lastRecordCount = sampleCount;
		}
		
		
		// Get Info
		
		System.out.println("Stream Length: " + sl
				+ "\nMax High: " + mh
				+ "\nMax Low: " + ml
				+ "\nSamp Incr: " + AUSamplesInGSample);
		//System.out.println("Last Record Count " + lastRecordCount);
		System.out.println("Seconds: " + seconds);
		System.out.println("Max Average: " + mAve);
	}
	
	private void loadSamplesWithScale() {
		// Saber Cuantos Samples ocupa cada 'sample renderizado' dado la cantidad de samples
		// que queremos mostrar por window
		// Saber Cuantos Samples tenemos que mostrar
		
		// Size of each sample rendered in the display
		pixelsPerSample = (int)(guiScale * 2);
		//pixelsPerSample = 10;
		assert(pixelsPerSample != 0);
		// Get the amount of samples that can be rendered in the display
		GSamplesInDisplay = displayWidth / pixelsPerSample;
		System.out.println("GSamplesInDisplay: " + GSamplesInDisplay + " - of " + pixelsPerSample + " pixel each");
		// How Many Music Samples from the audio file do we want to 'show' in the window
		// Default 'all'
		int AUSamplesInWindow = (int) (loader.getStreamSampleLength()*scale);
		AUSamplesInGSample = AUSamplesInWindow / GSamplesInDisplay;
		
		assert(AUSamplesInGSample != 0); 
		// Get how many pixels are per second
		secondsInDisplay = AUSamplesInWindow / sampleRate; // Normally 44100
		System.out.println("Seconds Calculalted After: " + secondsInDisplay);
		
		pixelsPerSecond = (displayWidth / secondsInDisplay);
		System.out.println("Pixels Per Second: " + pixelsPerSecond);
		assert(pixelsPerSecond != 0);
		// Now Get the XEnd  of what is resting to show from the music
		
		
	}
	
	public void update() {
		sR ++;
		if(sR % 60 == 0) {
			seconds++;
		}
	};
	
	public abstract void draw(Graphics2D g);
	
}
