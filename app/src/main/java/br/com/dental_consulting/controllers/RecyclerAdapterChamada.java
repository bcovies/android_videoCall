package br.com.dental_consulting.controllers;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import br.com.dental_consulting.ChamadaActivity;
import br.com.dental_consulting.R;

public class RecyclerAdapterChamada extends RecyclerView.Adapter<RecyclerAdapterChamada.ViewHolder>{

    //Variáveis;
    private ArrayList<String> arrayList = new ArrayList<>();
    private Context context;
    private TextView textView;

    //Construtor
    public RecyclerAdapterChamada(Context context, ArrayList<String> arrayList) {
        this.arrayList = arrayList;
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
        holder.bindView(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private String texto;

        void bindView(String row) {
            textView.setText(row);
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.activity_chamada_recyclerview_textView);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    texto = textView.getText().toString();
                    imprimeTexto(texto);
                }

                private void imprimeTexto(String texto) {
                    System.out.println("Texto aqui:" + texto );
                }

            });

        }
//        private void imprimeTexto(String texto) {
//            System.out.println("Texto aqui:" + texto);
//        }

    }


}
