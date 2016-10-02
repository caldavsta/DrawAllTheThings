package com.onagainapps.sketchallthethings.Tools;

import android.text.Layout;

import com.onagainapps.sketchallthethings.DrawingManager.Command;

/**
 * Created by Caleb on 10/2/2016.
 */

public abstract class Tool {
	private Command commandWhileDrawing;
	private Command commandAfterDrawn;
	
	protected int resource;
	
	
	public Command getCommandWhileDrawing() {
		return commandWhileDrawing;
	}
	
	public Command getCommandAfterDrawn() {
		return commandAfterDrawn;
	}
}
