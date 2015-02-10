package liqiang.flybird.object;

import liqiang.flybird.R;
import liqiang.flybird.config.Config;
import liqiang.flybird.config.Constants;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Ground extends GameObject {

	private Bitmap groundImg;
	
	private Rect obj_rect;
	
	public Ground(Resources resources) {
		super(resources);
		this.obj_rect = new Rect();
		this.obj_x = 0;
		initBitmap();
	}

	@Override
	public void step() {
		this.obj_x -= Config.SPEED;
		if(this.obj_x <= -(this.obj_width - Constants.SCREEN_WIDTH)) {
			this.obj_x = -15;
		}
	}

	@Override
	public void drawSelf(Canvas canvas) {
		canvas.drawBitmap(groundImg, obj_x, obj_y, paint);
	}

	@Override
	public void initBitmap() {
		groundImg = BitmapFactory.decodeResource(resources, R.drawable.ground);
		
		this.obj_width = groundImg.getWidth();
		this.obj_height = groundImg.getHeight();
		
		this.obj_y = Constants.SCREEN_HEIGHT - this.obj_height;
	}
	
	public Rect getObjRect() {
		obj_rect.set(0, (int)obj_y, (int)Constants.SCREEN_WIDTH, (int)Constants.SCREEN_HEIGHT);
		return this.obj_rect;
	}

	@Override
	public void release() {
		if(!groundImg.isRecycled()) {
			groundImg.recycle();
		}
	}

	
	
}
