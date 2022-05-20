package com.company.bda.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.company.bda.Model.User;
import com.company.bda.R;

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
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imagenPerfilUsuario;
        public TextView tipo, nombreUsuario, emailUsuario, telefono, grupoSangre;
        Button emailNow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imagenPerfilUsuario = itemView.findViewById(R.id.userProfileImage);
            tipo = itemView.findViewById(R.id.type);
            telefono = itemView.findViewById(R.id.phoneNumber);
            nombreUsuario = itemView.findViewById(R.id.userName);
            emailUsuario = itemView.findViewById(R.id.userEmail);
            grupoSangre = itemView.findViewById(R.id.bloodGroup);
            emailNow = itemView.findViewById(R.id.emailNow);

        }
    }
}
