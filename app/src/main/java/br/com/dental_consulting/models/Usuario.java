package br.com.dental_consulting.models;

public class Usuario {

    private String email;
    private String senha;
    private String nome;
    private String telefone;
    private String nascimento;

    public Usuario() {
    }

    public Usuario(String email, String senha, String nome, String telefone, String nascimento) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.telefone = telefone;
        this.nascimento = nascimento;
    }
    public Usuario(String email,  String nome, String telefone, String nascimento) {
        this.email = email;
        this.nome = nome;
        this.telefone = telefone;
        this.nascimento = nascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }
}
