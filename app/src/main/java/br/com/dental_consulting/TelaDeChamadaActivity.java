package br.com.dental_consulting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.dental_consulting.models.Usuario;

public class TelaDeChamadaActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
    Usuario parceiroDeChamada;
    private Button encerrar_chamada;

    @Override
    protected void onStart() {
        super.onStart();
        parceiroDeChamada = getIntent().getParcelableExtra("parceiroDeChamada");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_de_chamada);
        encerrar_chamada = findViewById(R.id.activity_tela_de_chamada_botao_encerrar);
        encerrar_chamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encerrarChamada(parceiroDeChamada);
            }
        });

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
}