package hu.ait.android.cevicheteam.ceviche.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hu.ait.android.cevicheteam.ceviche.R;


/**
 * Created by Chau on 5/14/2015.
 */
public class EditProfileFragment extends Fragment {

    public static final String TAG = "Edit_Profile_Fragment";
    public static final String PARSE_NAME = "name";
    public static final int PARSE_LOGIN_CODE = 999;
    public static final String PARSE_EMAIL = "email";
    private ParseUser currentUser;

    @InjectView(R.id.tvProfileTitle)
    TextView tvProfileTitle;
    @InjectView(R.id.tvProfileSubtitle)
    TextView tvProfileSubtitle;
    @InjectView(R.id.etProfileName)
    EditText etProfileName;
    @InjectView(R.id.etProfileEmail)
    EditText etProfileEmail;
    @InjectView(R.id.container_profile_info)
    LinearLayout containerProfileInfo;
    @InjectView(R.id.btnLogInLogOut)
    Button btnLogInLogOut;
    @InjectView(R.id.btnProfileSubmit)
    Button btnProfileSubmit;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, null);
        ButterKnife.inject(this, rootView);
        prepareLogInLogOutButton();
        prepareProfileSubmitButton();
        prepareNameEditText();
        prepareEmailEditText();
        return rootView;
    }

    private void prepareEmailEditText() {
        etProfileEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(currentUser.getEmail())) {
                    btnProfileSubmit.setEnabled(true);
                } else {
                    btnProfileSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void prepareNameEditText() {
        etProfileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(currentUser.getString(PARSE_NAME))) {
                    btnProfileSubmit.setEnabled(true);
                } else {
                    btnProfileSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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
                btnProfileSubmit.setEnabled(false);
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
        ParseLoginBuilder builder = new ParseLoginBuilder(getActivity());
        Intent parseLoginIntent = builder.build();
        //TODO: Don't start unless you have internet connection.
        startActivityForResult(parseLoginIntent, PARSE_LOGIN_CODE);
    }

    @Override
    public void onStart() {
        super.onStart();

        currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            showProfileLoggedOut();
        } else {
            showProfileLoggedIn();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        saveNameEmailEditText();
    }

    private void showProfileLoggedIn() {
        String name = currentUser.getString(PARSE_NAME);
        if (name != null) {
            tvProfileTitle.setText(getString(R.string.profile_title) + name);
        } else {
            tvProfileTitle.setText(getString(R.string.profile_title) + getString(R.string.profile_guest_name));
        }
        resetNameEmailEditText();
        tvProfileSubtitle.setText(getString(R.string.profile_subtitle_logged_in));
        btnLogInLogOut.setText(getString(R.string.btn_profile_logout));
        containerProfileInfo.setVisibility(View.VISIBLE);
        btnProfileSubmit.setVisibility(View.VISIBLE);
        fillNameEmailEditText();
        btnProfileSubmit.setEnabled(false);
    }

    private void resetNameEmailEditText() {
        SharedPreferences sp = getActivity().getSharedPreferences(
                getString(R.string.PREF_PROFILE_EDIT_TEXT_CONTENT), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(getString(R.string.KEY_PROFILE_ET_NAME), "");
        editor.putString(getString(R.string.KEY_PROFILE_ET_EMAIL), "");
        editor.commit();
    }

    private void saveNameEmailEditText() {
        SharedPreferences sp = getActivity().getSharedPreferences(
                getString(R.string.PREF_PROFILE_EDIT_TEXT_CONTENT), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(getString(R.string.KEY_PROFILE_ET_NAME), etProfileName.getText().toString());
        editor.putString(getString(R.string.KEY_PROFILE_ET_EMAIL), etProfileEmail.getText().toString());
        editor.commit();
    }

    private void fillNameEmailEditText() {
        SharedPreferences sp = getActivity().getSharedPreferences(
                getString(R.string.PREF_PROFILE_EDIT_TEXT_CONTENT), Context.MODE_PRIVATE);
        String prefName = sp.getString(getString(R.string.KEY_PROFILE_ET_NAME), "");
        String prefEmail = sp.getString(getString(R.string.KEY_PROFILE_ET_EMAIL), "");

        Log.d("ROCK", "PrefName: " + prefName + " PrefEmail: " + prefEmail);

        if ("".equals(prefName)) {
            etProfileName.setText(currentUser.getString(PARSE_NAME));
        } else {
            etProfileName.setText(prefName);
        }

        if ("".equals(prefEmail)) {
            etProfileEmail.setText(currentUser.getEmail());
        } else {
            etProfileEmail.setText(prefEmail);
        }
    }

    private void showProfileLoggedOut() {
        tvProfileTitle.setText(getString(R.string.profile_title) + getString(R.string.profile_guest_name));
        tvProfileSubtitle.setText(getString(R.string.profile_subtitle_logged_out));
        btnLogInLogOut.setText(getString(R.string.btn_profile_login_signup));
        containerProfileInfo.setVisibility(View.GONE);
        btnProfileSubmit.setVisibility(View.GONE);
    }
}
