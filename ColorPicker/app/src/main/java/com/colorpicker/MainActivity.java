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

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_COLOR_PICKER = 1;
    private Button btnDialog;
    private Button btnActivity;
    private Bitmap bitmapColor;
    private Bitmap bitmapColorWheel;
    private SeekBar seekBarColorPicker;
    private ImageView imgColorPicker;
    private ImageView imgColorSelected;
    private int color = Color.BLACK;
    private int[] mColors = new int[]{Color.BLACK, Color.WHITE};
    private String selectedColor = "";
    private LinearLayout lnrMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lnrMain = (LinearLayout) findViewById(R.id.lnrMain);
        btnDialog = (Button) findViewById(R.id.btnDialog);
        btnActivity = (Button) findViewById(R.id.btnActivity);
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorPickerDialog();
            }
        });
        btnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ColorPickerActivity.class);
                startActivityForResult(i, REQUEST_COLOR_PICKER);
            }
        });

        bitmapColor = BitmapFactory.decodeResource(getResources(), R.drawable.color_picker);
        bitmapColorWheel = BitmapFactory.decodeResource(getResources(), R.drawable.color_wheel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //handle color picker activity result
        if (resultCode == RESULT_OK && requestCode == REQUEST_COLOR_PICKER) {
            selectedColor = data.getStringExtra("selectedColor");
            setBackgroundColor();
        }
    }

    //sets screen background color
    private void setBackgroundColor() {
        lnrMain.setBackgroundColor(Color.parseColor(selectedColor));
    }

    //open color picker dialog
    private void showColorPickerDialog() {
        View layout = LayoutInflater.from(this).inflate(R.layout.dialog_color_picker, null);
        final Dialog dialog = new Dialog(this, R.style.DialogTheme1);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        TextView txtCancel = (TextView) layout.findViewById(R.id.txtCancel);
        TextView txtDone = (TextView) layout.findViewById(R.id.txtDone);
        seekBarColorPicker = (SeekBar) layout.findViewById(R.id.seekBarColorPicker);
        ImageView imgColorBackground = (ImageView) layout.findViewById(R.id.imgColorBackground);
        imgColorPicker = (ImageView) layout.findViewById(R.id.imgColorPicker);
        imgColorSelected = (ImageView) layout.findViewById(R.id.imgColorSelected);

        imgColorBackground.setOnTouchListener(onTouchListener);


        setGradients();
        updateResultData(color);

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor();

                dialog.dismiss();
                color = Color.BLACK;
                mColors = new int[]{Color.BLACK, Color.WHITE};

            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                color = Color.BLACK;
                mColors = new int[]{Color.BLACK, Color.WHITE};
            }
        });

        //to handle alpha
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

        dialog.show();
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
