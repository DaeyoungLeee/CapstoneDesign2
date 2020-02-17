package kr.ac.yonsei.capstonedesign2.DrawerLayout_Contents;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import kr.ac.yonsei.capstonedesign2.R;

import static kr.ac.yonsei.capstonedesign2.DrawerLayout_Contents.Maximum2_Fragment.mChildListener2;
import static kr.ac.yonsei.capstonedesign2.DrawerLayout_Contents.Maximum2_Fragment.mRef2;
import static kr.ac.yonsei.capstonedesign2.DrawerLayout_Contents.Maximum2_Fragment.txt_guide2;
import static kr.ac.yonsei.capstonedesign2.DrawerLayout_Contents.Maximum3_Fragment.mChildListener3;
import static kr.ac.yonsei.capstonedesign2.DrawerLayout_Contents.Maximum3_Fragment.mRef3;
import static kr.ac.yonsei.capstonedesign2.DrawerLayout_Contents.Maximum3_Fragment.txt_guide3;

public class MaximumMuscle_Activity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private ImageButton nextButton;
    private int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_maximum_muscle);

        List<Fragment> list = new ArrayList<>();

        list.add(new Maximum1_Fragment());
        list.add(new Maximum2_Fragment());
        list.add(new Maximum3_Fragment());

        viewPager = findViewById(R.id.viewPager_maximum);
        pagerAdapter = new ViewPager_Adapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(pagerAdapter);

        nextButton = findViewById(R.id.imgbtn_maximum_finish);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    viewPager.setCurrentItem(1);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Maximum2_Fragment.ProgressBarAsyncTask2 progressBarAsyncTask = new Maximum2_Fragment.ProgressBarAsyncTask2();
                                    progressBarAsyncTask.execute();
                                    txt_guide2.setText("Put maximum strength in your muscle");
                                    txt_guide2.setTextSize(32);
                                    mRef2.addChildEventListener(mChildListener2);
                                }
                            }, 1000);
                        }
                    }).start();

                }
                else if (i == 1) {
                    viewPager.setCurrentItem(2);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Maximum3_Fragment.ProgressBarAsyncTask3 progressBarAsyncTask = new Maximum3_Fragment.ProgressBarAsyncTask3();
                                    progressBarAsyncTask.execute();
                                    txt_guide3.setText("Put maximum strength in your muscle");
                                    txt_guide3.setTextSize(32);
                                    mRef3.addChildEventListener(mChildListener3);
                                }
                            }, 1000);
                        }
                    }).start();

                }
                else if (i == 2){
                    Toast.makeText(MaximumMuscle_Activity.this, "Maximum Measurement is done", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                i = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
