package br.com.dental_consulting;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.dental_consulting.controllers.RecyclerAdapterChamada;
import br.com.dental_consulting.models.Usuario;

public class ChamadaActivity extends AppCompatActivity {

    //Variáveis;
    ArrayList<Usuario> arrayList = new ArrayList<Usuario>();
    DatabaseReference mDatabase;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamada);
        initView();
    }

    private void initView() {
        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios");
        recyclerView = findViewById(R.id.activity_chamada_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        dataSnapshot.getChildren().forEach(dataSnapshot1 -> {
                            String mAuthEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//                           System.out.println("MEU EMAIL É: "+ mAuthEmail);
                            Usuario usuario = dataSnapshot1.getValue(Usuario.class);
                            if (!usuario.getEmail().contains(mAuthEmail)) {
//                               System.out.println("OUTROS EMAILS: "+usuario.getEmail());
                                arrayList.add(usuario);
                            }
                            System.out.println(usuario.getEmail());
                            RecyclerAdapterChamada recyclerAdapterChamada = new RecyclerAdapterChamada(ChamadaActivity.this, arrayList);
                            recyclerView.setAdapter(recyclerAdapterChamada);
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });
    }
}