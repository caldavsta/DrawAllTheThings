package com.onagainapps.sketchallthethings.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.onagainapps.sketchallthethings.R;

/**
 * Created by Caleb on 10/4/2016.
 */

public class EraserDrawer implements SeekBar.OnSeekBarChangeListener{
	Eraser eraser;
	
	private static class EraserDrawerHolder {
		SeekBar eraserSizeSeekBar;
	}
	
	public EraserDrawer(Eraser eraser){
		this.eraser = eraser;
	}
	
	public View processView(View convertView, Context context, final ViewGroup parent){
		final EraserDrawer.EraserDrawerHolder eraserDrawerHolder;
		
		if (convertView == null){
			eraserDrawerHolder = new EraserDrawer.EraserDrawerHolder();
			
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.tool_eraser, parent, false);
			
			
			eraserDrawerHolder.eraserSizeSeekBar = (SeekBar) convertView.findViewById(R.id.tool_eraser_sizeSeekBar);
			eraserDrawerHolder.eraserSizeSeekBar.setOnSeekBarChangeListener(this);
			
			convertView.setTag(eraserDrawerHolder);
			
		} else {
			eraserDrawerHolder = (EraserDrawer.EraserDrawerHolder) convertView.getTag();
		}
		
		eraserDrawerHolder.eraserSizeSeekBar.setProgress(eraser.getEraserSize());
		
		return convertView;
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		eraser.setEraserSize(progress);
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	}
}
