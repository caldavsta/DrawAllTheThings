package com.onagainapps.sketchallthethings.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.onagainapps.sketchallthethings.R;


public class ImageImportDrawer {
	ImageImport imageImport;
	
	private static class ImageImportDrawerHolder {
		ImageView imageView;
	}
	
	public ImageImportDrawer(ImageImport imageImport){
		this.imageImport = imageImport;
	}
	
	public View processView(View convertView, Context context, final ViewGroup parent){
		final ImageImportDrawer.ImageImportDrawerHolder imageImportDrawerHolder;
		
		if (convertView == null){
			imageImportDrawerHolder = new ImageImportDrawer.ImageImportDrawerHolder();
			
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.tool_image_import, parent, false);
			
			
			imageImportDrawerHolder.imageView = (ImageView) convertView.findViewById(R.id.tool_image_imageview);
			
			convertView.setTag(imageImportDrawerHolder);
			
		} else {
			imageImportDrawerHolder = (ImageImportDrawer.ImageImportDrawerHolder) convertView.getTag();
		}
		
		//update the view here!!!
		
		return convertView;
	}
	
}
