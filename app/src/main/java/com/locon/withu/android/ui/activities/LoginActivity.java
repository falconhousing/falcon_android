
package com.locon.withu.android.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.locon.withu.Constants;
import com.locon.withu.OkHttpCallback;
import com.locon.withu.R;
import com.locon.withu.models.User;
import com.locon.withu.utils.Logger;
import com.locon.withu.utils.NetworkUtils;
import com.locon.withu.utils.PrefsHelper;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult> {

    private AccessToken mAccessToken;
    private OkHttpCallback<User> okHttpCallback = new OkHttpCallback<User>(User.class) {
        @Override
        public void setFailure(Request request, IOException e) {
            Logger.logd("LoginActivity", "Failed login request with exception " + e.getMessage());
            Toast.makeText(LoginActivity.this, "please try to login again", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void setResponse(Response response, User user) throws IOException {
            Logger.logd("LoginActivity", "Response response; " + response.toString());
            PrefsHelper.putString(Constants.PREF_KEY_AUTH_TOKEN, user.login_auth_token);
            goToHomeScreen();
        }

        @Override
        public void sessionExpired() {

        }
    };


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
        if (isLoggedIn()) {
            goToHomeScreen();
            return;
        }
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(this);
        }
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initFbStuff();
    }

    private boolean isLoggedIn() {
        return !TextUtils.isEmpty(PrefsHelper.getString(Constants.PREF_KEY_AUTH_TOKEN, ""));
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
        getUser();
    }


    private void getUser() {
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        Logger.logd("LoginActivity", response.toString());
                        JSONObject graphObject = response.getJSONObject();
                        try {
                            makeLoginRequest(graphObject.getString("name"), graphObject.getString("id"), graphObject.getString("email"), AccessToken.getCurrentAccessToken().getToken());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        request.setParameters(getBundleParametersForGraphRequest());
        request.executeAsync();
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCancel() {
        Logger.logd("fb", "cancelled");
    }

    @Override
    public void onError(FacebookException error) {
        Logger.logd("fb", "error: " + error.getMessage());
    }

    protected Bundle getBundleParametersForGraphRequest() {
        Bundle parameters = new Bundle();
        parameters
                .putString("fields", "id, name, email, friendlists, birthday, education, work");
        return parameters;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void makeLoginRequest(String name, String id, String email, String accessToken) {
        String loginUrl = "http://neeraja.housing.com:3030/connect-new";
        Uri uri = Uri.parse(loginUrl);
        Uri.Builder builder = uri.buildUpon();
        builder.appendQueryParameter("name", name);
        builder.appendQueryParameter("id", id);
        builder.appendQueryParameter("email", email);
        builder.appendQueryParameter("first_name", name);
        builder.appendQueryParameter("method", "fb");
        builder.appendQueryParameter("source", "android");
        builder.appendQueryParameter("accessToken", accessToken);
        NetworkUtils.doPostCall(builder.toString(), "", okHttpCallback);
    }
}
