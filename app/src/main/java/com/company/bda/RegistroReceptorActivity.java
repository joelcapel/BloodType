package com.company.bda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistroReceptorActivity extends AppCompatActivity {

    private TextView backButton;
    private CircleImageView imagen_perfil;
    private TextInputEditText nombreRegistro, numeroRegistro, telefonoRegistro, emailRegistro, passwordRegistro;
    private Spinner bloodGroupsSpinner;
    private Button botonRegistro;
    private Uri resultUri;
    private ProgressDialog loader;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_receptor);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistroReceptorActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        imagen_perfil = findViewById(R.id.profile_image);
        nombreRegistro = findViewById(R.id.registerFullName);
        numeroRegistro = findViewById(R.id.registerIdNumber);
        telefonoRegistro = findViewById(R.id.registerPhoneNumber);
        emailRegistro = findViewById(R.id.registerEmail);
        passwordRegistro = findViewById(R.id.registerPassword);
        bloodGroupsSpinner = findViewById(R.id.bloodGroupsSpinner);
        botonRegistro = findViewById(R.id.registerButton);
        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        imagen_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailRegistro.getText().toString().trim();
                final String password = passwordRegistro.getText().toString().trim();
                final String fullname = nombreRegistro.getText().toString().trim();
                final String idNumber = numeroRegistro.getText().toString().trim();
                final String phoneNumber = telefonoRegistro.getText().toString().trim();
                final String bloodGroup = bloodGroupsSpinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(email)){
                    emailRegistro.setError("El correo es obligatorio!");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    emailRegistro.setError("La contraseña es obligatorio!");
                    return;
                }
                if (TextUtils.isEmpty(fullname)){
                    emailRegistro.setError("El nombvre es obligatorio!");
                    return;
                }
                if (TextUtils.isEmpty(idNumber)){
                    emailRegistro.setError("El DNI es obligatorio!");
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber)){
                    emailRegistro.setError("El teléfono es obligatorio!");
                    return;
                }
                if (bloodGroup.equals("Selecciona tu grupo de sangre")){
                    Toast.makeText(RegistroReceptorActivity.this, "Selecciona el grupo de sangre", Toast.LENGTH_SHORT).show();
                    return;
                }

                else {
                    loader.setMessage("Registrando...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()){
                                String error = task.getException().toString();
                                Toast.makeText(RegistroReceptorActivity.this, "Error" + error, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String currenUserId = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(currenUserId);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currenUserId);
                                userInfo.put("nombre", fullname);
                                userInfo.put("email", email);
                                userInfo.put("dni", idNumber);
                                userInfo.put("telefono", phoneNumber);
                                userInfo.put("gruposangre", bloodGroup);
                                userInfo.put("type", "receptor");
                                userInfo.put("buscar", "receptor"+bloodGroup);

                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(RegistroReceptorActivity.this, "Datos introducidos correctamente", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(RegistroReceptorActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        finish();
                                        //loader.dismiss();
                                    }
                                });

                                if (resultUri !=null){
                                    final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                            .child("profile images").child(currenUserId);
                                    Bitmap bitmap = null;

                                    try{
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                    byte[] data = byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask = filePath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegistroReceptorActivity.this, "Imagen subida", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            if (taskSnapshot.getMetadata() !=null && taskSnapshot.getMetadata().getReference() != null){
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl = uri.toString();
                                                        Map newImageMap = new HashMap();
                                                        newImageMap.put("profilepictureurl", imageUrl);

                                                        userDatabaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(RegistroReceptorActivity.this, "URL de la imagen añadida a la BBDD correctamente", Toast.LENGTH_SHORT).show();
                                                                }else{
                                                                    Toast.makeText(RegistroReceptorActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                        finish();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    Intent intent = new Intent(RegistroReceptorActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    loader.dismiss();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode == RESULT_OK && data !=null){
            resultUri = data.getData();
            imagen_perfil.setImageURI(resultUri);
        }
    }
}