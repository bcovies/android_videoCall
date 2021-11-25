package br.com.dental_consulting;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.dental_consulting.models.Usuario;

public class MainActivity extends AppCompatActivity {

    private EditText editText_email;
    private EditText editText_senha;
    private Button button_entrar;
    private Button button_cadastrar;
    private String email;
    private String password;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private boolean usuarioLogado() {
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        boolean logado = false;
        if (currentUser != null) {
            System.out.println("\nUsuário ESTÁ logado");
            logado = true;
        } else {
            System.out.println("\nUsuário NÃO está logado");
        }
        return logado;
    }

    public void acessaDashboardActivity() {
        finish();
        startActivity(new Intent(this, DashboardActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (usuarioLogado()) {
            acessaDashboardActivity();
        }
    }

    private void inicializarVariaveis() {
        editText_email = findViewById(R.id.main_editText_email);
        editText_senha = findViewById(R.id.main_editText_senha);
        button_entrar = findViewById(R.id.main_button_entrar);
        button_cadastrar = findViewById(R.id.main_button_cadastrar);
    }

    public void alocarVariaveisString() {
        email = editText_email.getText().toString();
        password = editText_senha.getText().toString();
    }

    private void inicializaFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
    }


    private void acessarUsuarioFirebase(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    acessaDashboardActivity();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarVariaveis();
        inicializaFirebaseAuth();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Toast.makeText(MainActivity.this, databaseReference.toString(), Toast.LENGTH_SHORT).show();

        button_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alocarVariaveisString();
                acessarUsuarioFirebase(email, password);
            }
        });

        button_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CadastroActivity.class));
            }
        });
    }

}