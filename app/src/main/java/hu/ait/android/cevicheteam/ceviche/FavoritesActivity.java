package hu.ait.android.cevicheteam.ceviche;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import hu.ait.android.cevicheteam.ceviche.Fragments.FavoritesFragment;


public class FavoritesActivity extends AppCompatActivity {

    private FragmentManager fm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        fm = getSupportFragmentManager();
        showFragment(FavoritesFragment.TAG);
    }

    private void showFragment(String tag) {
        Fragment fragment = null;
        fragment = fm.findFragmentByTag(tag);

        if (fragment == null) {
            if (FavoritesFragment.TAG.equals(tag)) {
                fragment = new FavoritesFragment();
            }
        }

        if (fragment != null) {
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.layoutContainer, fragment, tag);
            transaction.commit();
        }
    }
}
