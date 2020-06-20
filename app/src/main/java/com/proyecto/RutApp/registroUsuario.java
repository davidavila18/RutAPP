package com.proyecto.RutApp;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class registroUsuario extends AppCompatActivity {

    //Llamar objetos
    private EditText nameT;
    private EditText emailT;
    private EditText phoneT;
    private EditText addressT;
    private EditText passwordT;
    private EditText confirmPasswordT;

    //Variables
    private String name;
    private String email;
    private String address;
    private String pass;
    private String phone;

    private FirebaseAuth authFirebase;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        authFirebase = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nameT = (EditText)findViewById(R.id.R_nombre);
        emailT = (EditText)findViewById(R.id.R_correo);
        phoneT = (EditText)findViewById(R.id.R_phone);
        addressT = (EditText)findViewById(R.id.R_Address);
        passwordT = (EditText)findViewById(R.id.R_password);
        confirmPasswordT= (EditText)findViewById(R.id.R_password1);
        Button registrarU = (Button) findViewById(R.id.bregistrar);

        registrarU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRegister();
            }
        });
    }

    private void validateRegister() {
        name = nameT.getText().toString();
        email = emailT.getText().toString();
        phone = phoneT.getText().toString();
        address = addressT.getText().toString();
        pass = passwordT.getText().toString();
        String confirmPass = confirmPasswordT.getText().toString();
        if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !address.isEmpty() && !pass.isEmpty() && !confirmPass.isEmpty()) {
            if (pass.equals(confirmPass)) {
                if (pass.length() > 6) {
                    registerUser();
                } else {
                    Toast.makeText(registroUsuario.this, "Su contraseña debe tener mas de 6 caracteres", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(registroUsuario.this, "Las contraseñas no coincidan", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(registroUsuario.this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser(){
        authFirebase.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Map<String, Object> map= new HashMap<>();
                    map.put("Name", name);
                    map.put("Email", email);
                    map.put("phone", phone);
                    map.put("Address", address);
                    map.put("Password", pass);
                    map.put("Kind", "User");
                    String id = Objects.requireNonNull(authFirebase.getCurrentUser()).getUid();
                    mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                Toast.makeText(registroUsuario.this,"Iniciar Sesion",Toast.LENGTH_SHORT).show();
                                Intent i= new Intent(registroUsuario.this, inicio.class);
                                startActivity(i);
                            }
                            else{
                               Toast.makeText(registroUsuario.this,"No se pudo crear el usuario",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(registroUsuario.this,"No se pudo registrar el usuario",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

