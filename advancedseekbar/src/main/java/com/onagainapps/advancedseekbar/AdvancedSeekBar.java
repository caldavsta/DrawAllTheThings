package com.onagainapps.advancedseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Caleb on 10/7/2016.
 */

public class AdvancedSeekBar extends RelativeLayout implements SeekBar.OnSeekBarChangeListener {
	private static final String TAG = AdvancedSeekBar.class.getSimpleName();
	
	SeekBar seekBar;
	TextView textView;
	
	CharSequence originalText = "";
	int originalWidth = 0;
	
	SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = null;
	

	public AdvancedSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		
		
		
		
		inflate(context, R.layout.advanced_seek_bar, this);
		
		seekBar = (SeekBar) findViewById(R.id.advanced_seek_bar_seekbar);
		textView = (TextView) findViewById(R.id.advanced_seek_bar_textview);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AdvancedSeekBar);
		try {
			originalText = ta.getText(R.styleable.AdvancedSeekBar_originaltext);
			seekBar.setMax(ta.getInt(R.styleable.AdvancedSeekBar_max, 10));
		}
		finally {
			ta.recycle();
		}
		textView.setText(originalText);
		originalWidth = textView.getWidth();
		Log.d("caleb " + TAG," " +originalWidth);
		//textView.setLayoutParams(textView.getLayoutParams().width = textView.getWidth());
		
		seekBar.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		
		seekBar.setOnSeekBarChangeListener(this);
		
	}
	
	
	
	public SeekBar getSeekBar() {
		return seekBar;
	}
	
	public TextView getTextView() {
		return textView;
	}
	
	public void SetOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener){
		this.onSeekBarChangeListener = onSeekBarChangeListener;
	}
	
	Boolean timerIsSet = false;
	CountDownTimer timer;
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		textView.setText(Integer.toString(progress));
		if (timerIsSet) {
			timer.cancel();
			Log.d("caleb " + TAG,"countdown");
		}
		timerIsSet = true;
		timer = new CountDownTimer(500, 50) {
			@Override
			public void onTick(long millisUntilFinished) {
				
			}
			
			@Override
			public void onFinish() {
				Log.d("caleb " + TAG,"done");
				timerIsSet = false;
				textView.setText(originalText);
			}
		}.start();
		if (onSeekBarChangeListener != null){
			Log.d("caleb " + TAG,"ProgressChanged");
			onSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
		}
	}
	
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		if (onSeekBarChangeListener != null) {
			onSeekBarChangeListener.onStartTrackingTouch(seekBar);
		}
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (onSeekBarChangeListener != null) {
			onSeekBarChangeListener.onStopTrackingTouch(seekBar);
		}
	}
	
	public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
		this.onSeekBarChangeListener = onSeekBarChangeListener;
	}
}
