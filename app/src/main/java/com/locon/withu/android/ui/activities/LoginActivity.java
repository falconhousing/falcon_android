
package com.locon.withu.android.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.locon.withu.R;
import com.locon.withu.utils.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult> {

    private AccessToken mAccessToken;

    private static class Permissions {
        public static final String FRIENDS = "user_friends";
        public static final String EMAIL = "email";
        public static final String USER_PROFILE = "public_profile";
    }

    @InjectView(R.id.btn_fb_login)
    LoginButton mFbLoginButton;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(this);
        }
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initFbStuff();
    }

    private void initFbStuff() {

        mCallbackManager = CallbackManager.Factory.create();
        mFbLoginButton.registerCallback(mCallbackManager, this);
        mFbLoginButton.setReadPermissions(getPermissionsList());
    }

    private List<String> getPermissionsList() {
        List<String> permissionsList = new ArrayList<>(0);
        permissionsList.add(Permissions.EMAIL);
        permissionsList.add(Permissions.FRIENDS);
        permissionsList.add(Permissions.USER_PROFILE);
        return permissionsList;
    }

    private boolean evaluatePermissions(Set<String> grantedPerms) {
        if (grantedPerms == null || grantedPerms.equals(Collections.emptySet())) {
            return false;
        }

        return grantedPerms.contains(Permissions.USER_PROFILE) && grantedPerms.contains(Permissions.EMAIL) && grantedPerms.contains(Permissions.FRIENDS);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        Set<String> deniedPerms = loginResult.getRecentlyDeniedPermissions();
        Set<String> grantedPerms = loginResult.getRecentlyGrantedPermissions();
        if (!deniedPerms.equals(Collections.emptySet()) || !evaluatePermissions(grantedPerms)) {
            Toast.makeText(this, "We require email, profile, and friends permissions. Please revoke permissions and re-login", Toast.LENGTH_SHORT).show();
            return;
        }
        mAccessToken = loginResult.getAccessToken();
        makeLoginCall();
        getUserFriends();
    }

    private void makeLoginCall() {
        Toast.makeText(this, "accessToken: " + mAccessToken.getToken(), Toast.LENGTH_SHORT).show();
    }


    private void getUserFriends() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        Logger.logd("LoginActivity", response.getRawResponse());
                    }
                }
        ).executeAsync();
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
