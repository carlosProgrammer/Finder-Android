package com.neuronatech.finder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class LoginActivity extends AppCompatActivity {

    private ImageView logoIcon;
    private ProgressBar loadingProgressBar;
    private RelativeLayout loginView, afterAnimationView;
    Button loginBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });



        initViews();
        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                loadingProgressBar.setVisibility(GONE);
                loginView.setBackgroundColor(ContextCompat.getColor(
                        LoginActivity.this, R.color.colorBackground));
                logoIcon.setImageResource(R.drawable.finder_logo);
                startAnimation();
            }

        }.start();
    }

    public void openNewActivity(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void initViews(){
        logoIcon = findViewById(R.id.logoIcon);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        loginView = findViewById(R.id.loginView);
        afterAnimationView = findViewById(R.id.afterAnimationView);

    }
    private void startAnimation() {
        ViewPropertyAnimator viewPropertyAnimator = logoIcon.animate();
        viewPropertyAnimator.x(50f);
        viewPropertyAnimator.y(100f);
        viewPropertyAnimator.setDuration(1000);
        viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                afterAnimationView.setVisibility(VISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
}