package br.com.dental_consulting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
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

    private DatabaseReference mDatabase;
    private FirebaseUser mAuth;

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
        setContentView(R.layout.activity_tela_de_chamada);

        // Recebe variáveis da tela anterior (RecyclerAdapterChamada.java)
        parceiroDeChamada = getIntent().getParcelableExtra("parceiroDeChamada");

        webView = findViewById(R.id.activity_tela_de_chamada_webView);
        linearLayout = findViewById(R.id.activity_tela_de_chamada_linear_layout);
        botaoEncerrarChamada = findViewById(R.id.activity_tela_de_chamada_botao_encerrar);
        botaoHabilitarVideo = findViewById(R.id.activity_tela_de_chamada_botao_ligarVideo);
        botaoHabilitarAudio = findViewById(R.id.activity_tela_de_chamada_botao_ligarAudio);

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
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child(parceiroDeChamada.getChaveUsuario()).child("dados").child("estaDisponivel").setValue(true);
        mDatabase.child(parceiroDeChamada.getChaveUsuario()).child("dados").child("idConexao").setValue("null");
        mDatabase.child(parceiroDeChamada.getChaveUsuario()).child("dados").child("emChamadaCom").setValue("null");
        mDatabase.child(mAuth.getUid()).child("dados").child("estaDisponivel").setValue(true);
        mDatabase.child(mAuth.getUid()).child("dados").child("emChamadaCom").setValue("null");
        mDatabase.child(mAuth.getUid()).child("dados").child("idConexao").setValue("null");

        voltarTela();
    }

    private void voltarTela() {
        String evS = "javascript:disconnect()";
        webView.evaluateJavascript(evS, null);
        finish();
        startActivity(new Intent(this, ChamadaActivity.class));
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
                inicializarJavaScript();
            }
        }));
    }

    private void inicializarJavaScript() {
        idUnico = gerarUmIdUnico();
        String evS = "javascript:init('" + idUnico + "')";
        webView.evaluateJavascript(evS, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                mensagemRecebida(value);
            }
        });
        System.out.println("##########################################Sistema inicializando");
        enviarIdAoParceiro(parceiroDeChamada,idUnico);
    }

    private void mensagemRecebida(String value) {
        System.out.println("##########################################Sistema inicializado: " + value);

    }

    private void enviarIdAoParceiro(Usuario parceiroDeChamada, String idUnico) {
        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios");
        mDatabase.child(parceiroDeChamada.getChaveUsuario()).child("dados").child("idConexao").setValue(idUnico);
//        mDatabase.child(mAuth.getUid()).child("dados").child("idConexao").setValue(evS);

    }

    private String gerarUmIdUnico() {
        return UUID.randomUUID().toString();
    }
}