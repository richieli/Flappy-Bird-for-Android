package liqiang.flybird.util;

import java.util.HashMap;

import liqiang.flybird.MainActivity;
import liqiang.flybird.R;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {

	private SoundPool soundPool;
	private MainActivity mainActivity;
	private HashMap<Integer, Integer> map;
	
	public SoundPlayer(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		map = new HashMap<Integer, Integer>();
		soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
	}
	
	public void initSounds() {
		map.put(1, soundPool.load(mainActivity, R.raw.flappy, 1));//飞扬
		map.put(2, soundPool.load(mainActivity, R.raw.pass, 1));//通过管子
		map.put(3, soundPool.load(mainActivity, R.raw.hit, 1));//撞到
		map.put(4, soundPool.load(mainActivity, R.raw.die, 1));//死掉
		map.put(5, soundPool.load(mainActivity, R.raw.swooshing, 1));//切换
	}
	
	public void playSound(int sound, int loop) {
		soundPool.play(sound, 1, 1, 1, loop, 1.0f);
	}
	
}
