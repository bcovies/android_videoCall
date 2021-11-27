package br.com.dental_consulting.models;

public class Usuario {

    private String email;
    private String senha;
    private String nome;
    private String telefone;
    private String nascimento;
    private boolean estaDisponivel;
    private String emChamadaCom;
    private String idConexao;
    private String chaveUsuario;

    public Usuario() {
    }

    public Usuario(String email, String nome, String telefone, String nascimento, boolean estaDisponivel, String emChamadaCom, String idConexao, String chaveUsuario) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.telefone = telefone;
        this.nascimento = nascimento;
        this.estaDisponivel = estaDisponivel;
        this.emChamadaCom = emChamadaCom;
        this.idConexao = idConexao;
        this.chaveUsuario = chaveUsuario;
    }



    public Usuario(String email, String senha, String nome, String telefone, String nascimento, boolean estaDisponivel, String emChamadaCom, String idConexao) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.telefone = telefone;
        this.nascimento = nascimento;
        this.estaDisponivel = estaDisponivel;
        this.emChamadaCom = emChamadaCom;
        this.idConexao = idConexao;
    }
    public Usuario(String email, String nome, String telefone, String nascimento, boolean estaDisponivel, String emChamadaCom, String idConexao) {
        this.email = email;
        this.nome = nome;
        this.telefone = telefone;
        this.nascimento = nascimento;
        this.estaDisponivel = estaDisponivel;
        this.emChamadaCom = emChamadaCom;
        this.idConexao = idConexao;
    }
    // Cadastro
    public Usuario(String email, String senha, String nome, String telefone, String nascimento) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.telefone = telefone;
        this.nascimento = nascimento;
    }
    //Cadastro sem Senha
    public Usuario(String email, String nome, String telefone, String nascimento) {
        this.email = email;
        this.nome = nome;
        this.telefone = telefone;
        this.nascimento = nascimento;
    }
    // Login
    public Usuario(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getChaveUsuario() {
        return chaveUsuario;
    }

    public void setChaveUsuario(String chaveUsuario) {
        this.chaveUsuario = chaveUsuario;
    }

    public boolean isEstaDisponivel() {
        return estaDisponivel;
    }

    public void setEstaDisponivel(boolean estaDisponivel) {
        this.estaDisponivel = estaDisponivel;
    }

    public String getEmChamadaCom() {
        return emChamadaCom;
    }

    public void setEmChamadaCom(String emChamadaCom) {
        this.emChamadaCom = emChamadaCom;
    }

    public String getIdConexao() {
        return idConexao;
    }

    public void setIdConexao(String idConexao) {
        this.idConexao = idConexao;
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
