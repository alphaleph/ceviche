package hu.ait.android.cevicheteam.ceviche;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class EditProfileActivity extends AppCompatActivity {

    public static final String PARSE_NAME = "name";
    public static final int PARSE_LOGIN_CODE = 999;
    public static final String PARSE_EMAIL = "email";
    private ParseUser currentUser;

    @InjectView(R.id.tvProfileTitle)
    TextView tvProfileTitle;
    @InjectView(R.id.tvProfileSubtitle)
    TextView tvProfileSubtitle;
    @InjectView(R.id.etProfileName)
    TextView etProfileName;
    @InjectView(R.id.etProfileEmail)
    TextView etProfileEmail;
    @InjectView(R.id.container_profile_info)
    LinearLayout containerProfileInfo;
    @InjectView(R.id.btnLogInLogOut)
    Button btnLogInLogOut;
    @InjectView(R.id.btnProfileSubmit)
    Button btnProfileSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.inject(this);

        prepareLogInLogOutButton();
        prepareProfileSubmitButton();
    }

    private void prepareProfileSubmitButton() {
        btnProfileSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean edited = false;
                edited = hasEditedName(edited);
                edited = hasEditedEmail(edited);
                if (edited) {
                    showMessage(getString(R.string.submit_profile_edits));
                    currentUser.saveEventually();
                }
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private Boolean hasEditedEmail(Boolean edited) {
        String editEmail = etProfileEmail.getText().toString();
        if (!editEmail.equals(currentUser.getEmail())) {
            currentUser.put(PARSE_EMAIL, editEmail);
            edited = true;
        }
        return edited;
    }

    private Boolean hasEditedName(Boolean edited) {
        String editName = etProfileName.getText().toString();
        if (!editName.equals(currentUser.getString(PARSE_NAME))) {
            currentUser.put(PARSE_NAME, editName);
            tvProfileTitle.setText(getString(R.string.profile_title) + editName);
            edited = true;
        }
        return edited;
    }

    private void prepareLogInLogOutButton() {
        btnLogInLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    startParseLogInActivity();
                } else {
                    logOutUser();
                }
            }
        });
    }

    private void logOutUser() {
        ParseUser.logOut();
        currentUser = null;
        showProfileLoggedOut();
    }

    private void startParseLogInActivity() {
        ParseLoginBuilder builder = new ParseLoginBuilder(EditProfileActivity.this);
        Intent parseLoginIntent = builder.build();
        //TODO: Make Shrimpy smaller for LoginScreen
        //Intent parseLoginIntent = builder.setAppLogo(R.drawable.shrimpy).build();
        //TODO: Don't start unless you have internet connection.
        startActivityForResult(parseLoginIntent, PARSE_LOGIN_CODE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            showProfileLoggedOut();
        } else {
            showProfileLoggedIn();
        }
    }

    private void showProfileLoggedIn() {
        String name = currentUser.getString(PARSE_NAME);
        if (name != null) {
            tvProfileTitle.setText(getString(R.string.profile_title) + name);
        } else {
            tvProfileTitle.setText(getString(R.string.profile_title) + currentUser.getUsername());
        }
        tvProfileSubtitle.setText(getString(R.string.profile_subtitle_logged_in));
        btnLogInLogOut.setText(getString(R.string.btn_profile_logout));
        containerProfileInfo.setVisibility(View.VISIBLE);
        btnProfileSubmit.setVisibility(View.VISIBLE);
        etProfileEmail.setText(currentUser.getEmail());
        etProfileName.setText(currentUser.getString(PARSE_NAME));
    }

    private void showProfileLoggedOut() {
        tvProfileTitle.setText(getString(R.string.profile_title) + getString(R.string.profile_guest_name));
        tvProfileSubtitle.setText(getString(R.string.profile_subtitle_logged_out));
        btnLogInLogOut.setText(getString(R.string.btn_profile_login_signup));
        containerProfileInfo.setVisibility(View.GONE);
        btnProfileSubmit.setVisibility(View.GONE);
    }
}
