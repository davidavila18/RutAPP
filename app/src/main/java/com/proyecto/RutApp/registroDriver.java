package com.proyecto.RutApp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class registroDriver extends AppCompatActivity {

    //Llamar objetos
    private EditText etName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etPassword;
    private EditText etPassword1;
    private EditText etMakeVehicle;
    private EditText etRegistrationVehicle;
    private EditText etDateSoat;
    private EditText etNumberP;

    //Variables
    private String name;
    private String email;
    private String pass;
    private String phone;
    private String makeVehicle;
    private String registrationVehicle;
    private String dateSoat;
    private String numberP;

    private FirebaseAuth authFirebase;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        authFirebase = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro2);
        etName= (EditText)findViewById(R.id.nameC);
        etEmail= (EditText)findViewById(R.id.emailC);
        etPhone= (EditText)findViewById(R.id.phoneC);
        etPassword= (EditText)findViewById(R.id.passwordC);
        etPassword1= (EditText)findViewById(R.id.passwordC1);
        etMakeVehicle = (EditText)findViewById(R.id.marcaV);
        etRegistrationVehicle = (EditText)findViewById(R.id.matriculaV);
        etDateSoat = (EditText)findViewById(R.id.fechaS);
        etNumberP = (EditText)findViewById(R.id.numeroPuestos);
        Button registerButton = (Button) findViewById(R.id.bregistrarC);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRegister();
            }
        });
    }

    private void validateRegister() {
        name = etName.getText().toString();
        email = etEmail.getText().toString();
        phone = etPhone.getText().toString();
        pass = etPassword.getText().toString();
        String pass1 = etPassword1.getText().toString();
        makeVehicle = etMakeVehicle.getText().toString();
        registrationVehicle = etRegistrationVehicle.getText().toString();
        dateSoat = etDateSoat.getText().toString();
        numberP = etNumberP.getText().toString();

        if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !makeVehicle.isEmpty() && !pass.isEmpty() && !pass1.isEmpty() && !registrationVehicle.isEmpty() && !dateSoat.isEmpty() && !numberP.isEmpty()) {
            if (pass.equals(pass1)) {
                if (pass.length() > 6) {
                    registerDriver();
                } else {
                    Toast.makeText(registroDriver.this, "Su contraseña debe tener mas de 6 caracteres", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(registroDriver.this, "Las contraseñas no coincidan", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(registroDriver.this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerDriver(){
        authFirebase.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Map<String, Object> mapa= new HashMap<>();
                    mapa.put("Name", name);
                    mapa.put("Email", email);
                    mapa.put("phone", phone);
                    mapa.put("Password", pass);
                    mapa.put("Make of vehicle", makeVehicle);
                    mapa.put("Vehicle registration", registrationVehicle);
                    mapa.put("Date SOAT", dateSoat);
                    mapa.put("Number of positions", numberP);
                    mapa.put("Kind", "Driver");
                    String id = authFirebase.getCurrentUser().getUid();
                    mDatabase.child("Users").child(id).setValue(mapa).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                Toast.makeText(registroDriver.this,"Iniciar Sesion",Toast.LENGTH_SHORT).show();
                                Intent i= new Intent(registroDriver.this, inicio.class);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(registroDriver.this,"No se pudo crear el usuario",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(registroDriver.this,"No se pudo registrar el usuario",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
