package com.onagainapps.sketchallthethings.DrawingManager;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Caleb on 9/24/2016.
 */
public abstract class Command {
	public int pointer;
	public Paint paint;
	public abstract void draw(Canvas canvas);
	public abstract Rect getBounds();
}
