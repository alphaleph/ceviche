package hu.ait.android.cevicheteam.ceviche.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import hu.ait.android.cevicheteam.ceviche.R;
import hu.ait.android.cevicheteam.ceviche.SwipeActivity;
import in.championswimmer.sfg.lib.SimpleFingerGestures;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import pro.alexzaitsev.freepager.library.view.infinite.Constants;
import pro.alexzaitsev.freepager.library.view.infinite.InfiniteHorizontalPagerAdapter;
import pro.alexzaitsev.freepager.library.view.infinite.ViewFactory;

public class SwipeFragment extends android.support.v4.app.Fragment implements ViewFactory {

    public static final String TAG = "Swipe_Fragment";

    private SimpleFingerGestures mySfg = new SimpleFingerGestures();
    private Transformation transformation = new RoundedCornersTransformation(100, 0);

    private Handler handler;
    private Runnable swipe;
    private ViewPager verticalPager;
    private SwipeActivity swipeActivity;

    private ImageView ivFood;
    private ImageView oldFoodView;
    private ImageView oldestFoodView;

    private String Url;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        handler = new Handler(Looper.getMainLooper());

        swipeActivity = ((SwipeActivity) getActivity());
        verticalPager = (ViewPager) inflater.inflate(R.layout.fragment_swipe, container, false);
        verticalPager.setAdapter(new InfiniteHorizontalPagerAdapter(this, 0));
        verticalPager.setCurrentItem(Constants.START_INDEX);
        verticalPager.setOnTouchListener(mySfg);

        getUrl();
        setFingerGestureListener();

        return verticalPager;
    }

    private void setFingerGestureListener() {
        mySfg.setOnFingerGestureListener(new SimpleFingerGestures.OnFingerGestureListener() {
            @Override
            public boolean onSwipeUp(int fingers, long gestureDuration) {
                if (oldestFoodView != null) {
                    oldestFoodView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.move_up));
                }
                setScrollRunnable();
                savePhotoAsFavorite();
                return false;
            }

            @Override
            public boolean onSwipeDown(int fingers, long gestureDuration) {
                if (oldestFoodView != null) {
                    oldestFoodView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.move_down));
                }
                setScrollRunnable();
                Toast.makeText(swipeActivity, "Picture Deleted", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onSwipeLeft(int fingers, long gestureDuration) {
                return false;
            }

            @Override
            public boolean onSwipeRight(int fingers, long gestureDuration) {
                return false;
            }

            @Override
            public boolean onPinch(int fingers, long gestureDuration) {
                return false;
            }

            @Override
            public boolean onUnpinch(int fingers, long gestureDuration) {
                return false;
            }
        });
    }

    private void savePhotoAsFavorite() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            currentUser.addUnique(swipeActivity.getString(R.string.parse_favorite_photos), Url);
            currentUser.saveEventually();
            Toast.makeText(swipeActivity, swipeActivity.getString(R.string.favorites_photo_saved), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(swipeActivity, swipeActivity.getString(R.string.favorites_photo_not_saved), Toast.LENGTH_SHORT).show();
        }
    }

    private void getUrl() {
        Url = swipeActivity.getIntent().getStringExtra("URL");
    }

    @Override
    public View makeView(int vertical, int horizontal) {
        this.oldestFoodView = this.oldFoodView;
        this.oldFoodView = this.ivFood;


        //todo changed this for class
        this.ivFood = new ImageView(swipeActivity);
        Picasso.with(swipeActivity).load(Url).resize(800, 800).transform(transformation).centerInside().into(ivFood);
        return ivFood;
    }

    private void setScrollRunnable() {
        swipe = new Runnable() {
            @Override
            public void run() {
                int next = verticalPager.getCurrentItem() + 1;
                verticalPager.setCurrentItem(next);
                oldestFoodView = oldFoodView;
            }
        };
        handler.postDelayed(swipe, 700);
    }

}
