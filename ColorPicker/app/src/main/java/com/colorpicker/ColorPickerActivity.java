//  The MIT License (MIT)

//  Copyright (c) 2018 Intuz Pvt Ltd.

//  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files
//  (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify,
//  merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:

//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
//  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
//  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.colorpicker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class ColorPickerActivity extends AppCompatActivity {


    private Bitmap bitmapColor;
    private Bitmap bitmapColorWheel;
    private SeekBar seekBarColorPicker;
    private ImageView imgColorPicker;
    private ImageView imgColorSelected;
    private TextView txtCancel;
    private TextView txtDone;
    private ImageView imgColorBackground;
    private int color = Color.BLACK;
    private int[] mColors = new int[]{Color.BLACK, Color.WHITE};
    private String selectedColor = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_color_picker);


        bitmapColor = BitmapFactory.decodeResource(getResources(), R.drawable.color_picker);
        bitmapColorWheel = BitmapFactory.decodeResource(getResources(), R.drawable.color_wheel);

        txtCancel = (TextView) findViewById(R.id.txtCancel);
        txtDone = (TextView) findViewById(R.id.txtDone);
        seekBarColorPicker = (SeekBar) findViewById(R.id.seekBarColorPicker);
        imgColorBackground = (ImageView) findViewById(R.id.imgColorBackground);
        imgColorPicker = (ImageView) findViewById(R.id.imgColorPicker);
        imgColorSelected = (ImageView) findViewById(R.id.imgColorSelected);

        imgColorBackground.setOnTouchListener(onTouchListener);


        setGradients();
        updateResultData(color);

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent();
                i.putExtra("selectedColor", selectedColor);
                setResult(RESULT_OK,i);
                finish();
                color = Color.BLACK;
                mColors = new int[]{Color.BLACK, Color.WHITE};

            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                color = Color.BLACK;
                mColors = new int[]{Color.BLACK, Color.WHITE};
            }
        });

        seekBarColorPicker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                int alpha;
                if (progress == 100) {
                    alpha = 0;
                    setAlpha(alpha);
                } else if (progress == 0) {
                    alpha = 255;
                    setAlpha(alpha);
                } else {
                    float a = progress / 100f;

                    alpha = (int) (a * 255);
                    alpha = 255 - alpha;
                    setAlpha(alpha);
                }
            }
        });
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();

            int x1 = x - bitmapColor.getWidth() / 2;
            int y1 = y - bitmapColor.getHeight() / 2;
            try {
                int status = checkCircle(motionEvent.getX(), motionEvent.getY());
                if (status == 0) {
                    imgColorPicker.setX(x1);
                    imgColorPicker.setY(y1);
                    color = ColorPicker.pickColor(view, x, y);
                    mColors[0] = color;
                    seekBarColorPicker.setProgress(0);
                    updateResultData(color);
                    setGradients();
                }

            } catch (NullPointerException e) {
                return false;
            }

            return true;
        }
    };

    //retrict color picker to within provided image
    public int checkCircle(float xTouch, float yTouch) {

        float centerX = (bitmapColorWheel.getWidth()) / 2;
        float centerY = (bitmapColorWheel.getHeight()) / 2;

        float distance = (float) Math.sqrt((xTouch - centerX) * (xTouch - centerX) +
                (yTouch - centerY) * (yTouch - centerY));
        distance = distance + (bitmapColor.getWidth() / 2);
        if (distance > centerX) {
            return 1;
        } else {
            return 0;
        }
    }

    private void setGradients() {
        LayerDrawable mDrawable = (LayerDrawable) seekBarColorPicker.getProgressDrawable();
        GradientDrawable gd = (GradientDrawable) mDrawable.getDrawable(0);
        gd.setColors(mColors);
        seekBarColorPicker.setProgressDrawable(mDrawable);
    }

    private void updateResultData(int color) {
        imgColorSelected.setColorFilter(color);
        selectedColor = String.format("#%08X", (0xFFFFFFFF & color));
    }

    private void setAlpha(int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int c = Color.argb(alpha, red, green, blue);
        updateResultData(c);
    }

}
