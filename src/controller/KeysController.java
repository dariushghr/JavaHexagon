package controller;

import java.awt.event.KeyEvent;

import model.Constants;
import model.Model;

public class KeysController extends Controller {
	private boolean[] keys;
	private boolean pKeyPressed = false;
	
	
	public KeysController(Model model) {
		super(model);
		keys = new boolean[256];
	}
	
	public void onKeyPressed(KeyEvent e) {
		if (e.getKeyCode() >= keys.length) return;
		keys[e.getKeyCode()] = true;
		
		if (e.getKeyCode() == KeyEvent.VK_P && !pKeyPressed) {
			pKeyPressed = true;
			getModel().togglePause();
		}
	}

	public void onKeyReleased(KeyEvent e) {
		if (e.getKeyCode() >= keys.length) return;
		keys[e.getKeyCode()] = false;
		
		if (e.getKeyCode() == KeyEvent.VK_P) {
			pKeyPressed = false;
		}
	}
	
	public void update(long delta) {
		if (getModel().isPaused()) return;
		
		float x = 0;
		if (keys[KeyEvent.VK_LEFT]) x -= Constants.PLAYER_SPEED;
		if (keys[KeyEvent.VK_RIGHT]) x += Constants.PLAYER_SPEED;
		
		getModel().movePlayer(x * delta, 0);
	}
}
