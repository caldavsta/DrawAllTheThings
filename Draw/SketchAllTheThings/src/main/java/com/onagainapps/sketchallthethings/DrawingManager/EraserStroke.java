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

/**
 * Created by Caleb on 10/7/2016.
 * 
 * EraserStroke is a BrushStroke. It just happens to be a BrushStroke that draws transparency.
 * todo fix the visual appearance of EraserStroke as it's drawn. It currently shows up black.
 */

public class EraserStroke extends BrushStroke {
	
	public EraserStroke(Point firstPoint) {
		super(firstPoint);
		//paint.setXfermode(null);
		paint.setAlpha(0xFF);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		paint.setStrokeWidth(SketchAllTheThings.getInstance().getEraser().getEraserSize());
	}
	

}
