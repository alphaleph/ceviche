package hu.ait.android.cevicheteam.ceviche;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import pro.alexzaitsev.freepager.library.view.infinite.Constants;
import pro.alexzaitsev.freepager.library.view.infinite.InfiniteHorizontalPager;
import pro.alexzaitsev.freepager.library.view.infinite.InfiniteVerticalPager;
import pro.alexzaitsev.freepager.library.view.infinite.ViewFactory;

public class SwipeFragment extends android.support.v4.app.Fragment implements ViewPager.OnPageChangeListener{

    public static final String TAG = "Swipe_Fragment";

    //private ImageView ivFood;
    private SwipeActivity swipeActivity;

    Transformation transformation = new RoundedCornersTransformation(100, 0);


    private ViewGroup mView;
    private int mCurrentHorizontalIndex = 0;
    private int mBgColor;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        //final View rootView = inflater.inflate(R.layout.fragment_swipe, container, false);
        //ivFood = (ImageView) rootView.findViewById(R.id.imageView);
        swipeActivity = ((SwipeActivity) getActivity());

        //createImageView();

        mBgColor = getResources().getColor(R.color.purple);
        InfiniteVerticalPager verticalPager = (InfiniteVerticalPager) inflater
                .inflate(R.layout.fragment_swipe, container, false);
        verticalPager.setFactory(mVerticalViewFactory);
        verticalPager.instantiate();
        return mView = verticalPager;
    }

    /*private void createImageView() {
        String Url = swipeActivity.getIntent().getStringExtra("URL");
        Picasso.with(swipeActivity).load(Url).resize(800, 800).transform(transformation).centerInside().into(ivFood);
    }*/

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Do nothing
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentHorizontalIndex = position - Constants.START_INDEX;

        ViewPager pagerTop = (ViewPager) mView.getChildAt(Constants.TOP_PAGE_INDEX);
        ViewPager pagerBottom = (ViewPager) mView.getChildAt(Constants.BOTTOM_PAGE_INDEX);
        synchronizePager(pagerTop, position);
        synchronizePager(pagerBottom, position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // Do nothing
    }


    private ViewFactory mVerticalViewFactory = new ViewFactory() {

        static final int VIEWS_TO_INITIALIZE_COUNT = 3;

        private int mViewsToInitilizeLeft = VIEWS_TO_INITIALIZE_COUNT;

        @Override
        public View makeView(int vertical, int horizontal) {
            int normalizedHorizontal = 0;
            if (mViewsToInitilizeLeft > 0) {
                mViewsToInitilizeLeft--;
            } else {
                normalizedHorizontal = mCurrentHorizontalIndex;
            }
            InfiniteHorizontalPager pager = new InfiniteHorizontalPager(
                    getActivity(), vertical, normalizedHorizontal,
                    mHorizontalViewFactory);
            pager.getPager().setOnPageChangeListener(SwipeFragment.this);
            return pager.getPager();
        }
    };

    private ViewFactory mHorizontalViewFactory = new ViewFactory() {

        @Override
        public View makeView(int vertical, int horizontal) {
            Button btn = new Button(getActivity());
            btn.setText("Horizontal " + horizontal + "\nVertical " + vertical);
            btn.setBackgroundColor(mBgColor);
            return btn;
        }
    };

    private void synchronizePager(ViewPager pager, int i) {
        pager.setOnPageChangeListener(null);
        pager.setCurrentItem(i);
        pager.setOnPageChangeListener(SwipeFragment.this);
    }

}
