package com.example.laptopcontroller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laptopcontroller.Network.ping;
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

        holder.nickname.setText(devices.getNick_name());
        holder.ip=devices.getAddress();
        holder.id=devices.getId();
        ping.PortPingListener listener = new ping.PortPingListener() {
            @Override
            public void onPortPingResult(boolean isPortOpen) {
                holder.state.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isPortOpen) {
                            holder.state.setText(R.string.Status);
                            holder.state.setTextColor(Color.GREEN);
                            holder.allow=true;
                        }
                        else {
                            holder.allow=false;
                        }
                    }
                });
                           }
        };
        ping portPinger = new ping(holder.ip, 1983, listener);
        Thread thread = new Thread(portPinger);
        thread.start();
    }

    // How many items?
    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView nickname,state;
        public String ip;
        public int id;
        public boolean allow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            nickname = itemView.findViewById(R.id.nickname);
            state=itemView.findViewById(R.id.state);
        }

        @Override
        public void onClick(View view) {
            Intent dashboard=new Intent(view.getContext(),Dashobard.class);
            dashboard.putExtra("Ip",ip);
            dashboard.putExtra("device_name",nickname.getText().toString());
            dashboard.putExtra("Id",id);

            if(allow){
            context.startActivity(dashboard);

                Vibrator v = (Vibrator) itemView.getContext().getSystemService(itemView.getContext().VIBRATOR_SERVICE);
                v.vibrate(VibrationEffect.createOneShot(100,VibrationEffect.DEFAULT_AMPLITUDE));


            }
            else {
                context.startActivity(dashboard);

                Toast.makeText(itemView.getContext(), "Offline hai nahi dikh raha kya",Toast.LENGTH_SHORT).show();
            }
        }

    }
}

