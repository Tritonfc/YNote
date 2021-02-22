package com.eararchitect.ynote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> {
    private List<Schedule> schedules = new ArrayList<>();
    private ScheduleAdapter.onItemClickListener listener;

    @NonNull
    @Override
    public ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_items, parent, false);
        return new ScheduleHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleHolder holder, int position) {
        Schedule current = schedules.get(position);
        holder.className.setText(current.getClassName());
        holder.startTime.setText(current.getStartTime());
        holder.endTime.setText(current.getEndTime());
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
        notifyDataSetChanged();
    }

    class ScheduleHolder extends RecyclerView.ViewHolder {

        private TextView className;
        private TextView startTime;
        private TextView endTime;

        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.class_name);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(schedules.get(position));

                    }

                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Schedule schedule);
    }


    public void setOnItemClickListener(ScheduleAdapter.onItemClickListener listener) {
        this.listener = listener;
    }

}
