package com.onagainapps.sketchallthethings.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.onagainapps.advancedseekbar.AdvancedSeekBar;
import com.onagainapps.sketchallthethings.R;
import com.onagainapps.sketchallthethings.SketchAllTheThings;


/**
 * Created by Caleb on 10/4/2016.
 */

public class EraserDrawer {
	private static final String TAG = EraserDrawer.class.getSimpleName();
	Eraser eraser;
	
	public static class EraserDrawerHolder {
		AdvancedSeekBar eraserSizeSeekBar;
	}
	
	public EraserDrawer(Eraser eraser){
		this.eraser = eraser;
	}
	
	public View processView(View convertView, Context context, final ViewGroup parent){
		final EraserDrawer.EraserDrawerHolder eraserDrawerHolder;
		
		
		
		if (convertView != null && convertView.getTag().getClass() == EraserDrawerHolder.class) {
			eraserDrawerHolder = (EraserDrawer.EraserDrawerHolder) convertView.getTag();
		} else {
			eraserDrawerHolder = new EraserDrawer.EraserDrawerHolder();
			
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.tool_eraser, parent, false);
			
			
			eraserDrawerHolder.eraserSizeSeekBar = (AdvancedSeekBar) convertView.findViewById(R.id.tool_eraser_advancedseekbar);
			eraserDrawerHolder.eraserSizeSeekBar.getSeekBar().setMax(Eraser.MAX_ERASER_SIZE);
			eraserDrawerHolder.eraserSizeSeekBar.SetOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					SketchAllTheThings.getInstance().getEraser().setEraserSize(progress);
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					
				}
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					
				}
			});
			
			convertView.setTag(eraserDrawerHolder);
		}
		
		eraserDrawerHolder.eraserSizeSeekBar.getSeekBar().setProgress(eraser.getEraserSize());
		
		return convertView;
	}
	

}
