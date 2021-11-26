package br.com.dental_consulting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.dental_consulting.controllers.ValidarUsuario;
import br.com.dental_consulting.models.Usuario;

public class CadastroActivity extends AppCompatActivity {
    private EditText registar_email;
    private EditText registar_senha;
    private EditText registar_nome;
    private EditText registar_telefone;
    private EditText registar_nascimento;
    private Button botaoRegistrar;

    private String email;
    private String senha;
    private String nome;
    private String telefone;
    private String nascimento;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    ValidarUsuario validarUsuario = new ValidarUsuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        registar_email = findViewById(R.id.cadastro_editText_email);
        registar_senha = findViewById(R.id.cadastro_editText_senha);
        registar_nome = findViewById(R.id.cadastro_editText_nome);
        registar_telefone = findViewById(R.id.cadastro_editText_telefone);
        registar_nascimento = findViewById(R.id.cadastro_editText_nascimento);
        botaoRegistrar = findViewById(R.id.cadastro_botao_cadastrar);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        botaoRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = registar_email.getText().toString();
                senha = registar_senha.getText().toString();
                nome = registar_nome.getText().toString();
                telefone = registar_telefone.getText().toString();
                nascimento = registar_nascimento.getText().toString();

                if(validarUsuario.validarEmail(email) && validarUsuario.validarSenha(senha) && validarUsuario.validarNome(nome) && validarUsuario.validarTelefone(telefone) && validarUsuario.validarNascimento(nascimento)){
//                    System.out.println("Email: " + email + "Senha: " + senha + "Nome: " + nome + "Telefone: " + telefone + "Nascimento: " + nascimento);
                    Usuario usuario = new Usuario(email,senha,nome,telefone,nascimento);
                    criarUsuario(usuario);
                }else{
                    System.out.println("Demandas não atingidas");
                }
            }

        });
    }

    private void criarUsuario(Usuario usuario) {
        mAuth.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    criarBancoUsuario(usuario);
                }else{
                    Toast.makeText(CadastroActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void criarBancoUsuario(Usuario usuario){
        Usuario usuarioTemp = new Usuario(usuario.getEmail(),usuario.getNome(),usuario.getTelefone(),usuario.getNascimento());
        String ID = mAuth.getCurrentUser().getUid();
        DatabaseReference myRef = mDatabase.getReference("usuarios/" + ID + "/dados/");
        myRef.setValue(usuarioTemp);
        Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso!!", Toast.LENGTH_SHORT).show();
        acessarDashboardActivity();
    }

    public void acessarDashboardActivity() {
        finish();
        startActivity(new Intent(this, DashboardActivity.class));
    }

}