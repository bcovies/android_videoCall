package br.com.dental_consulting.controllers;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.dental_consulting.ChamadaActivity;
import br.com.dental_consulting.R;
import br.com.dental_consulting.models.Usuario;

public class RecyclerAdapterChamada extends RecyclerView.Adapter<RecyclerAdapterChamada.ViewHolder> {

    //Variáveis;
    private Usuario usuario;

    private ArrayList<Usuario> arrayListUsuario = new ArrayList<Usuario>();
    private Context context;
    private TextView textView;
    private Button button;

    //Construtor
    public RecyclerAdapterChamada(Context context, ArrayList<Usuario> arrayList) {
        this.arrayListUsuario = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chamada_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(arrayListUsuario.get(position));

    }

    @Override
    public int getItemCount() {
        return arrayListUsuario.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        DatabaseReference mDatabase;
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        private String texto;
        private Usuario novoUsuario;

        public void bindView(Usuario usuario) {
            novoUsuario = usuario;
            textView.setText(novoUsuario.getEmail());
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.activity_chamada_recyclerview_textView);
            button = itemView.findViewById(R.id.activity_chamada_recyclerview_botao);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verificarDisponível();
                }

                private void verificarDisponível() {
                    if (novoUsuario.isEstaDisponivel()) {
                        System.out.println("O usuário " + mAuth.getEmail() + " de ID: " + mAuth.getUid() + " está chamando " + novoUsuario.getEmail() + " de ID: " + novoUsuario.getChaveUsuario());
                        alterarEstado();
                    } else {
                        System.out.println("Usuario não está disponível!");
                    }
                }

                private void alterarEstado() {
                    mDatabase = FirebaseDatabase.getInstance().getReference("usuarios");
                    mDatabase.child(novoUsuario.getChaveUsuario()).child("dados").child("estaDisponivel").setValue(false);
                    mDatabase.child(mAuth.getUid()).child("dados").child("estaDisponivel").setValue(false);
                }
            });


        }
    }
}
