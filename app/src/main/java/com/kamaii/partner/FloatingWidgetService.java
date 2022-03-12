package com.kamaii.partner;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.kamaii.partner.ui.activity.BaseActivity;


/**
 * Created by anupamchugh on 01/08/17.
 */

public class FloatingWidgetService extends Service {


    private WindowManager mWindowManager;
    private View mOverlayView;
    int mWidth;
    ImageView counterFab, mButtonClose;
    boolean activity_background, activity_close;
    private ImageView close;
    private LinearLayout layout2;
    private WindowManager windowManagerClose;
    private int screen_width;
    private int screen_height;
    private WindowManager.LayoutParams _closeParams;
    private WindowManager.LayoutParams params;
    private Animation shake;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initializeView();
        getScreenSize();

        layout2.setVisibility(View.VISIBLE);

        setTheme(R.style.AppTheme);


    }

    private static class MathUtil {
        public static boolean betweenExclusive(int x, int min, int max) {
            return x > min && x < max;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            activity_background = intent.getBooleanExtra("activity_background", false);


        }

        layout2.setVisibility(View.VISIBLE);


        if (mOverlayView == null) {

            _closeParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            _closeParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
            _closeParams.x = 0;
            _closeParams.y = 100;

            mOverlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null);
            int LAYOUT_FLAG;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                windowManagerClose.addView(layout2, _closeParams);

            } else {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
            }





            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        /*    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);*/


            //Specify the view position
            params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
            params.x = 0;
            params.y = 100;


            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            mWindowManager.addView(mOverlayView, params);
            layout2.setVisibility(View.INVISIBLE);
            close.startAnimation(shake);

            Display display = mWindowManager.getDefaultDisplay();
            final Point size = new Point();
            display.getSize(size);

            counterFab = mOverlayView.findViewById(R.id.fabHead);
            mButtonClose = (ImageView) mOverlayView.findViewById(R.id.closeButton);
            mButtonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopSelf();
                }
            });

//            if (Build.VERSION.SDK_INT >= 23) {
//                if (Settings.canDrawOverlays(FloatingWidgetService.this)) {
//
//                    if (!counterFab.isShown()) {
//                        mWindowManager.addView(counterFab, myParams);
//                        windowManagerClose.addView(layout, _closeParams);
////                        layout.setVisibility(View.INVISIBLE);
//                        close.startAnimation(shake);
//
//
//                    }
//                }
//
//
//            } else {
//
//                if (!counterFab.isShown()) {
//                    mWindowManager.addView(counterFab, myParams);
//                    windowManagerClose.addView(layout, _closeParams);
////                    layout.setVisibility(View.INVISIBLE);
//                    close.startAnimation(shake);
//
//                }
//            }
//

            final RelativeLayout layout = (RelativeLayout) mOverlayView.findViewById(R.id.layout);
            ViewTreeObserver vto = layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int width = layout.getMeasuredWidth();

                    //To get the accurate middle of the screen we subtract the width of the floating widget.
                    mWidth = size.x - width;

                }
            });


//            if (Build.VERSION.SDK_INT >= 23) {
//                if (Settings.canDrawOverlays(FloatingWidgetService.this)) {
//
//                    if (!counterFab.isShown()) {
//                        mWindowManager.addView(counterFab, params);
//                        windowManagerClose.addView(layout2, _closeParams);
//                        layout2.setVisibility(View.VISIBLE);
//                        close.startAnimation(shake);
//
//
//                    }
//                }
//
//
//            } else {
//
//                if (!counterFab.isShown()) {
//                    mWindowManager.addView(counterFab, params);
//                    windowManagerClose.addView(layout2, _closeParams);
//                    layout2.setVisibility(View.VISIBLE);
//                    close.startAnimation(shake);
//
//                }
//            }


//            Visibility();

            counterFab.setOnTouchListener(new View.OnTouchListener() {

                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;


                @Override
                public boolean onTouch(View v, MotionEvent event) {

//                    layout2.setVisibility(View.VISIBLE);


                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            //remember the initial position.

                            layout2.setVisibility(View.INVISIBLE);

                            initialX = params.x;
                            initialY = params.y;


                            //get the touch location
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();


                            break;
                        case MotionEvent.ACTION_UP:

                            layout2.setVisibility(View.INVISIBLE);

                            //Only start the activity if the application is in background. Pass the current badge_count to the activity
                            if (activity_background) {

                                float xDiff = event.getRawX() - initialTouchX;
                                float yDiff = event.getRawY() - initialTouchY;

                                if ((Math.abs(xDiff) < 5) && (Math.abs(yDiff) < 5)) {

                                    Intent intent = new Intent(FloatingWidgetService.this, BaseActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                    //close the service and remove the fab view
                                    stopSelf();
                                }



//

//                                if (FloatingWidgetService.MathUtil.betweenExclusive(params.x, -50, 50) && FloatingWidgetService.MathUtil.betweenExclusive(params.y, screen_height / 3, screen_height / 2)) {
//
////                                    Visibility();
//                                    counterFab.setVisibility(View.GONE);
//                                    stopSelf();
//
//                                }

                            }

                            if (params.y > screen_height * 0.8) {
                                counterFab.setVisibility(View.GONE);
                             /*   Toast.makeText(getApplication(), "Removed!",
                                        Toast.LENGTH_SHORT).show();*/
                                stopSelf();
                            }




//                            if (FloatingWidgetService.MathUtil.betweenExclusive(params.x, -100, 100) && !FloatingWidgetService.MathUtil.betweenExclusive(params.y, screen_height / 3, screen_height / 2)) {
//                                //moving to center range of screen
//
//
////                            animator.start(screen_width / 2, params.y);
////                                animator.start(-screen_width / 2, -((screen_height / 2) - 150));
//                                android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
//                                layoutParams.width = (int) (0.18 * screen_width);
//                                layoutParams.height = (int) (0.18 * screen_width);
//                                counterFab.setLayoutParams(layoutParams);
//                                mWindowManager.updateViewLayout(v, params);
//                                layout.setVisibility(View.INVISIBLE);
//
//
//                            } else if (FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawX(), 0, screen_width / 5)) {
//                                //move to left of screen
//
//                                if (FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawY(), 0, screen_height / 10)) {
//
//                                    // params.y = 0 ;
////                                    animator.start(-screen_width / 2, -((screen_height / 2) - 150));
//                                    android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
//                                    layoutParams.width = (int) (0.18 * screen_width);
//                                    layoutParams.height = (int) (0.18 * screen_width);
//                                    counterFab.setLayoutParams(layoutParams);
//                                    mWindowManager.updateViewLayout(mOverlayView, params);
//                                    layout.setVisibility(View.INVISIBLE);
//                                } else if (FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawY(), 9 * (screen_height / 10), screen_height)) {
////                                    animator.start(-screen_width / 2, screen_height / 2 - 150);
//                                    android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
//                                    layoutParams.width = (int) (0.18 * screen_width);
//                                    layoutParams.height = (int) (0.18 * screen_width);
//                                    counterFab.setLayoutParams(layoutParams);
//                                    mWindowManager.updateViewLayout(mOverlayView, params);
//                                    layout.setVisibility(View.INVISIBLE);
//                                } else {
////                                    animator.start(-screen_width / 2, params.y);
//                                    android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
//                                    layoutParams.width = (int) (0.18 * screen_width);
//                                    layoutParams.height = (int) (0.18 * screen_width);
//                                    counterFab.setLayoutParams(layoutParams);
//                                    mWindowManager.updateViewLayout(mOverlayView, params);
//                                    layout.setVisibility(View.INVISIBLE);
//                                }
//
//                            } else if (FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawX(), screen_width - (screen_width / 5), screen_width)) {
//                                //move to right of screen
//
//                                if (FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawY(), 0, screen_height / 10)) {
//
//                                    // params.y = 0 ;
////                                    animator.start(screen_width / 2, -((screen_height / 2) - 150));
//                                    android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
//                                    layoutParams.width = (int) (0.18 * screen_width);
//                                    layoutParams.height = (int) (0.18 * screen_width);
//                                    counterFab.setLayoutParams(layoutParams);
//                                    mWindowManager.updateViewLayout(mOverlayView, params);
//                                    layout.setVisibility(View.INVISIBLE);
//                                } else if (FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawY(), 9 * (screen_height / 10), screen_height)) {
////                                    animator.start(screen_width / 2, screen_height / 2 - 150);
//                                    android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
//                                    layoutParams.width = (int) (0.18 * screen_width);
//                                    layoutParams.height = (int) (0.18 * screen_width);
//                                    counterFab.setLayoutParams(layoutParams);
//                                    mWindowManager.updateViewLayout(mOverlayView, params);
//                                    layout.setVisibility(View.INVISIBLE);
//                                } else {
////                                    animator.start(screen_width / 2, params.y);
//                                    android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
//                                    layoutParams.width = (int) (0.18 * screen_width);
//                                    layoutParams.height = (int) (0.18 * screen_width);
//                                    counterFab.setLayoutParams(layoutParams);
//                                    mWindowManager.updateViewLayout(mOverlayView, params);
//                                    layout.setVisibility(View.INVISIBLE);
//                                }
//
//                            } else if (FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawX(), screen_width / 5, 2 * (screen_width / 5))) {
//                                //move to left of screen
//                                if (FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawY(), 0, screen_height / 10)) {
//
//
////                                    animator.start(-screen_width / 2, -((screen_height / 2) - 150));
//                                    android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
//                                    layoutParams.width = (int) (0.18 * screen_width);
//                                    layoutParams.height = (int) (0.18 * screen_width);
//                                    counterFab.setLayoutParams(layoutParams);
//                                    mWindowManager.updateViewLayout(mOverlayView, params);
//                                    layout.setVisibility(View.INVISIBLE);
//                                } else if (FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawY(), 9 * (screen_height / 10), screen_height)) {
////                                    animator.start(-screen_width / 2, screen_height / 2 - 150);
//                                    android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
//                                    layoutParams.width = (int) (0.18 * screen_width);
//                                    layoutParams.height = (int) (0.18 * screen_width);
//                                    counterFab.setLayoutParams(layoutParams);
//                                    mWindowManager.updateViewLayout(mOverlayView, params);
//                                    layout.setVisibility(View.INVISIBLE);
//                                } else {
////                                    animator.start(-screen_width / 2, params.y);
//                                    android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
//                                    layoutParams.width = (int) (0.18 * screen_width);
//                                    layoutParams.height = (int) (0.18 * screen_width);
//                                    counterFab.setLayoutParams(layoutParams);
//                                    mWindowManager.updateViewLayout(mOverlayView, params);
//                                    layout.setVisibility(View.INVISIBLE);
//                                }
//                            } else if (FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawX(), 3 * (screen_width / 5), screen_width)) {
//                                //move to right of screen
//                                if (FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawY(), 0, screen_height / 10)) {
//
//                                    // params.y = 0 ;
////                                    animator.start(screen_width / 2, -((screen_height / 2) - 150));
//                                    android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
//                                    layoutParams.width = (int) (0.18 * screen_width);
//                                    layoutParams.height = (int) (0.18 * screen_width);
//                                    counterFab.setLayoutParams(layoutParams);
//                                    mWindowManager.updateViewLayout(mOverlayView, params);
//                                    layout.setVisibility(View.INVISIBLE);
//                                } else if (FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawY(), 9 * (screen_height / 10), screen_height)) {
////                                    animator.start(screen_width / 2, screen_height / 2 - 150);
//                                    android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
//                                    layoutParams.width = (int) (0.18 * screen_width);
//                                    layoutParams.height = (int) (0.18 * screen_width);
//                                    counterFab.setLayoutParams(layoutParams);
//                                    mWindowManager.updateViewLayout(mOverlayView, params);
//                                    layout.setVisibility(View.INVISIBLE);
//                                } else {
////                                    animator.start(screen_width / 2, params.y);
//                                    android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
//                                    layoutParams.width = (int) (0.18 * screen_width);
//                                    layoutParams.height = (int) (0.18 * screen_width);
//                                    counterFab.setLayoutParams(layoutParams);
//                                    mWindowManager.updateViewLayout(mOverlayView, params);
//                                    layout.setVisibility(View.INVISIBLE);
//                                }
//                            } else if (FloatingWidgetService.MathUtil.betweenExclusive(params.x, -50, 50) && FloatingWidgetService.MathUtil.betweenExclusive(params.y, screen_height / 3, screen_height / 2)) {
//
////                                Visibility();
//                                stopSelf();
//
//
//                            }
////                            else {
////
////                                //not in either of the above cases
//////                                animator.start(screen_width / 2, params.y);
////                                android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
////                                layoutParams.width = (int) (0.18 * screen_width);
////                                layoutParams.height = (int) (0.18 * screen_width);
////                                counterFab.setLayoutParams(layoutParams);
//////                                mWindowManager.updateViewLayout(v, params);
////                                layout.setVisibility(View.INVISIBLE);
////
////                            }
//

//                            Logic to auto-position the widget based on where it is positioned currently w.r.t middle of the screen.
                            int middle = mWidth / 2;
                            float nearestXWall = params.x >= middle ? mWidth : 0;
                            params.x = (int) nearestXWall;
//
//
                            mWindowManager.updateViewLayout(mOverlayView, params);


                            break;
                        case MotionEvent.ACTION_MOVE:

                            layout2.setVisibility(View.VISIBLE);


                            int xDiff = Math.round(event.getRawX() - initialTouchX);
                            int yDiff = Math.round(event.getRawY() - initialTouchY);


                            //Calculate the X and Y coordinates of the view.
                            params.x = initialX + xDiff;
                            params.y = initialY + yDiff;

                            //Update the layout with new X & Y coordinates
                            mWindowManager.updateViewLayout(mOverlayView, params);
//
//
//                            return true;


//                            layout.setVisibility(View.VISIBLE);
//                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
//                            params.y = initialY + (int) (event.getRawY() - initialTouchY);
//                            windowManager.updateViewLayout(mOverlayView, params);
//                            if (FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawX(), 0, screen_width / 5) || FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawX(), screen_width - (screen_width / 5), screen_width)) {
//                                android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
//                                layoutParams.width = (int) (0.18 * screen_width);
//                                layoutParams.height = (int) (0.18 * screen_width);
//                                counterFab.setLayoutParams(layoutParams);
//                                windowManager.updateViewLayout(mOverlayView, params);
//                            } else if (FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawX(), 2 * (screen_width / 5), 3 * (screen_width / 5))) {
//                                android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
//                                layoutParams.width = (int) (0.18 * screen_width) ;
//                                layoutParams.height = (int) (0.18 * screen_width);
//                                counterFab.setLayoutParams(layoutParams);
//                                windowManager.updateViewLayout(mOverlayView, params);
//                            } else if (FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawX(), screen_width / 5, 2 * (screen_width / 5)) || FloatingWidgetService.MathUtil.betweenExclusive((int) event.getRawX(), 3 * (screen_width / 5), screen_width)) {
//                                android.view.ViewGroup.LayoutParams layoutParams = counterFab.getLayoutParams();
//                                layoutParams.width = (int) (0.18 * screen_width);
//                                layoutParams.height = (int) (0.18 * screen_width);
//                                counterFab.setLayoutParams(layoutParams);
//                                windowManager.updateViewLayout(mOverlayView, params);
//                            }

                            break;


                    }
                    return true;
                }
            });

            counterFab.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mButtonClose.setVisibility(View.VISIBLE);
                    return false;
                }
            });
        } else {


        }


        return super.onStartCommand(intent, flags, startId);


    }

    private void Visibility() {
        if (mWindowManager != null) {
            mWindowManager.removeViewImmediate(counterFab);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                windowManagerClose.removeViewImmediate(layout2);
            }
        }
    }

    private void getScreenSize() {
        Display display = mWindowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screen_width = size.x;
        screen_height = size.y;

    }

    private void initializeView() {
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManagerClose = (WindowManager) getSystemService(WINDOW_SERVICE);
        close = new ImageView(this);
        close.setImageResource(R.mipmap._close);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wiggle);
        shake.setRepeatCount(Animation.INFINITE);
        layout2 = new LinearLayout(this);
        layout2.addView(close);
    }

//    private void notifyFragment(String json) {
//        Intent intent = new Intent("nameOfTheActionShouldBeUniqueName");
//        Bundle bundle = new Bundle();
//        bundle.putString("json", json);
//        intent.putExtras(bundle);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//    }

//    @Override
//    public boolean stopService(Intent name) {
//        counterFab.setVisibility(View.GONE);
//        return super.stopService(name);
//
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOverlayView != null)
            mWindowManager.removeView(mOverlayView);
    }


}
