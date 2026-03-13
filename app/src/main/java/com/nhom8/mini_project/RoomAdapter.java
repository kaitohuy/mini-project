package com.nhom8.mini_project;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomList;
    private OnRoomClickListener listener;

    public interface OnRoomClickListener {
        void onRoomClick(Room room, int position);
        void onRoomLongClick(Room room, int position);
    }

    public RoomAdapter(List<Room> roomList, OnRoomClickListener listener) {
        this.roomList = roomList;
        this.listener = listener;
    }

    // HÀM TÌM KIẾM (Cho tính năng Nâng cao)
    public void filterList(List<Room> filteredList) {
        this.roomList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        if (room == null) return;

        holder.tvRoomName.setText(room.getId() + " - " + room.getName());
        holder.tvPrice.setText("Giá: " + room.getPrice() + " VNĐ");

        // XỬ LÝ MÀU SẮC THEO ĐỀ BÀI
        if (room.isAvailable()) {
            holder.tvStatus.setText("CÒN TRỐNG");
            holder.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50")); // Màu Xanh lá
            holder.layoutTenantInfo.setVisibility(View.GONE);
        } else {
            holder.tvStatus.setText("ĐÃ THUÊ");
            holder.tvStatus.setBackgroundColor(Color.parseColor("#F44336")); // Màu Đỏ
            holder.layoutTenantInfo.setVisibility(View.VISIBLE);
            holder.tvTenantName.setText("Người thuê: " + room.getTenantName());
            holder.tvPhone.setText("SĐT: " + room.getPhone());
        }

        // Sự kiện gửi ra MainActivity
        holder.itemView.setOnClickListener(v -> listener.onRoomClick(room, position));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onRoomLongClick(room, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return roomList != null ? roomList.size() : 0;
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvStatus, tvPrice, tvTenantName, tvPhone;
        LinearLayout layoutTenantInfo;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTenantName = itemView.findViewById(R.id.tvTenantName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            layoutTenantInfo = itemView.findViewById(R.id.layoutTenantInfo);
        }
    }
}