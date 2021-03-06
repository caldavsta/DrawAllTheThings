package com.onagainapps.sketchallthethings.DrawingManager;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;


public class BrushStroke extends Command {
	
	
	protected List<Point> pointList;
	
	
	protected Paint paint;
	
	protected Path path;
	
	
	
	public BrushStroke(Point firstPoint) {
		pointList = new ArrayList<>();
		path = new Path();
		path.moveTo(firstPoint.x, firstPoint.y);
		appendPointToLine(firstPoint);
		paint = new Paint();
		
		// configure the paint to have rounded edges.
		// todo allow user to modify these parameters in PainBrushDrawer
		CornerPathEffect cornerPathEffect =
				new CornerPathEffect(100);
		paint = new Paint();
		paint.setPathEffect(cornerPathEffect);
		paint.setDither(true);                    // set the dither to true
		paint.setStyle(Paint.Style.STROKE);       // set to STOKE
		paint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
		paint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
		paint.setAntiAlias(true);
		
	}
	
	
	public void appendPointToLine(Point point) {
		pointList.add(point);
		path.lineTo(point.x, point.y);
	}
	
	
	public void draw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);
		canvas.drawPath(getPath(), paint);
	}
	
	
	@Override
	public Rect getBounds() {
		RectF rect = new RectF();
		getPath().computeBounds(rect, false);
		
		Rect result = new Rect();
		result.left = (int) rect.left;
		result.top = (int) rect.top;
		result.right = (int) rect.right;
		result.bottom = (int) rect.bottom;
		
		return result;
	}
	
	public String toString() {
		return "BrushStroke with " + pointList.size() + " points.";
	}
	
	
	public Path getPath() {
		return path;
	}

}
