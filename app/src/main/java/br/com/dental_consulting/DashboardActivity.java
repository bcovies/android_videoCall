package br.com.dental_consulting;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import br.com.dental_consulting.models.Usuario;

public class DashboardActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private FirebaseAuth mAuth;
    private TextView dashboard_idUsuario;
    private TextView dashboard_emailUsuario;
    private Button botaoSair;
    private Button botaoChamada;
    ImageView aceitar_chamada;
    ImageView rejeitar_chamada;
    RelativeLayout relativeLayout;
    TextView textView;
    DatabaseReference mDatabase;
    String mUid;
    int requestcode = 1;
    ArrayList<String> permissions = new ArrayList<>();
     String  [] permissao11 = new String[]{Manifest.permission.CAMERA , Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET};
    private void aceitarChamadaActivity() {
        finish();
        startActivity(new Intent(this, TelaAceitaChamadaActivity.class));
    }

    private void onCallRequest() {

        relativeLayout.setVisibility(View.VISIBLE);
        aceitar_chamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aceitarChamadaActivity();

                relativeLayout.setVisibility(View.GONE);
//                switchToControls();
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


    @Override
    protected void onStart() {
        super.onStart();

        dashboard_idUsuario = findViewById(R.id.dashboard_textView_idUsuario);
        dashboard_emailUsuario = findViewById(R.id.dashboard_textView_emailUsuario);

        dashboard_idUsuario.setText("Usuário UID: " + mAuth.getCurrentUser().getUid());
        dashboard_emailUsuario.setText("Usuário Email: " + mAuth.getCurrentUser().getEmail());


        rejeitar_chamada = findViewById(R.id.dashboard_chamada_superior_rejeitar);
        relativeLayout = findViewById(R.id.dashboard_chamada_layout_superior);
        textView = findViewById(R.id.dashboard_chamada_superior_nome);
        aceitar_chamada = findViewById(R.id.dashboard_chamada_superior_aceitar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios");
        mUid = mAuth.getCurrentUser().getUid().toString();


        permissions.add("Manifest.permission.CAMERA");
        permissions.add("Manifest.permission.RECORD_AUDIO");
        permissions.add("Manifest.permission.MODIFY_AUDIO_SETTINGS");
        permissions.add("Manifest.permission.INTERNET");

        if(!ifPermissionGranted(permissions)){
            askPermissions(permissao11, requestcode);
        }


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.child(mUid).child("dados").getValue(Usuario.class);
                System.out.println(usuario.isEstaDisponivel());
                if (!usuario.isEstaDisponivel()) {
                    onCallRequest();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        botaoSair = findViewById(R.id.dashboard_botao_sair);
        botaoSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuarioSair();
            }
        });

        botaoChamada = findViewById(R.id.dashboard_botao_chamada);
        botaoChamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acessarChamada();
            }
        });
    }

    private void askPermissions(String [] permission11,int requestcode) {
        ActivityCompat.requestPermissions(this, permission11, requestcode);
    }

    private boolean ifPermissionGranted(ArrayList<String> permissions) {
        for (String permission : permissions) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            System.out.println("PERMISSÃO: " + permission.toString());
            System.out.println(ActivityCompat.checkSelfPermission(this,permission));
            System.out.println( PackageManager.PERMISSION_GRANTED);
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

            if(ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){

                return false;
            }
        }
        return true;
    }


    private void acessarChamada() {
        startActivity(new Intent(this, ChamadaActivity.class));
    }

    private void usuarioSair() {
        mAuth.signOut();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

}