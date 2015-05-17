package hu.ait.android.cevicheteam.ceviche;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hu.ait.android.cevicheteam.ceviche.Fragments.EditProfileFragment;


public class EditProfileActivity extends AppCompatActivity {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fragmentManager = getSupportFragmentManager();
        showFragment(EditProfileFragment.TAG);
    }

    private void showFragment(String tag) {
        Fragment fragment = null;
        fragment = fragmentManager.findFragmentByTag(tag);

        if (fragment == null) {
            if (EditProfileFragment.TAG.equals(tag)) {
                fragment = new EditProfileFragment();
            }
        }
        if (fragment != null) {
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.layoutContainer, fragment, tag);
            transaction.commit();
        }
    }
}
