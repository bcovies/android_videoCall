package br.com.dental_consulting;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private static final int PERMISSION_REQUEST_RECORD_AUDIO = 0;
    String permissaoCamera = "android.permission.CAMERA";
    String permissaoAudio = "android.permission.RECORD_AUDIO";


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

        ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{permissaoCamera}, PERMISSION_REQUEST_CAMERA);
//        ActivityCompat.requestPermissions(DashboardActivity.this,new String[]{permissaoAudio},PERMISSION_REQUEST_RECORD_AUDIO);
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
//                .child(mUid).child("dados").child("estaDisponivel").getKey();
//        System.out.println(teste);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
//                Toast.makeText(DashboardActivity.this, "Permissão de câmera adquirida com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                // Permission request was denied.
                Toast.makeText(DashboardActivity.this, "Permissão de câmera falhou!", Toast.LENGTH_SHORT).show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
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