package com.example.examnet2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.examnet2.Entity.Comment;
import com.example.examnet2.service.CommentService;
import com.example.examnet2.util.ConnectionRest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //Al crear el spinner se crean tres objectos
    Spinner spnComentarios;
    ArrayAdapter<String> adaptadorComentarios;
    ArrayList<String> listaComentarios = new ArrayList<String>();

    Button btnFiltrar;
    TextView txtResultado;

    //Servicio de usuario de retrofit
    CommentService commentService;

    //Aqui estaran toda la data de usuarios
    List<Comment> lstSalida ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        adaptadorComentarios = new ArrayAdapter<String>(
                this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                listaComentarios);
        spnComentarios = findViewById(R.id.spnComentarios);
        spnComentarios.setAdapter(adaptadorComentarios);

        btnFiltrar = findViewById(R.id.btnFiltrar);
        txtResultado = findViewById(R.id.txtResultado);

        commentService = ConnectionRest.getConnecion().create(CommentService.class);

        cargaUsuarios();

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idUsuario = (int)spnComentarios.getSelectedItemId();
                Comment objUser = lstSalida.get(idUsuario);
                String salida = "Comment: ";
                salida +=  "Id  " + objUser.getId() +"\n";
                salida +=  "Name  " + objUser.getName() +"\n";
                salida +=  "Email  " + objUser.getEmail() +"\n";
                salida +=  "Body  " + objUser.getBody() +"\n";

                txtResultado.setText(salida);
                Log.d("OnClick",objUser.getId());

            }
        });

    }

    void cargaUsuarios(){
        Call<List<Comment>> call =  commentService.listaComentarios();
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                //Las respuesta es exitosa  del servicio Rest
                if (response.isSuccessful()){
                    lstSalida = response.body();
                    for (Comment obj: lstSalida){
                        listaComentarios.add(obj.getId());
                    }
                    adaptadorComentarios.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                //No existe respuesta del servicio Rest
                  Log.e("Error","",t);
            }
        });
    }
}