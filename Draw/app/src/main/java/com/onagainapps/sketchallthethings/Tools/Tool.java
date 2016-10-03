package com.onagainapps.sketchallthethings.Tools;

import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;

import com.onagainapps.sketchallthethings.DrawingManager.Command;

import java.util.Dictionary;

/**
 * Created by Caleb on 10/2/2016.
 */

public abstract class Tool{
	
	public static final int PAINT_BRUSH = 0;
	
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
	
}