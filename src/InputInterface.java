import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputInterface {

	private static boolean[] keys = new boolean[1024];
	private static boolean[] buttons = new boolean[256];
	private static int mx, my;
	
	public static void initialize(Canvas canvas) {
		canvas.addKeyListener(new KeyListener() {
			

			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				keys[e.getKeyCode()] = false;
			}
			
			public void keyPressed(KeyEvent e) {
				keys[e.getKeyCode()] = true;
			}
		});
		
		canvas.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				mx = e.getX();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
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
			
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
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
