package com.social.sign.in;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.gson.JsonObject;
import com.social.sign.in.data.local.SharedPrefs;
import com.social.sign.in.data.remote.RetrofitService;
import com.social.sign.in.data.remote.ServiceGenerator;
import com.social.sign.in.horizontalprogresspar.HorizontalProgressBar;
import com.social.sign.in.linkedin.LinkedInConstants;
import com.social.sign.in.utilities.ActivityUtils;
import com.social.sign.in.utilities.LogcatUtils;
import com.social.sign.in.webview.MyWebChromeClient;
import com.social.sign.in.webview.MyWebViewClient;
import com.social.sign.in.webview.WebChromeClientCallback;
import com.social.sign.in.webview.WebViewClientCallback;
import com.social.sign.in.webview.WebViewOpenPDFCallback;
import org.json.JSONObject;
import java.net.URL;
import java.util.Calendar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebViewActivity extends AppCompatActivity implements WebViewClientCallback, WebViewOpenPDFCallback, WebChromeClientCallback {

    private static final String TAG = WebViewActivity.class.getSimpleName();

    private WebView webView;
    private HorizontalProgressBar progressBar;
    private TextView titleTextView;
    private ImageButton backButton, refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initializeView();
        initializeObject();
        initializeToolBar();
        onTextChangedListener();
        initializeEvent();

        Bundle bundle   = ActivityUtils.getDataFromPreviousActivity(WebViewActivity.this);
        String url      = bundle.getString("url");

        initializingWebView(url);
    }

    protected void initializeView() {
        refreshButton   = findViewById(R.id.refreshImageButton);
        titleTextView   = findViewById(R.id.titleTextView);
        backButton     = findViewById(R.id.backImageButton);
        webView         = findViewById(R.id.webView);
        progressBar     = findViewById(R.id.progressBar);
    }

    protected void initializeObject() {
    }

    protected void initializeToolBar() {
    }

    protected void onTextChangedListener() {
    }

    protected void initializeEvent() {
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.this.finish();
            }
        });
    }

    private void initializingWebView(String url) {
        MyWebViewClient myWebViewClient     = new MyWebViewClient(this,this, this);
        MyWebChromeClient myWebChromeClient = new MyWebChromeClient(this);

        webView.setWebViewClient(myWebViewClient);
        webView.setWebChromeClient(myWebChromeClient);

        webView.canGoBack();
        webView.canGoForward();
        webView.setLongClickable(false);

        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);

        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        WebSettings webSettings = webView.getSettings();

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setLoadsImagesAutomatically(true);

        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        /**
         * LOAD_CACHE_ONLY: Do not use the network, only read the local cache data
         * LOAD_DEFAULT: (default) Decide whether to fetch data from the network according to cache-control.
         * LOAD_NO_CACHE: Do not use cache, only get data from the network.
         * LOAD_CACHE_ELSE_NETWORK, as long as it is locally available, regardless of whether it is expired or no-cache, the data in the cache is used.
         */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        /* webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36"); */

        webView.loadUrl(url);
        titleTextView.setText(getTitleFromUrl(url));
    }

    public void ConfirmExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                WebViewActivity.this.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        if(webView != null && webView.canGoBack()){
            webView.goBack();
        }
        else
        {
            ConfirmExit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
        webView=null;
    }

    /*
     ***********************************************************************************************
     ***************************************** Implemented methods *********************************
     ***********************************************************************************************
     */
    @Override
    public void onPageStarted(WebView webView, String url, Bitmap favicon) {
    }

    @Override
    public void onPageFinished(WebView webView, String url) {
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if(url.startsWith(LinkedInConstants.REDIRECT_URI))
        {
            Uri uri = Uri.parse(url);
            String stateToken = uri.getQueryParameter("state");

            if(stateToken==null || !stateToken.equals(LinkedInConstants.STATE))
            {
                LogcatUtils.informationMessage(TAG, "State token doesn't match");
                return true;
            }

            String authorizationToken = uri.getQueryParameter("code");

            if(authorizationToken==null)
            {
                LogcatUtils.informationMessage(TAG, "The user doesn't allow authorization.");
                return true;
            }

            LogcatUtils.informationMessage(TAG, "Auth token received: "+authorizationToken);

            String accessTokenUrl = LinkedInConstants.getAccessTokenUrl(authorizationToken);
            callApi(accessTokenUrl);
        }
        else
        {
            LogcatUtils.informationMessage(TAG,"Redirecting to: "+url);
            webView.loadUrl(url);
        }

        titleTextView.setText(getTitleFromUrl(url));
        return true;
    }

    private void callApi(String accessTokenUrl) {
        RetrofitService retrofitService = ServiceGenerator.createService(getApplicationContext(), RetrofitService.class);
        Call<JsonObject> call = retrofitService.getAccessToken(accessTokenUrl);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful())
                {
                    if (response.code() == 200)
                    {
                        System.out.println("Get Request Response : "+response.body().toString());

                        JSONObject jsonObject;
                        int expiresIn;
                        String accessToken;
                        try
                        {
                            jsonObject = new JSONObject(response.body().toString());

                            expiresIn = jsonObject.has("expires_in") ? jsonObject.getInt("expires_in") : 0;
                            accessToken = jsonObject.has("access_token") ? jsonObject.getString("access_token") : null;

                            /*
                             * Calculate date of expiration
                             */
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.SECOND, expiresIn);
                            long expireDate = calendar.getTimeInMillis();

                            SharedPrefs.getInstance(getApplicationContext()).put("token", accessToken);
                            SharedPrefs.getInstance(getApplicationContext()).put("expired", expireDate);

                            Intent goToHomeScreenActivity = new Intent(WebViewActivity.this, DetailActivity.class);
                            startActivity(goToHomeScreenActivity);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        System.out.println("Something wrong");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                System.out.println("*****"+throwable.getMessage());
            }
        });
    }

    @Override
    public void onProgress(WebView webView, int newProgress) {
        reportProgress(newProgress);
    }

    @Override
    public void onOpenPDF() {
    }

    /*
     ***********************************************************************************************
     ******************************************* Helper methods ************************************
     ***********************************************************************************************
     */
    private void reportProgress(final int newProgress) {
        if (newProgress > 0 && newProgress < 100) {
            if (progressBar.getVisibility() != View.VISIBLE) {
                progressBar.setVisibility(View.VISIBLE);
            }
            progressBar.setProgress(newProgress);
        } else {
            if (progressBar.getVisibility() != View.INVISIBLE) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void refresh () {
        if (webView != null) webView.reload();
    }

    private String getTitleFromUrl(String url) {
        String title = url;
        try {
            URL urlObj = new URL(url);
            String host = urlObj.getHost();
            if (host != null && !host.isEmpty()) {
                return urlObj.getProtocol() + "://" + host;
            }
            if (url.startsWith("file:")) {
                String fileName = urlObj.getFile();
                if (fileName != null && !fileName.isEmpty()) {
                    return fileName;
                }
            }
        } catch (Exception e) {
        }
        return title;
    }
}