package br.com.dental_consulting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
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
    RelativeLayout relativeLayout;
    TextView textView;
    ImageView aceitar_chamada;
    ImageView rejeitar_chamada;
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
        webView = findViewById(R.id.webView);
        webSettings = webView.getSettings();
        encerrar_chamada = findViewById(R.id.activity_tela_de_chamada_botao_encerrar);
        rejeitar_chamada = findViewById(R.id.chamada_superior_rejeitar);
        relativeLayout = findViewById(R.id.chamada_layout_superior);
        textView = findViewById(R.id.chamada_superior_nome);
        aceitar_chamada = findViewById(R.id.chamada_superior_aceitar);
        linearLayout = findViewById(R.id.chamada_chamada_layout);

        encerrar_chamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encerrarChamada(parceiroDeChamada);
            }
        });

        setupWebView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Usuario parceiroDeChamada = getIntent().getParcelableExtra("parceiroDeChamada");
        encerrarChamada(parceiroDeChamada);
    }

    private void encerrarChamada(Usuario parceiroDeChamada) {
        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios");
        // altera estaDisponivel
        mDatabase.child(parceiroDeChamada.getChaveUsuario()).child("dados").child("estaDisponivel").setValue(true);
        mDatabase.child(mAuth.getUid()).child("dados").child("estaDisponivel").setValue(true);
        // altera emChamadaCom
        mDatabase.child(parceiroDeChamada.getChaveUsuario()).child("dados").child("emChamadaCom").setValue("null");
        mDatabase.child(mAuth.getUid()).child("dados").child("emChamadaCom").setValue("null");
        voltarTela();
    }

    private void voltarTela() {
        finish();
        startActivity(new Intent(this, ChamadaActivity.class));
    }

    public static void onPeerConnected() {

    }


    private void setupWebView() {
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccess(true);

        webView.setWebViewClient(new WebViewClient(){
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                super.onPermissionRequest(request);
                if (request != null) {
                    request.grant(request.getResources());
                    Toast.makeText(TelaDeChamadaActivity.this, "Precisa aceitar permissões", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TelaDeChamadaActivity.this, "permissões OK", Toast.LENGTH_SHORT).show();
                }
            }
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
                System.out.println("AQUIIIIIIIIIIIIIIIIIII ULLLLLLLLLLLLLLLLLLLLLLLLLL URL: " + url);
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
                    System.out.println("Valor: " + value);
                } else {
                    System.out.println("Erro no ELSE: "+ value);
                    // take necessary action if value is null
                }
            }
        });

        onCallRequest(parceiroDeChamada.getNome());
    }

    private void getFirstUser(final ValueCallback<String> valueCallback) {
        uniqueId = getUniqueID();
        String evS = "javascript:init('"+uniqueId+ "')";
        System.out.println("UNICO ID: " + uniqueId);
        try {
            webView.evaluateJavascript(evS, valueCallback);
        } catch (Exception e) {
            System.out.println("Erro try: " + e);
            valueCallback.onReceiveValue(null);// You can pass any value instead of null.
        }
    }




    private void onCallRequest(String caller) {
        if (caller == null) {
            return;
        } else {
            relativeLayout.setVisibility(View.VISIBLE);
            textView.setText(caller + " está te chamando... ");
            aceitar_chamada.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // firebase recebe connid do uniqueId
                    // firebase is available muda

                    relativeLayout.setVisibility(View.GONE);
                    switchToControls();
                }
            });

            rejeitar_chamada.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // firebase incoming para null
                    relativeLayout.setVisibility(View.GONE);
                }
            });
        }
    }

    private void switchToControls() {
        linearLayout.setVisibility(View.VISIBLE);
    }

//    private void callJavascriptFunction(String functionString) {
//        webView.post(webView.evaluateJavascript(@NonNull String script, @android.annotation.Nullable ValueCallback<String> resultCallback ));
//    }

    private String getUniqueID() {
        return UUID.randomUUID().toString();
    }
}