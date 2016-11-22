package com.onagainapps.sketchallthethings.Tools;

import com.onagainapps.sketchallthethings.R;

/**
 * Created by Caleb on 10/7/2016.
 * 
 * Not implemented.
 */
public class ImageImport extends Tool {// IMAGE IMPORTER IS NOT IMPLEMENTED
	
	private ImageImportDrawer imageImportDrawer;
	
	
	public ImageImport(){
		super.resource = R.layout.tool_eraser;
		super.displayName = "Image Import";
		
		
		
		imageImportDrawer = new ImageImportDrawer(this);
	}
	
	
	
	public ImageImportDrawer getImageImportDrawer() {
		return imageImportDrawer;
	}
	@Override
	public int getToolType() {
		return Tool.IMAGE_IMPORT;
	}
	
	@Override
	public int getIconDrawable() {
		return R.drawable.ic_tool_eraser;
	}
	
}
