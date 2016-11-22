package com.onagainapps.sketchallthethings.Tools;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.onagainapps.sketchallthethings.R;

/**
 * Created by Caleb on 10/2/2016.
 */

public class PaintBrush extends Tool {
	private static final int DEFAULT_BRUSH_SIZE = 12;
	public static final int MAX_BRUSH_SIZE = 250;
	public static final int MIN_BRUSH_SIZE = 3;
	public static int icon = R.drawable.ic_tool_eraser;
	
	private int brushSize;
	private PaintBrushDrawer paintBrushDrawer;
	
	
	public PaintBrush(){
		super.resource = R.layout.tool_brush;
		super.displayName = "Paint Brush";
		
		brushSize = DEFAULT_BRUSH_SIZE;
		
		paintBrushDrawer = new PaintBrushDrawer(this);
	}
	
	
	
	public int getBrushSize() {
		return brushSize;
	}
	
	public void setBrushSize(int brushSize) {
		this.brushSize = Math.max(MIN_BRUSH_SIZE, Math.min(MAX_BRUSH_SIZE, brushSize));
	}
	
	public PaintBrushDrawer getPaintBrushDrawer() {
		return paintBrushDrawer;
	}
	
	@Override
	public int getToolType() {
		return Tool.PAINT_BRUSH;
	}
	
	@Override
	public int getIconDrawable() {
		return R.drawable.ic_tool_paintbrush;
	}
}
