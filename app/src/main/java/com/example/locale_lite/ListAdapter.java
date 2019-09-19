package com.example.locale_lite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locale_lite.ui.dashboard.DashboardFragment;
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
        holder.info.setText(serviceProviders.getPhonenum());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mcontext instanceof Main2Activity){
                    Intent intent = new Intent(mcontext,Chat.class);
                    intent.putExtra("name",serviceProviders.getFirstname()+" "+serviceProviders.getLastname());
                    intent.putExtra("dp",serviceProviders.getProfilePicUrl());
                    intent.putExtra("category",serviceProviders.getCategory());
                    intent.putExtra("phone",serviceProviders.getPhonenum());
                    intent.putExtra("userid",serviceProviders.getId());
                    intent.putExtra("type","ServiceProvider");
                    mcontext.startActivity(intent);
                }
                if(mcontext instanceof Requests){
                    Intent intent = new Intent(mcontext,ShowSentRequest.class);
                    intent.putExtra("name",serviceProviders.getFirstname()+" "+serviceProviders.getLastname());
                    intent.putExtra("dp",serviceProviders.getProfilePicUrl());
                    intent.putExtra("category",serviceProviders.getCategory());
                    intent.putExtra("phone",serviceProviders.getPhonenum());
                    intent.putExtra("userid",serviceProviders.getId());
                    intent.putExtra("type","ServiceProvider");
                    mcontext.startActivity(intent);
                }
                if(mcontext instanceof selectProviders) {
                    Intent intent = new Intent(mcontext, profileSP.class);
                    intent.putExtra("name", serviceProviders.getFirstname() + " " + serviceProviders.getLastname());
                    intent.putExtra("dp", serviceProviders.getProfilePicUrl());
                    intent.putExtra("category", serviceProviders.getCategory());
                    intent.putExtra("phone", serviceProviders.getPhonenum());
                    intent.putExtra("userid", serviceProviders.getId());
                    intent.putExtra("numRate", serviceProviders.getNumrate());
                    intent.putExtra("totalRate", serviceProviders.getTotalrate());
                    intent.putExtra("avRate", serviceProviders.getAvrate());
                    Bundle b = new Bundle();
                    b.putDouble("spLat", serviceProviders.getLatitude());
                    b.putDouble("spLng", serviceProviders.getLongitude());
                    intent.putExtras(b);
                    mcontext.startActivity(intent);
                }
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
        public TextView info;

        public ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.name_list);
            profilepic = itemView.findViewById(R.id.providers_image);
            info = itemView.findViewById(R.id.info_list);
        }
    }
}
