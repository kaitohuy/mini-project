package com.nhom8.mini_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity {

    private TextView tvFormTitle;
    private EditText etRoomId, etRoomName, etPrice, etTenantName, etPhone;
    private Switch switchStatus;
    private LinearLayout layoutTenant;
    private Button btnSave, btnCancel;

    private int actionType = 1; // 1: Thêm mới (mặc định), 2: Cập nhật
    private int position = -1; // Vị trí đang sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        initViews();

        // Xử lý sự kiện bật/tắt công tắc tình trạng phòng
        switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchStatus.setText("Tình trạng: ĐÃ THUÊ");
                    layoutTenant.setVisibility(View.VISIBLE); // Hiện chỗ nhập tên
                } else {
                    switchStatus.setText("Tình trạng: CÒN TRỐNG");
                    layoutTenant.setVisibility(View.GONE); // Ẩn chỗ nhập tên
                    etTenantName.setText(""); // Xóa trắng dữ liệu
                    etPhone.setText("");
                }
            }
        });

        // NHẬN DỮ LIỆU TỪ MAIN_ACTIVITY TRUYỀN SANG (KIỂM TRA THÊM HAY SỬA)
        Intent intent = getIntent();
        actionType = intent.getIntExtra("ACTION_TYPE", 1);

        if (actionType == 2) {
            // LÀ TRẠNG THÁI SỬA
            tvFormTitle.setText("CẬP NHẬT PHÒNG TRỌ");
            Room roomEdit = (Room) intent.getSerializableExtra("ROOM_DATA");
            position = intent.getIntExtra("POSITION", -1);

            if (roomEdit != null) {
                // Đổ dữ liệu cũ lên form
                etRoomId.setText(roomEdit.getId());
                etRoomId.setEnabled(false); // Thường mã phòng không cho sửa
                etRoomName.setText(roomEdit.getName());
                etPrice.setText(String.valueOf(roomEdit.getPrice()));

                // Đảo ngược logic isAvailable vì Switch của ta là (Checked = Đã Thuê)
                boolean isRented = !roomEdit.isAvailable();
                switchStatus.setChecked(isRented);

                if (isRented) {
                    etTenantName.setText(roomEdit.getTenantName());
                    etPhone.setText(roomEdit.getPhone());
                }
            }
        }

        // BẤM LƯU: Validate và gửi về MainActivity
        btnSave.setOnClickListener(v -> saveRoomData());

        // BẤM HỦY: Đóng màn hình
        btnCancel.setOnClickListener(v -> finish());
    }

    private void initViews() {
        tvFormTitle = findViewById(R.id.tvFormTitle);
        etRoomId = findViewById(R.id.etRoomId);
        etRoomName = findViewById(R.id.etRoomName);
        etPrice = findViewById(R.id.etPrice);
        etTenantName = findViewById(R.id.etTenantName);
        etPhone = findViewById(R.id.etPhone);
        switchStatus = findViewById(R.id.switchStatus);
        layoutTenant = findViewById(R.id.layoutTenant);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void saveRoomData() {
        String id = etRoomId.getText().toString().trim();
        String name = etRoomName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        boolean isRented = switchStatus.isChecked(); // true = Đã thuê
        String tenant = etTenantName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // VALIDATE (Bắt lỗi bỏ trống)
        if (id.isEmpty() || name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin cơ bản!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isRented && (tenant.isEmpty() || phone.isEmpty())) {
            Toast.makeText(this, "Phòng đã thuê phải có tên và SĐT người thuê!", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = 0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá tiền không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đóng gói thành Object Room mới
        // Lưu ý: isAvailable = !isRented (Vì isAvailable = true nghĩa là Còn trống)
        Room resultRoom = new Room(id, name, price, !isRented, tenant, phone);

        // Ném kết quả về cho MainActivity
        Intent returnIntent = new Intent();
        returnIntent.putExtra("ROOM_RESULT", resultRoom);
        returnIntent.putExtra("ACTION_TYPE", actionType);
        returnIntent.putExtra("POSITION", position);

        setResult(RESULT_OK, returnIntent); // Báo hiệu thành công
        finish(); // Đóng form
    }
}