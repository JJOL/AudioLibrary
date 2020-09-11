package jjol.audiolibrary.gui;
import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/*
 * Class: InputInterface
 * Description: Binds listeners to a Canvas and provides functions for detecting input actions
 */
public class InputInterface {

	private static boolean[] keys = new boolean[1024];
	private static boolean[] buttons = new boolean[256];
	private static int mx, my;
	
	public static void initCanvasListeners(Canvas canvas) {
		// Key Listeners
		canvas.addKeyListener(new KeyListener() {	
			public void keyTyped(KeyEvent e) {}
			
			public void keyReleased(KeyEvent e) {
				keys[e.getKeyCode()] = false;
			}	
			public void keyPressed(KeyEvent e) {
				keys[e.getKeyCode()] = true;
			}
		});
		
		// Mouse Listeners
		canvas.addMouseMotionListener(new MouseMotionListener() {	
			public void mouseMoved(MouseEvent e) {
				mx = e.getX();
			}
			public void mouseDragged(MouseEvent e) {
				my = e.getY();
			}
		});
		canvas.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				buttons[e.getButton()] = false;
			}		
			public void mousePressed(MouseEvent e) {
				buttons[e.getButton()] = true;
			}		
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		});
	}
	
	// Input Functions
	public static boolean isKeyPressed(int keycode ) {
		return keys[keycode];
	}
	
	public static boolean isButtonPressed(int button ) {
		return buttons[button];
	}
	
	public static int getMouseX() {
		return mx;
	}
	
	public static int getMouseY() {
		return my;
	}
}
