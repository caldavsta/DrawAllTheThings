package com.onagainapps.sketchallthethings.Tools;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.onagainapps.advancedseekbar.AdvancedSeekBar;
import com.onagainapps.sketchallthethings.R;
import com.onagainapps.sketchallthethings.SketchAllTheThings;



public class PaintBrushDrawer{
	PaintBrush paintBrush;
	
	private static class PaintBrushDrawerHolder {
		AdvancedSeekBar brushSizeSeekBar;
	}
	
	public PaintBrushDrawer(PaintBrush paintBrush){
		this.paintBrush = paintBrush;
	}
	
	public View processView(View convertView, Context context, final ViewGroup parent){
		final PaintBrushDrawer.PaintBrushDrawerHolder paintBrushDrawerHolder;
		
		
		if (convertView != null && convertView.getTag().getClass() == PaintBrushDrawerHolder.class) {
			paintBrushDrawerHolder = (PaintBrushDrawer.PaintBrushDrawerHolder) convertView.getTag();
		} else {
			
			paintBrushDrawerHolder = new PaintBrushDrawer.PaintBrushDrawerHolder();
			
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.tool_brush, parent, false);
			
			
			paintBrushDrawerHolder.brushSizeSeekBar = (AdvancedSeekBar) convertView.findViewById(R.id.tool_brush_advancedseekbar); 
			paintBrushDrawerHolder.brushSizeSeekBar.getSeekBar().setMax(PaintBrush.MAX_BRUSH_SIZE);
			paintBrushDrawerHolder.brushSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					SketchAllTheThings.getInstance().getPaintBrush().setBrushSize(progress);
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					
				}
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					
				}
			});

			convertView.setTag(paintBrushDrawerHolder);
		} 
		
		paintBrushDrawerHolder.brushSizeSeekBar.getSeekBar().setProgress(paintBrush.getBrushSize());
		
		return convertView;
	}
}
