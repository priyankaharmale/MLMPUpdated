package hnwebproject.com.mlmp.Activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import hnwebproject.com.mlmp.R;


public class WebviewActivity extends AppCompatActivity {
    WebView mWebView;
    private LinearLayout loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
       /* WebView simpleWebView=(WebView) findViewById(R.id.web_view);
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        String url=getIntent().getStringExtra("url");
        simpleWebView.loadUrl(url);*/

        //simpleWebView.loadUrl("http://"+url);





        loadingBar = (LinearLayout)findViewById(R.id.purchase_loadingbar);

        mWebView = (WebView)findViewById(R.id.web_view);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_INSET);

        String url=getIntent().getStringExtra("url");
        mWebView.loadUrl(url);

        mWebView.setWebViewClient(new MyWebViewClient());


    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        //show the web page in webview but not in web browser
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl (url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Toast.makeText(WebviewActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);

        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            loadingBar.setVisibility(View.VISIBLE);

          //  loadingBar.bringToFront();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            loadingBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
