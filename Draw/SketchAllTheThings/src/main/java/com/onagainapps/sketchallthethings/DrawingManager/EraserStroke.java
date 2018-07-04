package com.onagainapps.sketchallthethings.DrawingManager;

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.onagainapps.sketchallthethings.SketchAllTheThings;



public class EraserStroke extends BrushStroke {
	
	public EraserStroke(Point firstPoint) {
		super(firstPoint);
		//paint.setXfermode(null);
		paint.setAlpha(0xFF);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		paint.setStrokeWidth(SketchAllTheThings.getInstance().getEraser().getEraserSize());
	}
	

}
