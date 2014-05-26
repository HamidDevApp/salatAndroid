package com.skanderjabouzi.salat;

import java.util.Calendar;
import android.util.Log;

import com.skanderjabouzi.salat.MySwitch.OnChangeAttemptListener;

import kankan.wheel.R;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class HijriActivity extends Activity implements OnChangeAttemptListener, OnCheckedChangeListener{

	MySwitch slider;
	Hijri hijri;
	public static final int ISLAMIC = 1;
	public static final int CHRISTIAN = 0;
	public int switch_val = CHRISTIAN;
	public int[] hijriDates = new int[3];
	public int month, day, year;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.skanderjabouzi.salat.R.layout.hijri);

		slider = (MySwitch)findViewById(com.skanderjabouzi.salat.R.id.pickup3);
		slider.setOnCheckedChangeListener(this);

		Calendar calendar = Calendar.getInstance();
		hijri = new Hijri();

		final WheelView month1 = (WheelView) findViewById(com.skanderjabouzi.salat.R.id.month1);
		final WheelView year1 = (WheelView) findViewById(com.skanderjabouzi.salat.R.id.year1);
		final WheelView day1 = (WheelView) findViewById(com.skanderjabouzi.salat.R.id.day1);

		final WheelView month2 = (WheelView) findViewById(com.skanderjabouzi.salat.R.id.month2);
		final WheelView year2 = (WheelView) findViewById(com.skanderjabouzi.salat.R.id.year2);
		final WheelView day2 = (WheelView) findViewById(com.skanderjabouzi.salat.R.id.day2);

		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (switch_val == CHRISTIAN)
				{
					month = month1.getCurrentItem();
					day = day1.getCurrentItem();
					year = year1.getCurrentItem() - 50;
					hijriDates = hijri.islToChr(year, month, day, 0);
					month2.setCurrentItem(hijriDates[1]);
					day2.setCurrentItem(hijriDates[0] - 1);
					year2.setCurrentItem(hijriDates[2] - 570);
					Log.i("MONTH1 ", String.valueOf(month));
					Log.i("DAY1 ", String.valueOf(day));
					Log.i("YEAR1 ", String.valueOf(year));
					Log.i("MONTH2 ", String.valueOf(hijriDates[1]));
					Log.i("DAY2 ", String.valueOf(hijriDates[0]));
					Log.i("YEAR2 ", String.valueOf(hijriDates[2]));
				}
				else
				{
					month = month2.getCurrentItem();
					day = day2.getCurrentItem();
					year = year2.getCurrentItem() + 570;
					hijriDates = hijri.chrToIsl(year, month, day, 0);
					month1.setCurrentItem(hijriDates[1]);
					day1.setCurrentItem(hijriDates[0]);
					year1.setCurrentItem(hijriDates[2] + 50);
					Log.i("MONTH2 ", String.valueOf(month));
					Log.i("DAY2 ", String.valueOf(day));
					Log.i("YEAR2 ", String.valueOf(year));
					Log.i("MONTH1 ", String.valueOf(hijriDates[1]));
					Log.i("DAY1 ", String.valueOf(hijriDates[0]));
					Log.i("YEAR1 ", String.valueOf(hijriDates[2]));
				}
			}
		};

		Log.i("HIJRI : ", String.valueOf(calendar.get(Calendar.YEAR)) + " " +  String.valueOf(calendar.get(Calendar.MONTH)) + " " + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
		hijriDates = hijri.chrToIsl(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0);

		String months1[] = {"Muharram","Safar","Rabii 1","Rabii 2","Jumada 1","Jumada 2","Rajab","Sha\'ban","Ramadhan","Shawwal","Dhul Qa\'dah","Dhul Hijjah"};
		month1.setViewAdapter(new DateArrayAdapter(this, months1, 0));
		month1.setCurrentItem(hijriDates[1]);
		month1.addChangingListener(listener);
		year1.setViewAdapter(new NumericWheelAdapter(this, -50, hijriDates[2]+20));
		year1.setCurrentItem(hijriDates[2] + 50);
		year1.addChangingListener(listener);
		day1.setViewAdapter(new NumericWheelAdapter(this, 1, 30));
		day1.setCurrentItem(hijriDates[0]);
		day1.addChangingListener(listener);

		int curMonth = calendar.get(Calendar.MONTH);
		String months2[] = {"January","February","March","April","May","June","July","August","September","October","November","December"};
		month2.setViewAdapter(new DateArrayAdapter(this, months2, 0));
		month2.setCurrentItem(curMonth);
		month2.addChangingListener(listener);
		int curYear = calendar.get(Calendar.YEAR);
		year2.setViewAdapter(new NumericWheelAdapter(this, 570, curYear+20));
		year2.setCurrentItem(curYear - 570);
		year2.addChangingListener(listener);
		day2.setViewAdapter(new NumericWheelAdapter(this, 1, 31));
		day2.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
		day2.addChangingListener(listener);
	}

	@Override
	public void onChangeAttempted(boolean isChecked) {
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Log.d("ONCHANGED : ","onChechedChanged(checked = "+isChecked+")");
		switch_val = CHRISTIAN;
		if (isChecked){
			switch_val = ISLAMIC;
		}
	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class DateArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			//setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			//if (currentItem == currentValue) {
				//view.setTextColor(0xFF0000F0);
			//}
			//view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}
}
