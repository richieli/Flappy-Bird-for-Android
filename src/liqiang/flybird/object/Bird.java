package liqiang.flybird.object;

import liqiang.flybird.R;
import liqiang.flybird.config.Config;
import liqiang.flybird.config.Constants;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

public class Bird extends GameObject {
	
	private Bitmap[] birdImgs;
	private Bitmap birdImg;
	
	private final double v0 = Config.v0;
	private final double g = Config.g;
	private final double t = Config.t;
	
	private double speed;
	private double s;
	private float angle;
	
	private float groundHeight;
	
	private Rect obj_rect;

	public Bird(Resources resources, float groundHeight) {
		super(resources);
		this.obj_rect = new Rect();
		this.groundHeight = groundHeight;
		initBitmap();
	}

	@Override
	public void step() {
		double v1 = speed;
		double v = v1 - g * t;
		speed = v;
		s= v1 * t - 0.5 * g * t * t;
		obj_y = obj_y - (float) s;
		if(obj_y <= 0) {
			obj_y = this.obj_height / 2;
		}
		if(obj_y >= Constants.SCREEN_HEIGHT - this.groundHeight - this.obj_height) {
			obj_y = Constants.SCREEN_HEIGHT - this.groundHeight - this.obj_height;
		}
		if(speed >= 0) {
			birdImg = birdImgs[(currentFrame / 3)];
			currentFrame++;
			if(currentFrame == 9) {
				currentFrame = 0;
			}
		} else {
			birdImg = birdImgs[2];
		}
		angle = (float) (s * 4);
		if(angle >= 30) {
			angle = 30;
		}
		if(angle <= -90) {
			angle = -90;
		}
		
		this.obj_mid_y = this.obj_y + this.obj_height / 2;
		
		//obj_rect.left = (int) obj_x;
		//obj_rect.top = (int) obj_y;
		//obj_rect.right = (int) (obj_x + obj_height + (obj_width - obj_height) * (1 - Math.cos(angle)));
		//obj_rect.bottom = (int) Math.floor (obj_y + obj_width - Math.abs((obj_width - obj_height) * Math.sin(angle)));
	
		obj_rect.left = (int) (obj_x + (obj_width - obj_height) / 2);
		obj_rect.top = (int) (obj_y + (obj_width - obj_height) / 2);
		obj_rect.right = (int) (obj_rect.left + obj_height);	
		obj_rect.bottom = (int) (obj_rect.top + obj_height - (obj_width - obj_height) / 2);
	}
	
	public void flappy() {
		speed = v0;
	}

	@Override
	public void drawSelf(Canvas canvas) {
		canvas.save();
		canvas.rotate(-angle, obj_mid_x, obj_mid_y);
		canvas.drawBitmap(birdImg, obj_x, obj_y, paint);
		canvas.restore();
		/*
		paint.setColor(Color.BLACK);
		paint.setAlpha(50);
		canvas.drawRect(obj_rect, paint);
		*/
	}
	
	public boolean pass(Column column) {
		if(this.obj_mid_x <= column.getObjMidX() && column.getObjMidX() - this.obj_mid_x < 5) {
			return true;
		}
		return false;
	}
	
	public boolean hitColumn(Column column) {
		if(this.obj_rect.intersect(column.getObjRectTop()) || this.obj_rect.intersect(column.getObjRectBottom())) {
			return true;
		}
		return false;
	}
	
	public boolean hitGround(Ground ground) {
		/*if(this.obj_rect.intersect(ground.getObjRect())) {
			return true;
		}
		return false;*/
		
		if((this.obj_rect.bottom + 1 ) >= ground.getObjRect().top) {
			return true;
		}
		return false;
	}

	@Override
	public void initBitmap() {
		birdImgs = new Bitmap[3];
		birdImgs[0] = BitmapFactory.decodeResource(resources, R.drawable.bird0);
		birdImgs[1] = BitmapFactory.decodeResource(resources, R.drawable.bird1);
		birdImgs[2] = BitmapFactory.decodeResource(resources, R.drawable.bird2);
		
		birdImg = birdImgs[0];
		
		this.obj_width = birdImg.getWidth();
		this.obj_height = birdImg.getHeight();
		
		this.obj_x = this.obj_width * 2;
		this.obj_y = Constants.SCREEN_HEIGHT / 2 - this.obj_height / 2;
		
		this.obj_mid_x = this.obj_x + this.obj_width / 2;
		this.obj_mid_y = this.obj_y + this.obj_height / 2;
	}

	@Override
	public void release() {
		for(int i=0; i<3; i++) {
			if(!birdImgs[i].isRecycled()) {
				birdImgs[i].recycle();
			}
		}
	}

	public Rect getObjRect() {
		return this.obj_rect;
	}

}
