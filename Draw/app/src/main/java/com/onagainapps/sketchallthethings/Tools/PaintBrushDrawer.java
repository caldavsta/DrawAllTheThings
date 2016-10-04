package com.onagainapps.sketchallthethings.Tools;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.onagainapps.sketchallthethings.R;

/**
 * Created by Caleb on 10/2/2016.
 */

public class PaintBrushDrawer implements SeekBar.OnSeekBarChangeListener{
	PaintBrush paintBrush;
	
	private static class PaintBrushDrawerHolder {
		SeekBar brushSizeSeekBar;
	}
	
	public PaintBrushDrawer(PaintBrush paintBrush){
		this.paintBrush = paintBrush;
	}
	
	public View processView(View convertView, Context context, final ViewGroup parent){
		final PaintBrushDrawerHolder paintBrushDrawerHolder;
		
		if (convertView == null){
			paintBrushDrawerHolder = new PaintBrushDrawerHolder();
			
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.tool_brush, parent, false);
			
			
			paintBrushDrawerHolder.brushSizeSeekBar = (SeekBar) convertView.findViewById(R.id.tool_paintbrush_sizeSeekBar); 
			paintBrushDrawerHolder.brushSizeSeekBar.setOnSeekBarChangeListener(this);
			
			convertView.setTag(paintBrushDrawerHolder);
			
		} else {
			paintBrushDrawerHolder = (PaintBrushDrawerHolder) convertView.getTag();
		}
		
		paintBrushDrawerHolder.brushSizeSeekBar.setProgress(paintBrush.getBrushSize());
		
		return convertView;
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		paintBrush.setBrushSize(progress);
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	}
}
