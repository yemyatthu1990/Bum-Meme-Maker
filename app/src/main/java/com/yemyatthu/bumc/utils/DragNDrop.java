package com.yemyatthu.bumc.utils;


import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by YMT on 10/10/2014.
            */
public class DragNDrop {
  private static int xDelta;
  private static int yDelta;
  private static TextView tv;
  private static ViewGroup root;

  public DragNDrop(){

  }

  public static void startDragNDrop(TextView v,ViewGroup viewGroup) {
      tv = v;
      root = viewGroup;
      tv.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View view, MotionEvent event) {
              final int X = (int) event.getRawX();
              final int Y = (int) event.getRawY();
              switch (event.getAction() & MotionEvent.ACTION_MASK) {
                  case MotionEvent.ACTION_DOWN:
                      RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                      xDelta = X - lParams.leftMargin;
                      yDelta = Y - lParams.topMargin;
                      break;
                  case MotionEvent.ACTION_UP:
                      break;
                  case MotionEvent.ACTION_POINTER_DOWN:
                      break;
                  case MotionEvent.ACTION_POINTER_UP:
                      break;
                  case MotionEvent.ACTION_MOVE:
                      RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                      layoutParams.leftMargin = X-xDelta;
                      layoutParams.topMargin =  Y-yDelta;
                      layoutParams.rightMargin = xDelta-X;
                      layoutParams.bottomMargin = yDelta-Y;

                      view.setLayoutParams(layoutParams);
                      break;
              }
              root.invalidate();
              return true;
          }
      });
  }




}
