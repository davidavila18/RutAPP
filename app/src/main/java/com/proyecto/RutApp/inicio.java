package com.proyecto.RutApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static java.lang.Thread.sleep;

public class inicio extends AppCompatActivity {

    private FirebaseAuth mAuth; //Variable de autenticacion Firebase
    //Variables de elementos
    private EditText user_login;
    private EditText password;

    private DatabaseReference mDatabase; //Variable Base de datos Firebase


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser userFireBase = FirebaseAuth.getInstance().getCurrentUser();
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        mAuth = FirebaseAuth.getInstance();
        user_login= (EditText)findViewById(R.id.user_login);
        password= (EditText)findViewById(R.id.user_password);
        Button login = (Button) findViewById(R.id.btnLogin);
        Button registerUser = (Button) findViewById(R.id.btnRegistroU);
        Button registerDriver = (Button) findViewById(R.id.btnRegistroC);
        Button forgetP = (Button) findViewById(R.id.btnOlvideC);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticationFirebase(userFireBase);
            }
        });
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(inicio.this, registroUsuario.class);
                startActivity(i);
            }
        });
        registerDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(inicio.this, registroDriver.class);
                startActivity(i);
            }
        });
        forgetP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(inicio.this, olvide.class);
                startActivity(i);
            }
        });
    }
    //Metodo para autenticar usuario
    private void authenticationFirebase(final FirebaseUser userFireBase) {
        String user = user_login.getText().toString();
        String pass = password.getText().toString();
        mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                assert userFireBase != null;
                String user1=userFireBase.getUid();
                Toast.makeText(inicio.this,"Welcome",Toast.LENGTH_SHORT).show();
                if (task.isSuccessful()){
                      mDatabase.child("Users").child(user1).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            validationUser(dataSnapshot);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                else{
                    Toast.makeText(inicio.this,"No se pudo iniciar sesion",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //Metodo para validar tipo de usuario
    private void validationUser(@NonNull DataSnapshot dataSnapshot) {
        String di1 = Objects.requireNonNull(dataSnapshot.child("Kind").getValue()).toString();
        Toast.makeText(inicio.this, di1,Toast.LENGTH_SHORT).show();
        String di2="Driver";
        if (di1.equals(di2)) {

            Intent i = new Intent(inicio.this, MapsdriverFragment.class);
            startActivity(i);
        }
       else {
            Intent i = new Intent(inicio.this, Mapsusers.class);
            startActivity(i);
        }
    }

    //Metodo evento boton atras
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Desea salir de la app?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i= new Intent(Intent.ACTION_MAIN);
                    i.addCategory(Intent.CATEGORY_HOME);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();;
                        }
                    });
            builder.show();
        }
    return super.onKeyDown(keyCode, event);
    }


}
