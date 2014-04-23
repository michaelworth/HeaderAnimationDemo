package com.example.headeranimationdemo.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    FrameLayout headerView;
    ListView listView;
    long lastAnimationUpdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);

        headerView = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.header_layout, listView, false);
        headerView.findViewById(R.id.v1_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideHeaderV1();
            }
        });

        headerView.findViewById(R.id.v2_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideHeaderV2();
            }
        });

        listView.addHeaderView(headerView);

        listView.setAdapter(new ArrayAdapter<>(this, R.layout.row_item_layout,
                new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
    }

    private void reset() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void hideHeaderV1() {
        final int height = headerView.getMeasuredHeight();
        final AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) headerView.getLayoutParams();

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(400);

        Animation translationAnimation = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                layoutParams.height = (int) (height * (1 - interpolatedTime));
                headerView.setLayoutParams(layoutParams);

                lastAnimationUpdateTime = System.nanoTime();
                //Log.i(TAG, "layoutParams: height= " + layoutParams.height);
                //Log.i(TAG, "AnimationUpdate @ " + lastAnimationUpdateTime);
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        translationAnimation.setDuration(400);
        translationAnimation.setStartOffset(300);

        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addAnimation(alphaAnimation);
        set.addAnimation(translationAnimation);
        set.setFillAfter(true);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Hold onto the update time in case it is still being modified
                long animationUpdate = lastAnimationUpdateTime;
                long animationEnd = System.nanoTime();
                Log.i(TAG, "LastUpdate @ " + animationEnd);
                Log.i(TAG, "Delta: " + (animationEnd - animationUpdate));

                listView.removeHeaderView(headerView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        headerView.startAnimation(set);
    }

    private void hideHeaderV2() {
        final int height = headerView.getMeasuredHeight();
        final AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) headerView.getLayoutParams();

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(headerView, "alpha", 0f).setDuration(300);

        ValueAnimator heightAnimator = ValueAnimator.ofInt(height, 0).setDuration(400);
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
                headerView.setLayoutParams(layoutParams);
                lastAnimationUpdateTime = System.nanoTime();
                //Log.i(TAG, "layoutParams: height= " + layoutParams.height);
                //Log.i(TAG, "AnimationUpdate @ " + lastAnimationUpdateTime);
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(alphaAnimator, heightAnimator);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                // Hold onto the update time in case it is still being modified
                long animationUpdate = lastAnimationUpdateTime;
                long animationEnd = System.nanoTime();
                Log.i(TAG, "LastUpdate @ " + animationEnd);
                Log.i(TAG, "Delta: " + (animationEnd - animationUpdate));

                listView.removeHeaderView(headerView);
            }
        });

        set.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_reset) {
            reset();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
