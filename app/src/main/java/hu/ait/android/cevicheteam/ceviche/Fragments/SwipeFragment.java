package hu.ait.android.cevicheteam.ceviche.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.Header;

import java.util.ArrayList;

import hu.ait.android.cevicheteam.ceviche.R;
import hu.ait.android.cevicheteam.ceviche.SwipeActivity;
import in.championswimmer.sfg.lib.SimpleFingerGestures;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import pro.alexzaitsev.freepager.library.view.infinite.Constants;
import pro.alexzaitsev.freepager.library.view.infinite.InfiniteHorizontalPagerAdapter;
import pro.alexzaitsev.freepager.library.view.infinite.ViewFactory;

public class SwipeFragment extends android.support.v4.app.Fragment implements ViewFactory {

    public static final String TAG = "Swipe_Fragment";

    private String CX_KEY = "006595710349057423305:js3hz-kiofe";
    private String API_KEY = "AIzaSyBrlBeP70dgFnvl2zddqtRfEkmFzm6WfJY";

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
    private String Meta;
    private ArrayList<String> all_urls = new ArrayList<>();

    private int curr = 0;

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
        getMetaData();
        setAllUrls(1);
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
        all_urls.add(Url);
        String Url1 = swipeActivity.getIntent().getStringExtra("URL1");
        all_urls.add(Url1);
        String Url2 = swipeActivity.getIntent().getStringExtra("URL2");
        all_urls.add(Url2);
    }

    private void getMetaData() {Meta = swipeActivity.getIntent().getStringExtra("META");}

    @Override
    public View makeView(int vertical, int horizontal) {
        this.ivFood = new ImageView(swipeActivity);
        Picasso.with(swipeActivity).load(all_urls.get(curr)).resize(800, 800).transform(transformation).centerInside().into(ivFood);
        curr = curr + 1;

        if (curr % 10 == 0 && curr != 0) {
            setAllUrls(curr);
        }

        this.oldestFoodView = this.oldFoodView;
        this.oldFoodView = this.ivFood;
        return ivFood;
    }

    private void setScrollRunnable() {
        swipe = new Runnable() {
            @Override
            public void run() {
                int next = verticalPager.getCurrentItem() + 1;
                verticalPager.setCurrentItem(next);
            }
        };
        handler.postDelayed(swipe, 700);
    }

    private void setAllUrls(int start) {
        String s = Integer.toString(start);
        String imgQuery = "https://www.googleapis.com/customsearch/v1?q=" +
                Meta + "&cx=" + CX_KEY + "&searchType=image&num=10&start=" + s + "&key=" + API_KEY;
        new AsyncHttpClient().get(imgQuery, new AsyncHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String imgJson = new String(response);

                Log.d("Order", "Search Successful");
                for (int i = 1; i < imgJson.split("link\": \"").length; i++) {
                    all_urls.add(imgJson.split("link\": \"")[i].split("\"")[0]);
                }

            }

            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d("Response", "Failure");
            }
        });

    }
}
