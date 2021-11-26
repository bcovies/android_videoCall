package br.com.dental_consulting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.UUID;

public class ChamadaActivity extends AppCompatActivity {
    private static boolean isPeerConnected;
    private String username;
    private String emailDestinatario;
//    private boolean isPeerConnected = false;
    private boolean isAudio = false;
    private boolean isVideo = false;
    private String uniqueId = "";

    private Button botao_chamar;
    private EditText chamar_email;

    private ImageView botao_audio;
    private ImageView botao_video;

    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamada);

        chamar_email = findViewById(R.id.chamada_editText_email);
        botao_chamar = findViewById(R.id.chamada_botao_ligar);
        botao_audio = findViewById(R.id.chamada_botao_ligarAudio);
        botao_video = findViewById(R.id.chamada_botao_ligarVideo);

        botao_chamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailDestinatario = chamar_email.getText().toString();
                sendCallRequest();
            }
        });

        botao_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAudio = !isAudio;
                if (isAudio){
                    botao_audio.setImageResource(R.drawable.ic_baseline_mic_24);
                }
                else {
                    botao_audio.setImageResource( R.drawable.ic_baseline_mic_off_24);
                }
                // DESLIGA O AUDIO ATRAVÉS DO JS
//                callJavascriptFunction("javascript:toggleAudio(\"${isAudio}\")");
            }
        });

        botao_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVideo = !isVideo;
                if (isVideo){
                    botao_video.setImageResource(R.drawable.ic_baseline_videocam_24);
                }
                else {
                    botao_video.setImageResource( R.drawable.ic_baseline_videocam_off_24);
                }
                // DESLIGA O AUDIO ATRAVÉS DO JS
//                callJavascriptFunction("javascript:toggleAudio(\"${isAudio}\")");
            }
        });
        setupWebView();
    }

    private void sendCallRequest(){
        if (!isPeerConnected) {
            Toast.makeText(this, "You're not connected. Check your internet", Toast.LENGTH_LONG).show();
        }else{
            // faz uma busca no firebase para saber se o usuário está disponível,
            // caso esteja, muda o campo do destinatário para o usuário remetente do chamado chama
            // listenForConnId()
        }
    }

    private void listenForConnId(){
        // chama o campo idConexao no firebase e verifica se está nulo, caso esteja, retorna vazio
        // , se não, switchToControls()
        // e inicia a chamada em js callJavascriptFunction("javascript:startCall(\"${snapshot.value}\")")
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
        // adicionar
        // webView.addJavascriptInterface(new JavascriptInterface[], "Android");
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

    private void initializePeer() {
        uniqueId = getUniqueID();
//        callJavascriptFunction("javascript:init(\"${uniqueId}\")")
//        firebaseRef.child(username).child("incoming").addValueEventListener(object: ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {}
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                onCallRequest(snapshot.value as? String)
//            }
//
//        })
    }

    private void onCallRequest(String caller){
        if(caller == null){
            return;
        }else{
            RelativeLayout relativeLayout = findViewById(R.id.chamada_layout_superior);
            relativeLayout.setVisibility(View.VISIBLE);

            TextView textView = findViewById(R.id.chamada_superior_nome);
            textView.setText(caller + " está te chamando... ");

            ImageView aceitar_chamada;
            aceitar_chamada = findViewById(R.id.chamada_superior_aceitar);
            aceitar_chamada.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // firebase recebe connid do uniqueId
                    // firebase is available muda

                    relativeLayout.setVisibility(View.GONE);
                    switchToControls();
                }
            });

            ImageView rejeitar_chamada;
            rejeitar_chamada = findViewById(R.id.chamada_superior_rejeitar);
            rejeitar_chamada.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // firebase incoming para null
                    relativeLayout.setVisibility(View.GONE);
                }
            });
        }
    }

    private void switchToControls(){
        RelativeLayout relativeLayout = findViewById(R.id.chamada_barra_inferior);
        relativeLayout.setVisibility(View.INVISIBLE);
        LinearLayout linearLayout = findViewById(R.id.chamada_chamada_layout);
        linearLayout.setVisibility(View.VISIBLE);
    }

    private String getUniqueID() {
        return UUID.randomUUID().toString();
    }

    private void callJavascriptFunction() {
        WebView webView = findViewById(R.id.webView);
//        webView.post( webView.evaluateJavascript(String s ));
    }

    public static void onPeerConnected() {
        isPeerConnected = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        //firebase valor nulo
        WebView webView = findViewById(R.id.webView);
        webView.loadUrl("about:blank");
        super.onDestroy();
    }
}