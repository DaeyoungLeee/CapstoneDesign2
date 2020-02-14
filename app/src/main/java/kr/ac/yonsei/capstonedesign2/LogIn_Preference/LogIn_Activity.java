package kr.ac.yonsei.capstonedesign2.LogIn_Preference;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kr.ac.yonsei.capstonedesign2.R;

public class LogIn_Activity extends AppCompatActivity {
    // UI element
    Button btn_signIn, btn_logIn;
    EditText edt_id, edt_password;
    CheckBox check_id, check_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UI element
        btn_logIn = findViewById(R.id.btn_logIn);
        btn_signIn = findViewById(R.id.btn_signIn);
        edt_id = findViewById(R.id.edt_loginId);
        edt_password = findViewById(R.id.edt_loginPassword);
        check_id = findViewById(R.id.checkbox_rememberId);
        check_password = findViewById(R.id.checkbox_rememberPassword);

    }
}
