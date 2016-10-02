package com.onagainapps.sketchallthethings;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.rarepebble.colorpicker.ColorPickerView;


public class BrushSettingsFragment extends DialogFragment {
	private static final String TAG = BrushSettingsFragment.class.getSimpleName();
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private SeekBar seekBar;
	private ColorPickerView colorPickerView;


	DrawingActivity drawingActivity;


	public static BrushSettingsFragment newInstance() {
		BrushSettingsFragment fragment = new BrushSettingsFragment();
		Bundle args = new Bundle();
		//args.putInt(ARG_PARAM1, sketchAllTheThings.getCurrentBrushSize());
		//args.putInt(ARG_PARAM2, sketchAllTheThings.getColor());
		fragment.setArguments(args);

		return fragment;
	}

	public BrushSettingsFragment() {
		// Required empty public constructor
	}

	public void colorButtonPressed(View v) {
		Log.d("caleb " + TAG, "Button Pressed " + v.getId());
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getActivity().getClass() == DrawingActivity.class){
			drawingActivity = (DrawingActivity) getActivity();
		} else {
			Log.d("caleb " + TAG,"BrushSettingsFragment was not created by DrawingActivity. This is a problem.");
		}

		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}



	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_brush_settings, container, false);

		colorPickerView = (ColorPickerView) view.findViewById(R.id.colorPicker);
		colorPickerView.setCurrentColor(SketchAllTheThings.getInstance().getColor());

		seekBar = (SeekBar) view.findViewById(R.id.colorPicker_brushSeekBar);
		seekBar.setMax(SketchAllTheThings.getInstance().MAX_BRUSH_SIZE);
		seekBar.setProgress(SketchAllTheThings.getInstance().getCurrentBrushSize());
		return view;
	}

	@Override
	public void onStart() {
		getView().findViewById(R.id.colorPicker_cancelButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
		getView().findViewById(R.id.colorPicker_okButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SketchAllTheThings.getInstance().setCurrentColor(colorPickerView.getColor());
				SketchAllTheThings.getInstance().setCurrentBrushSize(seekBar.getProgress());
				dismiss();
			}
		});

		super.onStart();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}


}
