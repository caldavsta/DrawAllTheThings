package com.onagainapps.sketchallthethings.DrawingManager;

import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Point;

import com.onagainapps.sketchallthethings.SketchAllTheThings;



public class PaintBrushStroke extends BrushStroke {
	public PaintBrushStroke(Point firstPoint) {
		super(firstPoint);
		
		paint.setColor(SketchAllTheThings.getInstance().getColor());
		paint.setStrokeWidth(SketchAllTheThings.getInstance().getPaintBrush().getBrushSize());
	}
	

}
