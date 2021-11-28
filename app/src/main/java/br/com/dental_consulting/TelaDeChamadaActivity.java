package br.com.dental_consulting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import br.com.dental_consulting.models.Usuario;

public class TelaDeChamadaActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
    Usuario parceiroDeChamada;
    Button encerrar_chamada;
    WebView webView;
    WebSettings webSettings;
    String uniqueId;
    boolean audioAtivo = true;
    boolean videoAtivo = true;

    ImageView videoImage;
    ImageView audioImage;

    LinearLayout linearLayout;
    private Object String;
    private android.webkit.ValueCallback<java.lang.String> ValueCallback;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_de_chamada);

        parceiroDeChamada = getIntent().getParcelableExtra("parceiroDeChamada");
        webView = findViewById(R.id.activity_tela_de_chamada_webView);
        webSettings = webView.getSettings();
        encerrar_chamada = findViewById(R.id.activity_tela_de_chamada_botao_encerrar);
        linearLayout = findViewById(R.id.activity_tela_de_chamada_linear_layout);
        videoImage = findViewById(R.id.activity_tela_de_chamada_botao_ligarVideo);
        audioImage = findViewById(R.id.activity_tela_de_chamada_botao_ligarAudio);

        videoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoAtivo = !videoAtivo;
                if (videoAtivo) {
                    videoImage.setImageResource(R.drawable.ic_baseline_videocam_24);
                    inicializaVideo(new ValueCallback<String>() { @Override public void onReceiveValue(String value) {}});
                } else {
                    videoImage.setImageResource(R.drawable.ic_baseline_videocam_off_24);
                }
            }
        });

        audioImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioAtivo = !audioAtivo;
                if (audioAtivo) {
                    audioImage.setImageResource(R.drawable.ic_baseline_mic_24);
                    inicializaAudio(new ValueCallback<String>() { @Override public void onReceiveValue(String value) {}});
                } else {
                    audioImage.setImageResource(R.drawable.ic_baseline_mic_off_24);
                }
            }
        });

        encerrar_chamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encerrarChamada(parceiroDeChamada);
            }
        });
        setupWebView();
    }
    void inicializaAudio(final ValueCallback<String> valueCallback) {
        String evS = "javascript:toggleAudio('" + audioAtivo + "')";
        System.out.println("############################################### VIDEO: " +audioAtivo);
        try {
            webView.evaluateJavascript(evS, valueCallback);
            conectaOsUsuarios(parceiroDeChamada, uniqueId);
        } catch (Exception e) {
            System.out.println("Erro try: " + e);
            valueCallback.onReceiveValue(null);// You can pass any value instead of null.
        }
    }
    void inicializaVideo(final ValueCallback<String> valueCallback) {
        String evS = "javascript:toggleVideo('" + videoAtivo + "')";
        System.out.println("############################################### VIDEO: " +videoAtivo);
        try {
            webView.evaluateJavascript(evS, valueCallback);
            conectaOsUsuarios(parceiroDeChamada, uniqueId);
        } catch (Exception e) {
            System.out.println("Erro try: " + e);
            valueCallback.onReceiveValue(null);// You can pass any value instead of null.
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Usuario parceiroDeChamada = getIntent().getParcelableExtra("parceiroDeChamada");
        encerrarChamada(parceiroDeChamada);
    }

    private void encerrarChamada(Usuario parceiroDeChamada) {
        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios");

        mDatabase.child(parceiroDeChamada.getChaveUsuario()).child("dados").child("estaDisponivel").setValue(true);
        mDatabase.child(parceiroDeChamada.getChaveUsuario()).child("dados").child("idConexao").setValue("null");
        mDatabase.child(parceiroDeChamada.getChaveUsuario()).child("dados").child("emChamadaCom").setValue("null");

        mDatabase.child(mAuth.getUid()).child("dados").child("estaDisponivel").setValue(true);
        mDatabase.child(mAuth.getUid()).child("dados").child("emChamadaCom").setValue("null");
        mDatabase.child(mAuth.getUid()).child("dados").child("idConexao").setValue("null");

        voltarTela();
    }

    private void voltarTela() {
        finish();
        startActivity(new Intent(this, ChamadaActivity.class));
    }

    private void setupWebView() {
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
//        webSettings.setAllowFileAccess(true);

        webView.setWebViewClient(new WebViewClient() {
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    request.grant(request.getResources());
                }
            }
//            @Override
//            public void onPermissionRequest(PermissionRequest request) {
////                super.onPermissionRequest(request);
//
//                if (request != null) {
//                    request.grant(request.getResources());
//                    Toast.makeText(TelaDeChamadaActivity.this, "Precisa aceitar permissões", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    Toast.makeText(TelaDeChamadaActivity.this, "permissões OK", Toast.LENGTH_SHORT).show();
//                }
//            }
        });
        loadVideoCall();
//        Toast.makeText(TelaDeChamadaActivity.this, "Inicalizado", Toast.LENGTH_SHORT).show();
    }

    private void loadVideoCall() {
//        String filePath = "file:android_asset/call.html";
        webView.loadUrl("file:android_asset/call.html");
//        Toast.makeText(TelaDeChamadaActivity.this, "loadVideoCall", Toast.LENGTH_SHORT).show();
        webView.setWebViewClient((WebViewClient) (new WebViewClient() {
            public void onPageFinished(@Nullable WebView view, @Nullable String url) {
//                System.out.println("URL: " + url);
                initializePeer();
            }
        }));
    }

    private void initializePeer() {
        getFirstUser(new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                if (value != null && value.length() != 0 && !value.equals("null")) {
                    //do something with proper value
//                    System.out.println("Valor: " + value);
                } else {
//                    System.out.println("Erro no ELSE: "+ value);
                    // take necessary action if value is null
                }
            }
        });
//        onCallRequest(parceiroDeChamada.getNome());
    }

    private void getFirstUser(final ValueCallback<String> valueCallback) {
        uniqueId = getUniqueID();
        String evS = "javascript:init('" + uniqueId + "')";
        System.out.println("UNICO ID CONETANDO 1: " + uniqueId);

        try {
            webView.evaluateJavascript(evS, valueCallback);
            conectaOsUsuarios(parceiroDeChamada, uniqueId);
        } catch (Exception e) {
            System.out.println("Erro try: " + e);
            valueCallback.onReceiveValue(null);// You can pass any value instead of null.
        }
    }

    private void conectaOsUsuarios(Usuario parceiroDeChamada, String evS) {
        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios");
        // altera estaDisponivel
        mDatabase.child(parceiroDeChamada.getChaveUsuario()).child("dados").child("idConexao").setValue(evS);
        mDatabase.child(mAuth.getUid()).child("dados").child("idConexao").setValue(evS);
    }


    private void switchToControls() {
        linearLayout.setVisibility(View.VISIBLE);
    }


    private String getUniqueID() {
        return UUID.randomUUID().toString();
    }
}