package br.com.dental_consulting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import br.com.dental_consulting.models.Usuario;

public class TelaAceitaChamadaActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    private String idUnico;
    private Usuario parceiroDeChamada;
    private boolean videoEstaAtivo = true;
    private boolean audioEstaAtivo = true;

    private WebView webView;
    private WebSettings webSettings;

    private Button botaoEncerrarChamada;
    private ImageView botaoHabilitarVideo;
    private ImageView botaoHabilitarAudio;
    private LinearLayout linearLayout;

    private android.webkit.ValueCallback<java.lang.String> ValueCallback;


    @Override
    protected void onStart() {
        super.onStart();
        webView.clearCache(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_aceita_chamada);

        linearLayout = findViewById(R.id.activity_tela_aceita_chamada_linear_layout);
        webView = findViewById(R.id.activity_tela_aceita_chamada_webView);
        botaoEncerrarChamada = findViewById(R.id.activity_tela_aceita_chamada_botao_encerrar);
        botaoHabilitarVideo = findViewById(R.id.activity_tela_aceita_chamada_botao_ligarVideo);
        botaoHabilitarAudio = findViewById(R.id.activity_tela_aceita_chamada_botao_ligarAudio);

        webSettings = webView.getSettings();
        webView.clearCache(true);

        botaoHabilitarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoEstaAtivo = !videoEstaAtivo;
                if (videoEstaAtivo) {
                    botaoHabilitarVideo.setImageResource(R.drawable.ic_baseline_videocam_24);
                    String evS = "javascript:toggleVideo('true')";
                    webView.evaluateJavascript(evS, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            System.out.println("Video: "+value);
                        }
                    });
                } else {
                    botaoHabilitarVideo.setImageResource(R.drawable.ic_baseline_videocam_off_24);
                    String evS = "javascript:toggleVideo('true')";
                    webView.evaluateJavascript(evS, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            System.out.println("Video: "+value);
                        }
                    });
                }
            }
        });

        botaoHabilitarAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioEstaAtivo = !audioEstaAtivo;
                if (audioEstaAtivo) {
                    botaoHabilitarAudio.setImageResource(R.drawable.ic_baseline_mic_24);
                    String evS = "javascript:toggleAudio('true')";
                    webView.evaluateJavascript(evS, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            System.out.println("Audio: "+value);
                        }
                    });

                } else {
                    botaoHabilitarAudio.setImageResource(R.drawable.ic_baseline_mic_off_24);
                    String evS = "javascript:toggleAudio('false')";
                    webView.evaluateJavascript(evS, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            System.out.println("Audio: "+value);
                        }
                    });
                }
            }
        });

        botaoEncerrarChamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encerrarChamada();
            }
        });
        setupWebView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        encerrarChamada();
    }

    private void encerrarChamada() {
        voltarTela();
    }

    private void voltarTela() {
        String evS = "javascript:disconnect()";
        webView.evaluateJavascript(evS, null);
        finish();
        startActivity(new Intent(this, DashboardActivity.class));
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    request.grant(request.getResources());
                }
            }
        });

        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        loadVideoCall();

    }

    private void loadVideoCall() {
        webView.loadUrl("file:android_asset/call.html");
        webView.setWebViewClient((new WebViewClient() {
            public void onPageFinished(@Nullable WebView view, @Nullable String url) {
                inicializarJavaScriptInit();
            }
        }));
    }

    private void inicializarJavaScriptInit() {
        idUnico = gerarUmIdUnico();
        String evS = "javascript:init('" + idUnico + "')";
        webView.evaluateJavascript(evS, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                mensagemRecebida(value);
            }
        });
        System.out.println("##########################################Sistema inicializando +++ inicializarJavaScriptInit");

    }

    private void mensagemRecebida(String value) {
        System.out.println("##########################################Sistema CONECTADO!!: " + value);
        boolean esperaDeConexao = false;
        int i = 0;
        System.out.println("##########################################Sistema CONECTADO!!: WHILE");
        while (!esperaDeConexao){
            if( !value.isEmpty() ){
                System.out.println("##########################################Sistema inicializando: "+i );
                i++;
                esperaDeConexao = true;
                inciarChamada();
            }
        }

    }

    private void inciarChamada() {
        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Usuario usuario = snapshot.child(mUid).child("dados").getValue(Usuario.class);
                System.out.println("Inicio Start Call");
                SystemClock.sleep(10000);
                String evS = "javascript:startCall('" + usuario.getIdConexao() + "')";
                webView.evaluateJavascript(evS, null);
                System.out.println("##########################################Sistema inicializando: INCIALIZADA CONEXÃO START CALL");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private String gerarUmIdUnico() {
        return UUID.randomUUID().toString();
    }

}