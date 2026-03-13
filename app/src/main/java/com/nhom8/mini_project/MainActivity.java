package com.nhom8.mini_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    private EditText etSearch;

    private RoomAdapter adapter;
    private List<Room> roomList;

    // --- CỬA ĐÓN KẾT QUẢ TỪ FORM THÊM/SỬA (CỦA NGƯỜI B) TRẢ VỀ ---
    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            Room room = (Room) data.getSerializableExtra("ROOM_RESULT");
                            int actionType = data.getIntExtra("ACTION_TYPE", -1); // 1: Thêm, 2: Sửa
                            int position = data.getIntExtra("POSITION", -1);

                            if (room != null) {
                                if (actionType == 1) { // LÀ THÊM
                                    roomList.add(room);
                                    adapter.notifyItemInserted(roomList.size() - 1);
                                    Toast.makeText(this, "Đã thêm phòng thành công!", Toast.LENGTH_SHORT).show();
                                } else if (actionType == 2 && position != -1) { // LÀ SỬA
                                    roomList.set(position, room);
                                    adapter.notifyItemChanged(position);
                                    Toast.makeText(this, "Đã cập nhật phòng!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRooms = findViewById(R.id.rvRooms);
        fabAddRoom = findViewById(R.id.fabAddRoom);
        etSearch = findViewById(R.id.etSearch);

        // 1. CHỨC NĂNG READ: Dữ liệu ảo ban đầu
        roomList = new ArrayList<>();
        roomList.add(new Room("P101", "Phòng Đơn", 2500000, true, "", ""));
        roomList.add(new Room("P102", "Phòng Đôi", 3500000, false, "Trần Văn B", "0912345678"));

        adapter = new RoomAdapter(roomList, new RoomAdapter.OnRoomClickListener() {
            @Override
            public void onRoomClick(Room room, int position) {
                // MỞ FORM SỬA (Người B sẽ tạo AddEditActivity sau)
                try {
                    Intent intent = new Intent(MainActivity.this, Class.forName("com.nhom8.mini_project.AddEditActivity"));
                    intent.putExtra("ROOM_DATA", room); // Gửi dữ liệu phòng đi
                    intent.putExtra("ACTION_TYPE", 2); // Báo là Sửa
                    intent.putExtra("POSITION", position); // Gửi vị trí đi để tí biết mà cập nhật
                    activityResultLauncher.launch(intent);
                } catch (ClassNotFoundException e) {
                    Toast.makeText(MainActivity.this, "Đồng đội chưa tạo file AddEditActivity!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRoomLongClick(Room room, int position) {
                // 2. CHỨC NĂNG DELETE
                showDeleteDialog(position, room);
            }
        });

        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);

        // 3. MỞ FORM THÊM
        fabAddRoom.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MainActivity.this, Class.forName("com.nhom8.mini_project.AddEditActivity"));
                intent.putExtra("ACTION_TYPE", 1); // Báo là Thêm
                activityResultLauncher.launch(intent);
            } catch (ClassNotFoundException e) {
                Toast.makeText(MainActivity.this, "Đồng đội chưa tạo file AddEditActivity!", Toast.LENGTH_SHORT).show();
            }
        });

        // 4. CHỨC NĂNG SEARCH NÂNG CAO
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Room> filteredList = new ArrayList<>();
                for (Room room : roomList) {
                    if (room.getName().toLowerCase().contains(s.toString().toLowerCase()) ||
                            room.getId().toLowerCase().contains(s.toString().toLowerCase())) {
                        filteredList.add(room);
                    }
                }
                adapter.filterList(filteredList);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void showDeleteDialog(int position, Room room) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa phòng " + room.getName() + " không?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                roomList.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, roomList.size());
                Toast.makeText(MainActivity.this, "Đã xóa phòng!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}