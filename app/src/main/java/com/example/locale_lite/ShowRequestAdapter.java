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

import com.example.locale_lite.ui.dashboard.DashboardFragmentSP;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowRequestAdapter extends RecyclerView.Adapter<ShowRequestAdapter.ViewHolder> {

    private Context mcontext;
    private List<Customers> musers;
    private String status;

    public ShowRequestAdapter(Context mcontext, List<Customers> musers,String status){
        this.musers = musers;
        this.mcontext = mcontext;
        this.status = status;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.list_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Customers c = musers.get(position);
        holder.username.setText(c.getFirstname()+" "+c.getLastname());
        holder.profilepic.setImageResource(R.drawable.cuslogo);
        holder.phone.setText(c.getPhonenum());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status.equals("pending")) {
                    Intent intent = new Intent(mcontext, ShowPendingRequest.class);
                    intent.putExtra("type", "Customer");
                    intent.putExtra("name", c.getFirstname() + " " + c.getLastname());
                    intent.putExtra("phone", c.getPhonenum());
                    intent.putExtra("userid", c.getId());
                    Bundle b = new Bundle();
                    b.putDouble("cusLat", c.getLatitude());
                    b.putDouble("cusLng", c.getLongitude());
                    intent.putExtras(b);
                    mcontext.startActivity(intent);
                }
                if (status.equals("active")) {
                    Intent intent = new Intent(mcontext, ShowActiveRequests.class);
                    intent.putExtra("type", "Customer");
                    intent.putExtra("name", c.getFirstname() + " " + c.getLastname());
                    intent.putExtra("phone", c.getPhonenum());
                    intent.putExtra("userid", c.getId());
                    Bundle b = new Bundle();
                    b.putDouble("cusLat", c.getLatitude());
                    b.putDouble("cusLng", c.getLongitude());
                    intent.putExtras(b);
                    mcontext.startActivity(intent);
                }
                if (status.equals("active")) {
                    Intent intent = new Intent(mcontext, ShowActiveRequests.class);
                    intent.putExtra("type", "Customer");
                    intent.putExtra("name", c.getFirstname() + " " + c.getLastname());
                    intent.putExtra("phone", c.getPhonenum());
                    intent.putExtra("userid", c.getId());
                    mcontext.startActivity(intent);
                }
                if (status.equals("completed")) {
                    Intent intent = new Intent(mcontext, ShowCompletedRequest.class);
                    intent.putExtra("type", "Customer");
                    intent.putExtra("name", c.getFirstname() + " " + c.getLastname());
                    intent.putExtra("phone", c.getPhonenum());
                    intent.putExtra("userid", c.getId());
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
        public TextView phone;

        public ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.name_list);
            profilepic = itemView.findViewById(R.id.providers_image);
            phone = itemView.findViewById(R.id.info_list);
        }
    }
}
