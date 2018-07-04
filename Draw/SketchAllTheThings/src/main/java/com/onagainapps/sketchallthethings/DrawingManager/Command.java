package com.onagainapps.sketchallthethings.DrawingManager;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


public abstract class Command {
	public abstract void draw(Canvas canvas);
	public abstract Rect getBounds();
}
