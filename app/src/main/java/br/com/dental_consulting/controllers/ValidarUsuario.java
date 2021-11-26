package br.com.dental_consulting.controllers;

import android.widget.Toast;

import br.com.dental_consulting.CadastroActivity;
import br.com.dental_consulting.models.Usuario;

public class ValidarUsuario {

    public boolean validarNascimento(String nascimento){
        if(nascimento.isEmpty()){
//            Toast.makeText(CadastroActivity.this,"Nascimento nulo" , Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!nascimento.contains("/")){
//            Toast.makeText(CadastroActivity.this, "Nascimento está faltando / (01/01/1990)", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(nascimento.length() != 10){
//            Toast.makeText(CadastroActivity.this, "Nascimento deve ter 10 digitos (01/01/1990)", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean validarTelefone(String telefone){
        if(telefone.isEmpty()){
//            Toast.makeText(CadastroActivity.this, "Telefone nulo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(telefone.length() != 11){
//            Toast.makeText(CadastroActivity.this, "Telefone deve ter 11 caracteres ex: 21999999999", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean validarNome(String nome){
        if(nome.isEmpty()){
//            Toast.makeText(CadastroActivity.this, "Nome nulo", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean validarSenha(String senha){
        if(senha.isEmpty()){
//            Toast.makeText(CadastroActivity.this, "Senha nula", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(senha.length()<= 5){
//            Toast.makeText(CadastroActivity.this, "Senha tem que ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(senha.length()>16){
//            Toast.makeText(CadastroActivity.this, "Senha tem que ter no máximo 16 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean validarEmail(String email){
        if(email.isEmpty()){
//            Toast.makeText(CadastroActivity.this, "Email nulo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!email.contains("@")){
//            Toast.makeText(CadastroActivity.this, "Email deve conter @", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
