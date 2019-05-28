package com.onagainapps.sketchallthethings.Tools;

import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;

import com.onagainapps.sketchallthethings.DrawingManager.Command;

import java.util.Dictionary;



public abstract class Tool{
	
	public static final int TOOL_PREFIX = 91293199;
	
	public static final int PAINT_BRUSH = 0;
	public static final int ERASER = 1;
	public static final int ZOOM_AND_PAN = 2;
	public static final int IMAGE_IMPORT = 3;
	
	private Command commandWhileDrawing;
	private Command commandAfterDrawn;
	protected String displayName;
	protected int resource;
	
	public Command getCommandWhileDrawing() {
		return commandWhileDrawing;
	}
	
	public Command getCommandAfterDrawn() {
		return commandAfterDrawn;
	}
	
	public int getResource() {
		return resource;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public abstract int getToolType();
	
	public abstract int getIconDrawable();
	
	

}