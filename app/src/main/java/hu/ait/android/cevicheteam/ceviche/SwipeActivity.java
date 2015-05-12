package hu.ait.android.cevicheteam.ceviche;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;


public class SwipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();

        setContentView(R.layout.activity_swipe);
        showFragment(SwipeFragment.TAG);
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void showFragment(String fragmentTag) {
        if (SwipeFragment.TAG.equals(fragmentTag)) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            SwipeFragment swipeFragment = new SwipeFragment();
            fragmentTransaction.replace(R.id.layoutContainer, swipeFragment, SwipeFragment.TAG);
            fragmentTransaction.commit();
        }
    }
}
