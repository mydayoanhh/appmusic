package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/** @noinspection ALL*/
public class DangNhap extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1001; // requestCode cho intent đăng nhập Google
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    private EditText email, pass;
    private TextView txtLogin,txtnSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhap);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.edtEU);
        pass = findViewById(R.id.edtpass);
        txtLogin = findViewById(R.id.txtLogin);
        txtnSignin = findViewById(R.id.txtSignin);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        txtnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });

        // Khởi tạo GoogleSignInClient
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Lắng nghe sự kiện click trên ImageView để đăng nhập Google
        ImageView imageViewGoogle = findViewById(R.id.gg);
        imageViewGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    // Phương thức để bắt đầu quá trình đăng nhập bằng Google
    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Xử lý kết quả từ hoạt động đăng nhập Google
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            try {
                // Nhận kết quả đăng nhập từ intent
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);

                // Đăng nhập vào Firebase Authentication
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                // Xử lý khi đăng nhập thất bại
                Toast.makeText(this, "Đăng nhập thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userName = user.getDisplayName();
                            String userEmail = user.getEmail();
                            Intent intent = new Intent(DangNhap.this, TrangChu.class);
                            startActivity(intent);
                            finish(); // Optional: Finish the current activity to remove it from the back stack
                            Toast.makeText(DangNhap.this, "Đăng nhập thành công: " + userName, Toast.LENGTH_SHORT).show();

                        } else {
                            // Đăng nhập thất bại
                            Toast.makeText(DangNhap.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void login() {
        String edtemail, edtpass;
        edtemail = email.getText().toString().trim();
        edtpass = pass.getText().toString().trim();
        if (TextUtils.isEmpty(edtemail) || TextUtils.isEmpty(edtpass)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ email và mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(edtemail, edtpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DangNhap.this, TrangChu.class);
                    startActivity(intent);
                    finish(); // Finish this activity after successful login
                } else {
                    Toast.makeText(getApplicationContext(), "Đăng nhập không thành công.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signin() {
        String edemail, edpass;
        edemail = email.getText().toString().trim();
        edpass = pass.getText().toString().trim();
        if (TextUtils.isEmpty(edemail) || TextUtils.isEmpty(edpass)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ email và mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(edemail, edpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Tạo tài khoản thành công.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DangNhap.this, TrangChu.class);
                    startActivity(intent);
                    finish(); // Finish this activity after successful registration
                } else {
                    Toast.makeText(getApplicationContext(), "Tạo tài khoản không thành công.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
