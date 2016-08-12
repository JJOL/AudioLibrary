import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JFrame;


public class Main {

	private boolean running = false;
	//private final static int WIDTH = 640; // 1200
	private final static int WIDTH = 1200; // 1200
	//private final static int HEIGHT = 640; // 400
	private final static int HEIGHT = 400; // 400
	
	private WindowAudioDisplayer displayer;
	
	private BufferStrategy bs;
	private Canvas canvas;
	private int dFrames, dUpdates;
	public static Font FONT = new Font("Arial", Font.PLAIN, 20);
	
	public Main() {
		canvas = new Canvas();
		canvas.setBackground(Color.BLACK);
		canvas.setSize(WIDTH, HEIGHT);
		JFrame frame = new JFrame("Sound");
		frame.setSize(WIDTH, HEIGHT);
		frame.setOpacity(1);
		frame.add(canvas);
		frame.pack();
		canvas.createBufferStrategy(2);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		AudioLoader loader = null;
		try {
			loader = new AudioLoader("./waitingforlove.wav");
			loader.loadMusicArray();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputInterface.initialize(canvas);
		AudioDisplayConfig adc = new AudioDisplayConfig();
		adc.guiScale = 1f;
		adc.scale = 1f/4f;
		//displayer = new WindowAudioDisplayer(canvas.getWidth(), canvas.getHeight(), loader, adc);
		displayer = new WindowAudioDisplayer(canvas.getWidth(), canvas.getHeight(), loader, adc);
		running = true;
		run();
	}
	
	public static void main(String[] args) {
		new Main();
	}
	
	public void run() {
		final double UNS = 1e9 / 60;
		long lastFrame = System.nanoTime();
		double delta = 0;
		int frames = 0;
		int updates = 0;
		long timer = System.currentTimeMillis();
		
		while(running) {
			long thisFrame = System.nanoTime();
			delta += (thisFrame - lastFrame) / UNS;
			lastFrame = thisFrame;
			while(delta >= 1) {
				updates++;
				update();
				render();
				frames++;
				delta--;
			}
			//render();
			//frames++;
			if(System.currentTimeMillis() - timer >= 1e3 ) {
				dUpdates = updates;
				dFrames = frames;
				frames = 0;
				updates = 0;
				timer += 1e3;
			}
		}
	}
	
	public void update() {
		displayer.update();
	}
	
	public void render() {
		bs = canvas.getBufferStrategy();
		Graphics2D g = (Graphics2D)bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		g.setFont(FONT);
		displayer.draw(g);
		
		// Draw Render Vars

	    
	     
	    g.setColor(Color.WHITE);
		String varInfo = String.format("Frames: %s  |  Updates: %s", dFrames, dUpdates);
		g.drawString(varInfo, 10, canvas.getHeight()-30);
		
		g.dispose();
		bs.show();
	}
	
}
