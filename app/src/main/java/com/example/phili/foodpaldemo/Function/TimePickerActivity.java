package com.example.phili.foodpaldemo.Function;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TimePicker;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.TimePickerView.OnTimeSelectListener;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by yiren on 2018-03-15.
 */

public class TimePickerActivity extends AppCompatActivity implements View.OnClickListener {
    private TimePickerView timePickerView;

    public TimePickerActivity() {
    }

    Calendar selectedDate = Calendar.getInstance();
    Calendar startDate = Calendar.getInstance();
    Calendar endDate = Calendar.getInstance();
    Calendar currentDate = Calendar.getInstance();

    @Override
    public void onClick(View view) {

    }

    //timePickerView = new TimePickerView.Builder(this, TimePickerView.OnTimeSelectListener)


}
