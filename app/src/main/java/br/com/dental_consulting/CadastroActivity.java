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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.dental_consulting.models.Usuario;

public class CadastroActivity extends AppCompatActivity {
    private EditText registar_email;
    private EditText registar_senha;
    private EditText registar_nome;
    private EditText registar_telefone;
    private EditText registar_nascimento;
    private Button registrar_usuario;

    private String email;
    private String senha;
    private String nome;
    private String telefone;
    private String nascimento;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        registar_email = findViewById(R.id.cadastro_editText_email);
        registar_senha = findViewById(R.id.cadastro_editText_senha);
        registar_nome = findViewById(R.id.cadastro_editText_nome);
        registar_telefone = findViewById(R.id.cadastro_editText_telefone);
        registar_nascimento = findViewById(R.id.cadastro_editText_nascimento);
        registrar_usuario = findViewById(R.id.cadastro_botao_cadastrar);

        registrar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = registar_email.getText().toString();
                senha = registar_senha.getText().toString();
                nome = registar_nome.getText().toString();
                telefone = registar_telefone.getText().toString();
                nascimento = registar_nascimento.getText().toString();

                if(validarEmail(email) && validarSenha(senha) && validarNome(nome) && validarTelefone(telefone) && validarNascimento(nascimento)){
                    System.out.println("Email: " + email + "Senha: " + senha + "Nome: " + nome + "Telefone: " + telefone + "Nascimento: " + nascimento);
                }else{
                    System.out.println("Demandas não atingidas");
                }
            }

        });
    }

    private boolean validarNascimento(String nascimento){
        if(nascimento.isEmpty()){
            Toast.makeText(CadastroActivity.this, "Nascimento nulo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!nascimento.contains("/")){
            Toast.makeText(CadastroActivity.this, "Nascimento está faltando / (01/01/1990)", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(nascimento.length() != 10){
            Toast.makeText(CadastroActivity.this, "Nascimento deve ter 10 digitos (01/01/1990)", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validarTelefone(String telefone){
        if(telefone.isEmpty()){
            Toast.makeText(CadastroActivity.this, "Telefone nulo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(telefone.length() != 11){
            Toast.makeText(CadastroActivity.this, "Telefone deve ter 11 caracteres ex: 21999999999", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validarNome(String nome){
        if(nome.isEmpty()){
            Toast.makeText(CadastroActivity.this, "Nome nulo", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validarSenha(String senha){
        if(senha.isEmpty()){
            Toast.makeText(CadastroActivity.this, "Senha nula", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(senha.length()<= 5){
            Toast.makeText(CadastroActivity.this, "Senha tem que ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(senha.length()>16){
            Toast.makeText(CadastroActivity.this, "Senha tem que ter no máximo 16 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validarEmail(String email){
        if(email.isEmpty()){
            Toast.makeText(CadastroActivity.this, "Email nulo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!email.contains("@")){
            Toast.makeText(CadastroActivity.this, "Email deve conter @", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void cadastrarUsuarioFirebase(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    acessarDashboardActivity();
                    criarRealDatabase(email, password);
                }
            }
        });
    }
    public void acessarDashboardActivity() {
        finish();
        startActivity(new Intent(this, DashboardActivity.class));
    }

    private void criarRealDatabase(String email, String password){

//        Toast.makeText(MainActivity.this,  firebaseAuth.getInstance().getCurrentUser().getUid().toString() , Toast.LENGTH_SHORT).show();
        String ID = firebaseAuth.getInstance().getCurrentUser().getUid();
//        Usuario usuario = new Usuario();
//        usuario.setEmail(email);
        DatabaseReference myRef = database.getReference("usuarios/" + ID);

//        myRef.setValue(usuario);
    }
}