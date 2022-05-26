package com.company.bda.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.company.bda.HolderMensaje;
import com.company.bda.MensajeRecibir;
import com.company.bda.Model.Mensaje;
import com.company.bda.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MensajesAdapter extends RecyclerView.Adapter<HolderMensaje> {

    private List<MensajeRecibir> mensajeList = new ArrayList<>();
    private Context context;

    public MensajesAdapter(Context context) {
        this.context = context;
    }

    public void addMensaje(MensajeRecibir m){
        mensajeList.add(m);
        notifyItemInserted(mensajeList.size());
    }

    @NonNull
    @Override
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_view_mensajes,parent,false);
        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {
        holder.getNombre().setText(mensajeList.get(position).getNombre());
        holder.getMensaje().setText(mensajeList.get(position).getMensaje());
        if (mensajeList.get(position).getTypeMensaje().equals("2")){
            holder.getFotoMensaje().setVisibility(View.VISIBLE);
            holder.getMensaje().setVisibility(View.VISIBLE);
            Glide.with(context).load(mensajeList.get(position).getUrlFoto()).into(holder.getFotoMensaje());

        }else if (mensajeList.get(position).getTypeMensaje().equals("1")){
            holder.getFotoMensaje().setVisibility(View.GONE);
            holder.getMensaje().setVisibility(View.GONE);
        }
        if (mensajeList.get(position).getFotoPerfil().isEmpty()){
            holder.getFotoMensajePerfil().setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(mensajeList.get(position).getFotoPerfil()).into(holder.getFotoMensajePerfil());
        }

        Long codigoHora = mensajeList.get(position).getHora();
        Date d = new Date(codigoHora);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        holder.getHora().setText(sdf.format(d));


    }

    @Override
    public int getItemCount() {
        return mensajeList.size();
    }
}
