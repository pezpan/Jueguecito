package com.example.cdp.jueguecito.timepicker;

/**
 * Created by CDP on 10/09/2016.
 */
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.example.cdp.jueguecito.R;

/**
 * A dialog that prompts the user for the time of day using a {@link TimePicker}.
 */
public class MyTimePickerDialog extends AlertDialog implements OnClickListener,
        TimePicker.OnTimeChangedListener {

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (they clicked on the 'Set' button).
     */
    public interface OnTimeSetListener {

        /**
         * @param view The view associated with this listener.
         * @param minute The minute that was set.
         */
        void onTimeSet(TimePicker view, int minute, int seconds);
    }

    private static final String MINUTE = "minute";
    private static final String SECONDS = "seconds";

    private final TimePicker mTimePicker;
    private final OnTimeSetListener mCallback;
    private final java.text.DateFormat mDateFormat;

    int mInitialMinute;
    int mInitialSeconds;

    /**
     * @param context Parent.
     * @param callBack How parent is notified.
     * @param minute The initial minute.
     */
    public MyTimePickerDialog(Context context, OnTimeSetListener callBack, int minute, int seconds) {
        this(context, 0, callBack, minute, seconds);
    }

    /**
     * @param context Parent.
     * @param theme the theme to apply to this dialog
     * @param callBack How parent is notified.
     */
    public MyTimePickerDialog(Context context, int theme, OnTimeSetListener callBack, int minute, int seconds) {
        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCallback = callBack;
        mInitialMinute = minute;
        mInitialSeconds = seconds;

        mDateFormat = DateFormat.getTimeFormat(context);
        updateTitle(mInitialMinute, mInitialSeconds);

        setButton(context.getText(R.string.time_set), this);
        setButton2(context.getText(R.string.cancel), (OnClickListener) null);
        //setIcon(android.R.drawable.ic_dialog_time);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.time_picker_dialog, null);
        setView(view);
        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);

        // initialize state
        mTimePicker.setCurrentMinute(mInitialMinute);
        mTimePicker.setCurrentSecond(mInitialSeconds);
        mTimePicker.setOnTimeChangedListener(this);
    }

    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            mTimePicker.clearFocus();
            mCallback.onTimeSet(mTimePicker, mTimePicker.getCurrentMinute(), mTimePicker.getCurrentSeconds());
        }
    }

    public void onTimeChanged(TimePicker view, int minute, int seconds) {
        updateTitle(minute, seconds);
    }

    public void updateTime(int minutOfHour, int seconds) {
        mTimePicker.setCurrentMinute(minutOfHour);
        mTimePicker.setCurrentSecond(seconds);
    }

    private void updateTitle(int minute, int seconds) {
        setTitle(String.format("%02d" , minute) + ":" + String.format("%02d" , seconds));
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(MINUTE, mTimePicker.getCurrentMinute());
        state.putInt(SECONDS, mTimePicker.getCurrentSeconds());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int minute = savedInstanceState.getInt(MINUTE);
        int seconds = savedInstanceState.getInt(SECONDS);
        mTimePicker.setCurrentMinute(minute);
        mTimePicker.setCurrentSecond(seconds);
        mTimePicker.setOnTimeChangedListener(this);
        updateTitle(minute, seconds);
    }


}