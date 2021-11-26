package br.com.dental_consulting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.UUID;

public class ChamadaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamada);
//        String username = this.getIntent().getStringExtra("username");
//        if (username == null){
//            Toast.makeText(ChamadaActivity.this, username, Toast.LENGTH_SHORT).show();
//        }
        setupWebView();

    }

    private void setupWebView() {
        WebView webView = findViewById(R.id.webView);
        webView.setWebChromeClient( new WebChromeClient(){
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                super.onPermissionRequest(request);
                if(request != null){
                    request.grant(request.getResources());
                    Toast.makeText(ChamadaActivity.this, "Aqui", Toast.LENGTH_SHORT).show();
                }
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
//        webView.addJavascriptInterface(new JavascriptInterface[], "Android");
    }

    private void loadVideoCall() {
        String filePath = "file:android_asset/call.html";
        WebView webView = findViewById(R.id.webView);
        webView.loadUrl(filePath);
        webView.setWebViewClient((WebViewClient)(new WebViewClient() {
            public void onPageFinished(@Nullable WebView view, @Nullable String url) {
                initializePeer();
            }
        }));
    }

    String uniqueId = "";
    private void initializePeer() {
        uniqueId = getUniqueID();
        callJavascriptFunction();
    }

    private void callJavascriptFunction() {
        WebView webView = findViewById(R.id.webView);
//        webView.post( webView.evaluateJavascript(String s ));
    }
    public final void onPeerConnected() {
//        this.isPeerConnected = true;
    }
    private String getUniqueID() {
        return UUID.randomUUID().toString();
    }

}