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

import br.com.dental_consulting.controllers.ValidarUsuario;
import br.com.dental_consulting.models.Usuario;

public class MainActivity extends AppCompatActivity {

    private EditText main_email;
    private EditText main_senha;
    private Button botaoEntrar;
    private Button botaoCadastrar;
    private String email;
    private String senha;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_email = findViewById(R.id.main_editText_email);
        main_senha = findViewById(R.id.main_editText_senha);
        botaoEntrar = findViewById(R.id.main_button_entrar);
        botaoCadastrar = findViewById(R.id.main_button_cadastrar);

        mAuth = FirebaseAuth.getInstance();

        ValidarUsuario validarUsuario = new ValidarUsuario();

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = main_email.getText().toString();
                senha = main_senha.getText().toString();
                if(validarUsuario.validarEmail(email) && validarUsuario.validarSenha(senha)){
                    Usuario usuario = new Usuario(email,senha);
                    autenticarUsuario(usuario);
                }else{
                    Toast.makeText(MainActivity.this, "Houve um problema nos campos inseridos...", Toast.LENGTH_SHORT);
                }
            }
        });

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CadastroActivity.class));
            }
        });
    }

    private boolean usuarioLogado() {
        final FirebaseUser usuarioAtual = mAuth.getCurrentUser();
        if (usuarioAtual != null) {
            Toast.makeText(MainActivity.this, "Usuário já logado!", Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, "Bem-vindo!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(MainActivity.this, "Usuário não logado, faça cadastro ou login!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (usuarioLogado()) {
            acessarDashboardActivity();
        }
    }

    private void autenticarUsuario(Usuario usuario) {
        mAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    acessarDashboardActivity();
                }else{
                    Toast.makeText(MainActivity.this, "Houve no banco de dados...", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    public void acessarDashboardActivity() {
        finish();
        Toast.makeText(MainActivity.this, "Bem-vindo!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, DashboardActivity.class));
    }
}