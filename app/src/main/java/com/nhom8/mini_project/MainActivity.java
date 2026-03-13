package com.nhom8.mini_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvRooms;
    private FloatingActionButton fabAddRoom;
    private RoomAdapter adapter;
    private List<Room> roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRooms = findViewById(R.id.rvRooms);
        fabAddRoom = findViewById(R.id.fabAddRoom);

        // 1. CHỨC NĂNG READ: Khởi tạo dữ liệu giả và gắn vào RecyclerView
        roomList = new ArrayList<>();
        roomList.add(new Room("P101", "Phòng Đơn", 2500000, true, "", ""));
        roomList.add(new Room("P102", "Phòng Đôi", 3500000, false, "Trần Văn B", "0912345678"));

        adapter = new RoomAdapter(roomList, new RoomAdapter.OnRoomClickListener() {
            @Override
            public void onRoomClick(Room room, int position) {
                // TODO: BẠN KIA SẼ LÀM UPDATE Ở ĐÂY
                // Intent mở AddEditActivity kèm dữ liệu phòng để sửa
                Toast.makeText(MainActivity.this, "Sắp tới bạn kia sẽ mở form sửa phòng: " + room.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRoomLongClick(Room room, int position) {
                // 2. CHỨC NĂNG DELETE: Hiển thị AlertDialog xác nhận xóa
                showDeleteDialog(position, room);
            }
        });

        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);

        // Nút bấm THÊM MỚI (Dành cho bạn kia xử lý)
        fabAddRoom.setOnClickListener(v -> {
            // TODO: BẠN KIA SẼ LÀM CREATE Ở ĐÂY
            // Intent mở AddEditActivity trắng để thêm
            Toast.makeText(MainActivity.this, "Sắp tới bạn kia sẽ làm form Thêm Mới!", Toast.LENGTH_SHORT).show();
        });
    }

    private void showDeleteDialog(int position, Room room) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa " + room.getName() + " không?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xóa khỏi List
                roomList.remove(position);
                // Cập nhật lại RecyclerView
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, roomList.size());
                Toast.makeText(MainActivity.this, "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}