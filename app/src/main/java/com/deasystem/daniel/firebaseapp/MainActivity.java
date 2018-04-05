package com.deasystem.daniel.firebaseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private EditText txtNome, txtEmail;
    private ListView listView;

    private List<Usuario> listaUsuario = new ArrayList<>();
    private ArrayAdapter<Usuario> adapterUsuario;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Usuario usuarioSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNome = (EditText)findViewById(R.id.txtNome);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        listView = (ListView)findViewById(R.id.listView);

        inicializarDatabase();
        eventoDatabase();

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                usuarioSelecionado = (Usuario)parent.getItemAtPosition(position);
                txtNome.setText(usuarioSelecionado.getNome());
                txtEmail.setText(usuarioSelecionado.getEmail());
            }
        });

    }

    private void inicializarDatabase(){
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    private void eventoDatabase() {
        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaUsuario.clear();
                for(DataSnapshot objSnapshot: dataSnapshot.getChildren()){
                    Usuario u = objSnapshot.getValue(Usuario.class);
                    listaUsuario.add(u);
                }

                adapterUsuario = new ArrayAdapter<Usuario>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        listaUsuario);
                listView.setAdapter(adapterUsuario);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_new){
            Usuario u = new Usuario();
            u.setUid(UUID.randomUUID().toString());
            u.setNome(txtNome.getText().toString());
            u.setEmail(txtEmail.getText().toString());
            databaseReference.child("Usuario").child(u.getUid()).setValue(u);
            exibeMensagem("Usuario salvo com sucesso.");
            limparCampos();
        }else if(id == R.id.menu_edit){
            Usuario u = new Usuario();
            u.setUid(usuarioSelecionado.getUid());
            u.setNome(txtNome.getText().toString().trim());
            u.setEmail(txtEmail.getText().toString().trim());
            databaseReference.child("Usuario").child(u.getUid()).setValue(u);
            exibeMensagem("Usuario Editado com sucesso.");
        }else if(id == R.id.menu_delete){
            Usuario u = new Usuario();
            u.setUid(usuarioSelecionado.getUid());
            databaseReference.child("Usuario").child(u.getUid()).removeValue();
            exibeMensagem("Usuario excluído com sucesso.");
            limparCampos();
        }else if(id == R.id.menu_limpar){
            limparCampos();
        }
        return true;
    }

    private void validarCampos() {
        if(txtEmail.getText().equals("")){
            exibeMensagem("Campo Email deve estar Preenchido");
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtEmail.setText("");
    }

    public void exibeMensagem(String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
