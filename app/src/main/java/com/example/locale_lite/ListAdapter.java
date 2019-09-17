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

import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Context mcontext;
    private List<ServiceProviders> musers;

    public ListAdapter(Context mcontext, List<ServiceProviders> musers){
        this.musers = musers;
        this.mcontext = mcontext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.list_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ServiceProviders serviceProviders = musers.get(position);
        holder.username.setText(serviceProviders.getFirstname()+" "+serviceProviders.getLastname());
        Picasso.with(mcontext).load(serviceProviders.getProfilePicUrl()).into(holder.profilepic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,profileSP.class);
                intent.putExtra("name",serviceProviders.getFirstname()+" "+serviceProviders.getLastname());
                intent.putExtra("dp",serviceProviders.getProfilePicUrl());
                intent.putExtra("category",serviceProviders.getCategory());
                intent.putExtra("phone",serviceProviders.getPhonenum());
                intent.putExtra("userid",serviceProviders.getId());
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return musers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profilepic;

        public ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.name_list);
            profilepic = itemView.findViewById(R.id.providers_image);
        }
    }
}
