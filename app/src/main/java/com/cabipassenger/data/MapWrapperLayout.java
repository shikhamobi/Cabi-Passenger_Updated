package com.cabipassenger.data;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.RelativeLayout;

import com.cabipassenger.fragments.BookTaxiNewFrag;

import com.google.android.gms.maps.GoogleMap;

import android.view.GestureDetector;
import android.view.ScaleGestureDetector;

import com.google.android.gms.maps.CameraUpdateFactory;

/**
 * This class is used to get the MAP functionality is dragged,is touched,is moved.
 */

//
public class MapWrapperLayout extends RelativeLayout {
    private static final String DEBUG_TAG = "";
    private int fingers = 0;
    private GoogleMap googleMap;
    private long lastZoomTime = 0;
    private float lastSpan = -1;
    private static boolean bookingPage;
    private int bottomOffsetPixels;
    private Handler handler = new Handler();
    private final Handler handler1 = new Handler();
    public static boolean moved;

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private VelocityTracker mVelocityTracker;
    private int pointerId;
    private BottomSheetBehavior<View> carLay;

    public MapWrapperLayout(Context context) {
        super(context);
    }

    public MapWrapperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MapWrapperLayout(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }

    //    public void carLayVisible(boolean isVisible){
//        if(carLay!=null)
//        if(!isVisible)
//
//                carLay.setVisibility(GONE);
//            else
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(carLay!=null)
//                            carLay.setVisibility(VISIBLE);
//                    }
//                },1000);
//
//    }
    public void init(GoogleMap map, int bottomOffsetPixels, boolean bookingpage, BottomSheetBehavior<View> carlayout) {
        this.bookingPage = bookingpage;
        carLay = carlayout;
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (lastSpan == -1) {
                    lastSpan = detector.getCurrentSpan();
                } else if (detector.getEventTime() - lastZoomTime >= 50) {
                    lastZoomTime = detector.getEventTime();
                    googleMap.animateCamera(CameraUpdateFactory.zoomBy(getZoomValue(detector.getCurrentSpan(), lastSpan)), 50, null);
                    lastSpan = detector.getCurrentSpan();
                }
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                lastSpan = -1;
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                lastSpan = -1;

            }
        });
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                disableScrolling();
                googleMap.animateCamera(CameraUpdateFactory.zoomIn(), 400, null);
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        googleMap = map;
    }

    private float getZoomValue(float currentSpan, float lastSpan) {
        double value = (Math.log(currentSpan / lastSpan) / Math.log(1.55d));
        return (float) value;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            gestureDetector.onTouchEvent(ev);
            pointerId = ev.getPointerId(ev.getActionIndex());
            switch (ev.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    fingers = fingers + 1;
                    googleMap.animateCamera(CameraUpdateFactory.zoomOut(), 400, null);
                    //  carLayVisible(false);
                    break;
                case MotionEvent.ACTION_POINTER_UP:

                    fingers = fingers - 1;
                    //  carLayVisible(true);
                    break;
                case MotionEvent.ACTION_UP:
                    fingers = 0;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            moved = false;
                        }
                    }, 3000);

                    // carLay.setVisibility(VISIBLE);
                    // carLayVisible(true);
                    if (bookingPage) {
                        try {
                            BookTaxiNewFrag.z = 1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case MotionEvent.ACTION_DOWN:

                    // carLay.setPeekHeight(dpToPx(110));
                    if (carLay != null)
                        carLay.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    //  carLayVisible(true);
                    //   if(carLay.getVisibility()!= View.GONE)
                    //   carLay.setVisibility(GONE);
                    fingers = 1;

                    if (mVelocityTracker == null) {
                        // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                        mVelocityTracker = VelocityTracker.obtain();
                    } else {
                        // Reset the velocity tracker back to its initial state.
                        mVelocityTracker.clear();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    moved = true;
                    if (mVelocityTracker == null) {
                        // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                        mVelocityTracker = VelocityTracker.obtain();
                    }
                    mVelocityTracker.addMovement(ev);
                    mVelocityTracker.computeCurrentVelocity(100);

                    float velocityx = VelocityTrackerCompat.getXVelocity(mVelocityTracker,
                            pointerId);
                    float velocityy = VelocityTrackerCompat.getYVelocity(mVelocityTracker,
                            pointerId);
                    //   setmMapIsTouched(true);
                    if (bookingPage & fingers == 1) {

                        if (Math.abs(velocityx) > 80 || Math.abs(velocityy) > 80)
                            BookTaxiNewFrag.setfetch_address();
                        BookTaxiNewFrag.z = 0;

                    }
                    break;

            }


            if (fingers > 1) {
                disableScrolling();
            } else if (fingers < 1) {
                enableScrolling();
            }
            if (fingers > 1) {
                return scaleGestureDetector.onTouchEvent(ev);
            } else {
                return super.dispatchTouchEvent(ev);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void enableScrolling() {
        if (googleMap != null && !googleMap.getUiSettings().isScrollGesturesEnabled()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    googleMap.getUiSettings().setAllGesturesEnabled(true);
                }
            }, 50);
        }
    }

    private void disableScrolling() {
        handler.removeCallbacksAndMessages(null);
        if (googleMap != null && googleMap.getUiSettings().isScrollGesturesEnabled()) {
            googleMap.getUiSettings().setAllGesturesEnabled(false);
        }
    }


}