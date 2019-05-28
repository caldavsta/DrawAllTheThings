package com.onagainapps.sketchallthethings.Tools;

import com.onagainapps.sketchallthethings.R;



public class Eraser extends Tool {
	private static final int DEFAULT_ERASER_SIZE = 12;
	public static final int MAX_ERASER_SIZE = 100;
	public static final int MIN_ERASER_SIZE = 3;
	
	private int eraserSize;
	private EraserDrawer eraserDrawer;
	
	
	public Eraser(){
		super.resource = R.layout.tool_eraser;
		super.displayName = "Eraser";
		
		eraserSize = DEFAULT_ERASER_SIZE;
		
		
		eraserDrawer = new EraserDrawer(this);
	}
	
	
	
	public int getEraserSize() {
		return eraserSize;
	}
	
	public void setEraserSize(int eraserSize) {
		this.eraserSize = Math.max(MIN_ERASER_SIZE, Math.min(MAX_ERASER_SIZE, eraserSize));
	}
	
	public EraserDrawer getEraserDrawer() {
		return eraserDrawer;
	}
	@Override
	public int getToolType() {
		return Tool.ERASER;
	}
	
	@Override
	public int getIconDrawable() {
		return R.drawable.ic_tool_eraser;
	}
}
