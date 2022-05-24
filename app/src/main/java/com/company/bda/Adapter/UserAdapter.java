package com.company.bda.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.company.bda.Email.JavaMailApi;
import com.company.bda.MainActivity;
import com.company.bda.MainMensajes;
import com.company.bda.Model.User;
import com.company.bda.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_displayed_layout, parent, false);
    return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = userList.get(position);

        holder.tipo.setText(user.getType());

        if (user.getType().equals("donante")){
            holder.emailNow.setVisibility(View.VISIBLE);
        }

        holder.emailUsuario.setText(user.getEmail());
        holder.nombreUsuario.setText(user.getNombre());
        holder.grupoSangre.setText(user.getGruposangre());
        holder.telefono.setText(user.getTelefono());

        Glide.with(context).load(user.getProfilepictureurl()).into(holder.imagenPerfilUsuario);

        final String nombreDelReceptor = user.getNombre();
        final String idDelReceptor = user.getId();

        //enviando email

        holder.emailNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("ENVIAR EMAIL")
                        .setMessage("Enviar email a " + user.getNombre() + "?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String nombreEnvio = snapshot.child("nombre").getValue().toString();
                                        String email = snapshot.child("email").getValue().toString();
                                        String telefono = snapshot.child("telefono").getValue().toString();
                                        String sangre = snapshot.child("gruposangre").getValue().toString();

                                        String mEmail = user.getEmail();
                                        String mAsunto = "DONACION DE SANGRE";
                                        String mMensaje = "Hola "+  nombreDelReceptor +", " + nombreEnvio+
                                                "Quiere recibir una donacion tuya. Aqui estan sus datos: \n"
                                                +"Nombre: " + nombreEnvio+ "\n"+
                                                "Numero de telefono: "+ telefono+ "\n"+
                                                "Email: "+ email +"\n"+
                                                "Grupo sanguineo: "+ sangre +"\n"+
                                                "Amablemente comuniquese con él/ella. Gracias!\n"+
                                                "BLOOD TYPE - DONAR SANGRE, SALVA VIDAS!";

                                        JavaMailApi javaMailApi = new JavaMailApi(context, mEmail, mAsunto, mMensaje);
                                        javaMailApi.execute();

                                        DatabaseReference envioRef = FirebaseDatabase.getInstance().getReference("emails")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        envioRef.child(idDelReceptor).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    DatabaseReference receptorRef = FirebaseDatabase.getInstance().getReference("emails")
                                                            .child(idDelReceptor);
                                                    receptorRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);

                                                    addNotifications(idDelReceptor, FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                }
                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imagenPerfilUsuario;
        public TextView tipo, nombreUsuario, emailUsuario, telefono, grupoSangre;
        Button emailNow,msgNow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imagenPerfilUsuario = itemView.findViewById(R.id.userProfileImage);
            tipo = itemView.findViewById(R.id.type);
            telefono = itemView.findViewById(R.id.phoneNumber);
            nombreUsuario = itemView.findViewById(R.id.userName);
            emailUsuario = itemView.findViewById(R.id.userEmail);
            grupoSangre = itemView.findViewById(R.id.bloodGroup);
            emailNow = itemView.findViewById(R.id.emailNow);
            msgNow = itemView.findViewById(R.id.msgNow);

        }
    }

    private void addNotifications(String receptorId, String envioId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("notifications").child(receptorId);
        String date = DateFormat.getDateInstance().format(new Date());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("receptorId", receptorId);
        hashMap.put("envioId", envioId);
        hashMap.put("texto", "Te envió un correo, eche un vistazo!");
        hashMap.put("date", date);

        reference.push().setValue(hashMap);
    }
}
