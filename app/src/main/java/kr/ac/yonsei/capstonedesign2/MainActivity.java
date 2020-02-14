package kr.ac.yonsei.capstonedesign2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private RadarChart radarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDrawerLayout();

        radarChartSetting();
    }

    //토글버튼 클릭시 드로여 열리고 닫힘
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /** --------------- Methods ---------------------*/
    private void setDrawerLayout() {
        // 드로어 네비게이션 토글기능
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.drawer_showChart:

                        break;
                    case R.id.drawer_bluetooth:
                        break;
                    case R.id.drawer_basicSetting:
                        break;
                    case R.id.drawer_logOut:
                        break;
                }
                return true;
            }
        });
        // 네이게이션 드로어 아이콘 색상 추가
        navigationView.setItemIconTintList(null);
    }

    private void radarChartSetting() {
        radarChart = findViewById(R.id.radarChart);

        ArrayList<RadarEntry> dataValues1 = new ArrayList<>();
        dataValues1.add(new RadarEntry(4));
        dataValues1.add(new RadarEntry(12));
        dataValues1.add(new RadarEntry(6));
        dataValues1.add(new RadarEntry(7));
        dataValues1.add(new RadarEntry(8));
        dataValues1.add(new RadarEntry(11));
        dataValues1.add(new RadarEntry(10));
        dataValues1.add(new RadarEntry(9));

        RadarDataSet dataSet1 = new RadarDataSet(dataValues1, "Data 1");

        dataSet1.setColor(Color.RED);
        dataSet1.setFillColor(Color.RED);
        dataSet1.setDrawFilled(true);
        dataSet1.setFillAlpha(180);
        dataSet1.setLineWidth(2f);

        RadarData radarData = new RadarData();
        radarData.addDataSet(dataSet1);

        String[] labels = {"EMG1", "EMG2", "EMG3", "EMG4", "EMG5", "EMG6", "EMG7", "EMG8"};

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        radarChart.setData(radarData);
    }

}
