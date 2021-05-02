package com.social.sign.in;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.social.sign.in.linkedin.LinkedInConstants;
import com.social.sign.in.utilities.ActivityUtils;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = SignInActivity.class.getSimpleName();
    private LinearLayout signInButtonLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initializeView();
        initializeEvent();
    }

    protected void initializeView() {
        signInButtonLinearLayout = findViewById(R.id.signInButtonLinearLayout);
    }

    protected void initializeEvent() {
        signInButtonLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    /*
     ***********************************************************************************************
     ******************************************* Helper methods ************************************
     ***********************************************************************************************
     */
    private void signIn() {
        String authorizationCodeUrl = LinkedInConstants.getAuthorizationUrl();
        Bundle bundle = new Bundle();
        bundle.putString("url", authorizationCodeUrl);
        ActivityUtils.launchActivityWithBundle(SignInActivity.this, WebViewActivity.class, bundle);
    }
}