package com.onagainapps.sketchallthethings.DrawingManager;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import com.onagainapps.sketchallthethings.SketchAllTheThings;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * Created by Caleb on 9/24/2016.
 */
public class BrushStroke extends Command {
	
	private List<Point> pointList;
	private List<Long> timeList;
	private Path path;
	private int brushColor;
	private int brushSize;
	
	
	private int tool;
	
	public BrushStroke(Point firstPoint) {
		pointList = new ArrayList<Point>();
		timeList = new ArrayList<Long>();
		path = new Path();
		path.moveTo(firstPoint.x, firstPoint.y);
		addPointToLine(firstPoint);
		
		brushColor = SketchAllTheThings.getInstance().getColor();
		brushSize = SketchAllTheThings.getInstance().getPaintBrush().getBrushSize();
		
		paint = new Paint();
		setupPaint();
	}
	
	public void addPointToLine(Point point) {
		pointList.add(point);
		timeList.add(System.currentTimeMillis());
		path.lineTo(point.x, point.y);
	}
	
	public void draw(Canvas canvas) {
		paint.setColor(getBrushColor());
		paint.setStrokeWidth(getBrushSize());
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
	
	private void setupPaint() {
		//setup paint that draws the commands
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
	
	public String toString() {
		return "BrushStroke with " + pointList.size() + " points.";
	}
	
	public List<Point> getPointList() {
		return pointList;
	}
	
	public int getBrushColor() {
		return brushColor;
	}
	
	public void setBrushColor(int brushColor) {
		this.brushColor = brushColor;
	}
	
	public int getBrushSize() {
		return brushSize;
	}
	
	public void setBrushSize(int brushSize) {
		this.brushSize = brushSize;
	}
	
	public int getTool() {
		return tool;
	}
	
	public void setTool(int tool) {
		this.tool = tool;
	}
	
	public Path getPath() {
		//Path result = new Path();
		//path.offset(getOffsetX(), getOffsetY(), result);
		return path;
	}
	
	private static double distanceBetween(Point a, Point b) {
		double distance = Math.hypot(a.x - b.x, a.y - b.y);
		
		return distance;
	}
}
