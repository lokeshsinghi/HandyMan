package com.example.locale_lite;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {


    private Context mcontext;
    private List<RequestBox> mchat;
    private String imageurl;
    FirebaseUser fuser;

    public RequestAdapter(Context mcontext, List<RequestBox> mchat, String imageurl){
        this.mchat = mchat;
        this.mcontext = mcontext;
        this.imageurl = imageurl;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.request_item_right, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestBox requestBox = mchat.get(position);
        holder.show_date.setText((requestBox.getDate()));
        holder.show_time.setText((requestBox.getTime()));
        holder.show_des.setText((requestBox.getDescription()));
        holder.show_status.setText((requestBox.getStatus()));

    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_date;
        public TextView show_time;
        public TextView show_des;
        public TextView show_status;

        public ViewHolder(View itemView){
            super(itemView);

            show_date = itemView.findViewById(R.id.show_date);
            show_time = itemView.findViewById(R.id.show_time);
            show_des = itemView.findViewById(R.id.show_description);
            show_status = itemView.findViewById(R.id.show_status);
        }
    }
}
