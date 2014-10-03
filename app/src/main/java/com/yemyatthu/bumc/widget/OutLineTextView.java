package com.yemyatthu.bumc.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Locale;

public class OutLineTextView extends TextView {


    // constructors
    public OutLineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public OutLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OutLineTextView(Context context) {
        super(context);
    }


    // overridden methods
    @Override
    public void draw(Canvas canvas) {

        for (int i = 0; i < 8; i++) {
            super.draw(canvas);
        }

    }



}

