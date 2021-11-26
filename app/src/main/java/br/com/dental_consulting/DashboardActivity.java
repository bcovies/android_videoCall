package br.com.dental_consulting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView dashboard_idUsuario;
    private TextView dashboard_emailUsuario;
    private Button botaoSair;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

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

    public void usuarioSair() {
        mAuth.signOut();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}