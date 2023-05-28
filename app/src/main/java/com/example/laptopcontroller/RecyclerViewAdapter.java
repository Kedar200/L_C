package com.example.laptopcontroller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laptopcontroller.data.Devices;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Devices> deviceList;

    public RecyclerViewAdapter(Context context, List<Devices> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    // Where to get the single card as viewholder Object
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    // What will happen after we create the viewholder object
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Devices devices = deviceList.get(position);

        holder.contactName.setText(devices.getNick_name());
        holder.phoneNumber.setText(devices.getAddress());

    }

    // How many items?
    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView contactName;
        public TextView phoneNumber;
        public ImageView iconButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            contactName = itemView.findViewById(R.id.nickname);
            phoneNumber = itemView.findViewById(R.id.address);

        }

        @Override
        public void onClick(View view) {
            Log.d("ClickFromViewHolder", "Clicked");
        }
    }
}

