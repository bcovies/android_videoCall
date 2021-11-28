package br.com.dental_consulting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
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

    DatabaseReference mDatabase;
    FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
    Usuario parceiroDeChamada;
    Button encerrar_chamada;
    WebView webView;
    WebSettings webSettings;
    String uniqueId;

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
        setContentView(R.layout.activity_tela_aceita_chamada);

        parceiroDeChamada = getIntent().getParcelableExtra("parceiroDeChamada");
        webView = findViewById(R.id.activity_tela_aceita_chamada_webView);
        webSettings = webView.getSettings();
        encerrar_chamada = findViewById(R.id.activity_tela_aceita_chamada_botao_encerrar);

        linearLayout = findViewById(R.id.activity_tela_aceita_chamada_linear_layout);

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


        webView.setWebViewClient(new WebViewClient(){
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    request.grant(request.getResources());
                }
            }
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onPermissionRequest(final PermissionRequest request) {
//                request.grant(request.getResources());
//            }
//            @Override
//            public void onPermissionRequest(PermissionRequest request) {
////                super.onPermissionRequest(request);
//                if (request != null) {
//                    request.grant(request.getResources());
//                    Toast.makeText(TelaAceitaChamadaActivity.this, "Precisa aceitar permissões", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(TelaAceitaChamadaActivity.this, "permissões OK", Toast.LENGTH_SHORT).show();
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

    }

    private void getFirstUser(final ValueCallback<String> valueCallback) {
    //faz consulta de IDCONEXAO

        uniqueId = getUniqueID();
        String evS = "javascript:init('"+uniqueId+ "')";
        System.out.println("UNICO ID CONETANDO 2: " + uniqueId);
        try {
            webView.evaluateJavascript(evS, valueCallback);

        } catch (Exception e) {
            System.out.println("Erro try: " + e);
            valueCallback.onReceiveValue(null);// You can pass any value instead of null.
        }


        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String  mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Usuario usuario = snapshot.child(mUid).child("dados").getValue(Usuario.class);

                String evS = "javascript:startCall('"+usuario.getIdConexao()+ "')";

                System.out.println("UNICO ID QUE IRÁ SE CONECTAR2: " + usuario.getIdConexao());
                try {
                    webView.evaluateJavascript(evS, valueCallback);

                } catch (Exception e) {
                    System.out.println("Erro try: " + e);
                    valueCallback.onReceiveValue(null);// You can pass any value instead of null.
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private String getUniqueID() {
        return UUID.randomUUID().toString();
    }

}