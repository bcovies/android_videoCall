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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity  implements ActivityCompat.OnRequestPermissionsResultCallback{

    private FirebaseAuth mAuth;
    private TextView dashboard_idUsuario;
    private TextView dashboard_emailUsuario;
    private Button botaoSair;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private static final int PERMISSION_REQUEST_RECORD_AUDIO = 0;
    String permissaoCamera = "android.permission.CAMERA";
    String permissaoAudio = "android.permission.RECORD_AUDIO";


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

        ActivityCompat.requestPermissions(DashboardActivity.this,new String[]{permissaoCamera},PERMISSION_REQUEST_CAMERA);
//        ActivityCompat.requestPermissions(DashboardActivity.this,new String[]{permissaoAudio},PERMISSION_REQUEST_RECORD_AUDIO);
        dashboard_idUsuario = findViewById(R.id.dashboard_textView_idUsuario);
        dashboard_emailUsuario = findViewById(R.id.dashboard_textView_emailUsuario);

        dashboard_idUsuario.setText("Usuário UID: " + mAuth.getCurrentUser().getUid());
        dashboard_emailUsuario.setText("Usuário Email: " + mAuth.getCurrentUser().getEmail());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        botaoSair = findViewById(R.id.dashboard_botao_sair);
        botaoSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuarioSair();
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
                Toast.makeText(DashboardActivity.this, "Sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                // Permission request was denied.
                Toast.makeText(DashboardActivity.this, "Falhou!", Toast.LENGTH_SHORT).show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }
    private void usuarioSair() {
        mAuth.signOut();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

}