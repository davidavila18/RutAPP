package com.proyecto.RutApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class olvide extends AppCompatActivity {
    EditText correo;
    Button enviar;
    private String email="";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvide);
        correo= (EditText)findViewById(R.id.o_c_correo);
        enviar = (Button)findViewById(R.id.btenviar);
        mAuth = FirebaseAuth.getInstance();

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email =correo.getText().toString();

                if (!email.isEmpty()){
                    resetPass();
                }
                else{
                    Toast.makeText(olvide.this, "Ingrese el correo registrado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resetPass(){
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(olvide.this, "Se a enviado un correo para reestablecer la contraseña", Toast.LENGTH_SHORT).show();
                    Intent i= new Intent(olvide.this, inicio.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(olvide.this, "No se pudo enviar el correo de registrar contraseña", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
