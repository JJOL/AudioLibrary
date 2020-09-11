package jjol.audiolibrary.loading;


import java.io.File;
import java.io.FileNotFoundException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioLoader implements Runnable {

	private int sample = 0;
	private boolean sampling;
	private Thread thread;
	private File file;
	private AudioInputStream in;
	private int[][] toReturn;
	private int maxHigh = 0;
	private int maxLow = 0;
	private int streamLength;
	
	private int nBytes;
	
	
	
	public AudioLoader(String name) throws FileNotFoundException{
		String path = System.getProperty("user.dir");
		file = new File(name);
		if(!file.exists()) {
			System.out.println("-------------File Not Loaded!------------------");
			System.out.println("Path: " + file.getPath());
			FileNotFoundException ex = new FileNotFoundException("File " + name + "was not found!");
			throw ex;
		} else{
			System.out.println("[SOUNDMC] Has Successfully Loaded File!! ---------");
		}
	}
	
	public void run() {
		
		try {
			System.out.println("prepare");
			Thread.sleep(3000);
			System.out.println("Ready");
			in = AudioSystem.getAudioInputStream(file);	
			AudioFormat infoThis = in.getFormat();
			
			int frameSize = (int)infoThis.getFrameSize();
			int streamLength = (int)in.getFrameLength();
			
			System.out.println("Sample Rate: "+infoThis.getSampleRate());
			System.out.println("Channels: "+infoThis.getChannels());
			System.out.println("Sample Size: "+infoThis.getSampleSizeInBits());
			System.out.println("Frame Rate: "+infoThis.getFrameRate());
			System.out.println("Frame Size: "+infoThis.getFrameSize());
			System.out.println("Frame Length: "+in.getFrameLength());
			
			
			
			if(!file.getName().endsWith("wav")) {
				AudioFormat baseFormat = in.getFormat();
				AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
						baseFormat.getSampleRate(),
						16,
						baseFormat.getChannels(),
						baseFormat.getChannels()*2,
						baseFormat.getSampleRate(),
						false);
				AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
				//in.close();
				in = din;
			}
			
			
			long count = 0;
			
			nBytes = 0;
			byte[] abData = new byte[128000];
			
			long oldTime = System.currentTimeMillis();
			boolean before = true;
			byte prev = 0;
			long bCount = 0;
			while(nBytes != -1) {
				
				nBytes = in.read(abData, 0, abData.length); // 100
				if(nBytes < 0) 
					break;
				int sampleNow = 0;
				byte[] newBytes = new byte[nBytes]; // 50
				for(int i=0; i < nBytes; i++) {
					newBytes[i] = abData[i];
					
					before = !before;
					if(before){
						 sampleNow = (int)(prev<<8) | abData[i];
								
						prev = 0;
					} else {
						prev = abData[i];
					}
				
					long currentTime = System.currentTimeMillis();
					if (currentTime - oldTime > 100) {
						
						//sample = (int)newBytes[i];
						sample = sampleNow;
						oldTime = currentTime;
					}
					bCount++;
					if(count++ >= (44100*2*2)) {
						Thread.sleep(1000);
						count = 0;
					}
				}
				
			}
			in.close();
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			sampling = false;
		}
	}
	
	public void loadMusicArray() {
		try {
			// Check if its an mp3 to convert it to wav
			if (file.getName().endsWith("mp3")) {
				AudioInputStream rin = AudioSystem.getAudioInputStream(file);
				AudioFormat baseFormat = rin.getFormat();
				AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
															baseFormat.getSampleRate(),
															16,
															baseFormat.getChannels(),
															baseFormat.getChannels()*2,
															baseFormat.getSampleRate(),
															false);
				in = AudioSystem.getAudioInputStream(decodedFormat, rin);
				rin.close();
			} else {
				in = AudioSystem.getAudioInputStream(file);
			}
			
			int frameSize = (int)in.getFormat().getFrameSize();
			streamLength = (int)in.getFrameLength();
			int numChannels = in.getFormat().getChannels();
			
			
			System.out.println("Frame Size: " + frameSize);
			System.out.println("Frame Rate: " + in.getFormat().getFrameRate());
			System.out.println("Sample Size Bits: " + in.getFormat().getSampleSizeInBits());
			System.out.println("Sample Rate: " + in.getFormat().getSampleRate());
			System.out.println("Stream Length: " + streamLength);
			System.out.println("Num Channels: " + numChannels);
			System.out.println("Frame Rate: " + in.getFormat().getFrameRate());
			toReturn = new int[numChannels][streamLength];
			byte[] musicStreamBytes = new byte[frameSize * streamLength];
			
			in.read(musicStreamBytes);
			in.close();
			int sampleIndex = 0;
			
			for (int t = 0; t < musicStreamBytes.length; ) {
				for(int channel = 0; channel < numChannels; channel++) {
					int low =  (int) musicStreamBytes[t];
					t++;
					int high = (int) musicStreamBytes[t];
					t++;
					
					int sample = get16BitSample(high, low);
					if(sample > maxHigh) 
						maxHigh = sample;
					if(sample < maxLow)
						maxLow = sample;
					toReturn[channel][sampleIndex] = sample;
					
				}
				sampleIndex++;
				
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			
		}
		
	}
	
	private int get16BitSample(int high, int low) {
		return   (high << 8) + (low & 0x00ff);
	}
	
	public int getMaxHigh() {
		return maxHigh;
	}
	
	public int getMaxLow() {
		return maxLow;
	}
	
	public int getStreamSampleLength() {
		return streamLength;
	}
	
	public int[] getSamples() {
		/*int[] allSamples = new int[streamLength];
		int i = 0;
		while(i < streamLength) {
			int s1  = toReturn[0][i];
			int s2 = toReturn[0][i+1];
			int s3 = toReturn[1][i];
			int s4 = toReturn[1][i+1];
			allSamples[i++] = s1;
			allSamples[i++] = s2;
			allSamples[i++] = s3;
			allSamples[i++] = s4;
			
		}*/
		return toReturn[0];
		//return allSamples;
	}
	
//	public void startSampling() {
//		sampling = true;
//		thread = new Thread(this);
//		thread.start();
//	}
	
	public void stop() {
		nBytes = -1;
	}
	
	public int getSample() {
		return sample;
	}
	
	public boolean isSampling() {
		return sampling;
	}
	
	
	
	public void play() {
		Clip clip;
		try {
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
		} catch(Exception e) {
			
		}
	}
	
}
