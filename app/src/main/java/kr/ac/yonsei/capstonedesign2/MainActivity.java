package kr.ac.yonsei.capstonedesign2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import kr.ac.yonsei.capstonedesign2.DrawerLayout_Contents.BasicSetting_Activity;

import static kr.ac.yonsei.capstonedesign2.DrawerLayout_Contents.BasicSetting_Activity.ARMBAND_DIRECTION;
import static kr.ac.yonsei.capstonedesign2.DrawerLayout_Contents.BasicSetting_Activity.ARMBAND_LEFT;
import static kr.ac.yonsei.capstonedesign2.DrawerLayout_Contents.BasicSetting_Activity.BASIC_SETTING;
import static kr.ac.yonsei.capstonedesign2.DrawerLayout_Contents.BasicSetting_Activity.SEEKBAR_SEC;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private RadarChart radarChart;
    private Switch switch_activePattern, switch_startRecord;

    // Bluetooth
    private BluetoothSPP bluetooth;

    // Firebase
    private FirebaseDatabase mDatabase;

    // Flag
    private boolean showChartFlag = false;

    // setting values
    private int sec_values;
    private String armband_direction;

    // Data average String
    private float emg1_max, emg2_max, emg3_max, emg4_max, emg5_max, emg6_max, emg7_max, emg8_max = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sec_values = secValue();
        armband_direction = armDirectionValue();
        makeToast("direction = " + armband_direction + "  sec = " + sec_values);

        // Drawer layout setting
        drawerLayoutSetting();

        // Bluetooth Setting
        bluetoothSetting();

        // get emg realtime data from firebase
        radarChart = findViewById(R.id.radarChart);
        firebaseData();

        // switch state
        switch_activePattern = findViewById(R.id.sw_active);
        if (!switch_activePattern.isChecked()) {
            radarChartNothing(radarChart);
        }
        switch_activePattern.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showChartFlag = true;
                } else {
                    showChartFlag = false;
                }
            }
        });

    }

    //토글버튼 클릭시 드로여 열리고 닫힘
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * --------------- Methods ---------------------
     */
    private void drawerLayoutSetting() {
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
                        connectBluetooth();
                        break;
                    case R.id.drawer_basicSetting:
                        Intent intent = new Intent(MainActivity.this, BasicSetting_Activity.class);
                        startActivity(intent);
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

    private void radarChartEMGData(RadarChart radarChart,
                                   float emg1,
                                   float emg2,
                                   float emg3,
                                   float emg4,
                                   float emg5,
                                   float emg6,
                                   float emg7,
                                   float emg8) {

        RadarData radarData = new RadarData();

        ArrayList<RadarEntry> dataValues = new ArrayList<>();
        dataValues.add(new RadarEntry(emg1));
        dataValues.add(new RadarEntry(emg2));
        dataValues.add(new RadarEntry(emg3));
        dataValues.add(new RadarEntry(emg4));
        dataValues.add(new RadarEntry(emg5));
        dataValues.add(new RadarEntry(emg6));
        dataValues.add(new RadarEntry(emg7));
        dataValues.add(new RadarEntry(emg8));

        RadarDataSet dataSet = new RadarDataSet(dataValues, "EMG data");

        dataSet.setFillColor(Color.BLUE);
        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(100);

        radarData.addDataSet(dataSet);

        //-------emg max average--------//

        averageOfMaximumEMG();

        ArrayList<RadarEntry> dataValuesSample = new ArrayList<>();
        dataValuesSample.add(new RadarEntry(emg1_max));
        dataValuesSample.add(new RadarEntry(emg2_max));
        dataValuesSample.add(new RadarEntry(emg3_max));
        dataValuesSample.add(new RadarEntry(emg4_max));
        dataValuesSample.add(new RadarEntry(emg5_max));
        dataValuesSample.add(new RadarEntry(emg6_max));
        dataValuesSample.add(new RadarEntry(emg7_max));
        dataValuesSample.add(new RadarEntry(emg8_max));

        RadarDataSet dataSetSample = new RadarDataSet(dataValuesSample, "EMG Sample");

        dataSetSample.setColor(Color.BLUE);
        dataSetSample.setLineWidth(3f);

        radarData.addDataSet(dataSetSample);


        // ------ setting!!! -------//

        String[] labels = {"EMG1", "EMG2", "EMG3", "EMG4", "EMG5", "EMG6", "EMG7", "EMG8"};

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        YAxis yAxis = radarChart.getYAxis();

        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(60);

        radarChart.setRotationEnabled(false);
        radarChart.invalidate();

        if (showChartFlag) {
            radarChart.setData(radarData);
        } else {
            radarChartNothing(radarChart);
        }
    }

    private void radarChartNothing(RadarChart radarChart) {
        ArrayList<RadarEntry> dataValues = new ArrayList<>();
        dataValues.add(new RadarEntry(0));
        dataValues.add(new RadarEntry(0));
        dataValues.add(new RadarEntry(0));
        dataValues.add(new RadarEntry(0));
        dataValues.add(new RadarEntry(0));
        dataValues.add(new RadarEntry(0));
        dataValues.add(new RadarEntry(0));
        dataValues.add(new RadarEntry(0));

        RadarDataSet dataSet = new RadarDataSet(dataValues, "EMG data");

        dataSet.setFillColor(Color.BLUE);
        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(200);

        RadarData radarData = new RadarData();
        radarData.addDataSet(dataSet);

        String[] labels = {"EMG1", "EMG2", "EMG3", "EMG4", "EMG5", "EMG6", "EMG7", "EMG8"};

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        YAxis yAxis = radarChart.getYAxis();

        yAxis.setAxisMinimum(0);
        //yAxis.setAxisMaximum(60);

        radarChart.setRotationEnabled(false);
        radarChart.setData(radarData);
    }

    private void radarChartGuideLine(RadarChart radarChart) {
        ArrayList<RadarEntry> dataValues = new ArrayList<>();
        dataValues.add(new RadarEntry(30));
        dataValues.add(new RadarEntry(35));
        dataValues.add(new RadarEntry(23));
        dataValues.add(new RadarEntry(37));
        dataValues.add(new RadarEntry(20));
        dataValues.add(new RadarEntry(50));
        dataValues.add(new RadarEntry(25));
        dataValues.add(new RadarEntry(30));

        RadarDataSet dataSet = new RadarDataSet(dataValues, "EMG data");

        dataSet.setColor(Color.BLUE);
        dataSet.setDrawFilled(true);

        RadarData radarData = new RadarData();
        radarData.addDataSet(dataSet);

        String[] labels = {"EMG1", "EMG2", "EMG3", "EMG4", "EMG5", "EMG6", "EMG7", "EMG8"};

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        YAxis yAxis = radarChart.getYAxis();

        yAxis.setAxisMinimum(0);
        //yAxis.setAxisMaximum(60);

        radarChart.setRotationEnabled(false);
        radarChart.setData(radarData);
    }

    private void bluetoothSetting() {
        bluetooth = new BluetoothSPP(getApplicationContext());

        // When bluetooth couldn't use
        if (!bluetooth.isBluetoothAvailable()) {
            makeSnackbar("Bluetooth is not available");
            finish();
        }

        // About Bluetooth Connection State
        bluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            @Override
            public void onDeviceConnected(String name, String address) {
                makeToast("Connected to " + name + "\n" + address);
            }

            @Override
            public void onDeviceDisconnected() {
                makeToast("Connection lost");
            }

            @Override
            public void onDeviceConnectionFailed() {
                makeToast("Unable to connect");
            }
        });

        // Processing Received Data from Bluetooth
        bluetooth.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {

            }
        });
    }

    private void connectBluetooth() {
        if (bluetooth.getServiceState() == BluetoothState.STATE_CONNECTED) {
            bluetooth.disconnect();
        } else {
            Intent intent = new Intent(getApplicationContext(), DeviceList.class);
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
        }
    }

    private void firebaseData() {
        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference("user_id").child("data").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String data = dataSnapshot.getValue(String.class);
                String dataS = data.replace("[", "")
                        .replace("]", "")
                        .replace(" ", "");
                String[] dataArray = dataS.split(",");
                float emg1 = Float.parseFloat(dataArray[0]);
                float emg2 = Float.parseFloat(dataArray[1]);
                float emg3 = Float.parseFloat(dataArray[2]);
                float emg4 = Float.parseFloat(dataArray[3]);
                float emg5 = Float.parseFloat(dataArray[4]);
                float emg6 = Float.parseFloat(dataArray[5]);
                float emg7 = Float.parseFloat(dataArray[6]);
                float emg8 = Float.parseFloat(dataArray[7]);

                radarChartEMGData(radarChart, emg1, emg2, emg3, emg4, emg5, emg6, emg7, emg8);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String data = dataSnapshot.getValue(String.class);
                String dataS = data.replace("[", "")
                        .replace("]", "")
                        .replace(" ", "");
                String[] dataArray = dataS.split(",");
                float emg1 = Float.parseFloat(dataArray[0]);
                float emg2 = Float.parseFloat(dataArray[1]);
                float emg3 = Float.parseFloat(dataArray[2]);
                float emg4 = Float.parseFloat(dataArray[3]);
                float emg5 = Float.parseFloat(dataArray[4]);
                float emg6 = Float.parseFloat(dataArray[5]);
                float emg7 = Float.parseFloat(dataArray[6]);
                float emg8 = Float.parseFloat(dataArray[7]);

                radarChartEMGData(radarChart, emg1, emg2, emg3, emg4, emg5, emg6, emg7, emg8);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void makeSnackbar(String string) {
        Snackbar.make(getWindow().getDecorView().getRootView(), string, Snackbar.LENGTH_LONG).show();
    }

    private void makeToast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }

    private int secValue() {
        SharedPreferences sharedPreferences = getSharedPreferences(BASIC_SETTING, MODE_PRIVATE);
        int secValue = sharedPreferences.getInt(SEEKBAR_SEC, 1);
        return secValue;
    }

    private String armDirectionValue() {
        SharedPreferences sharedPreferences = getSharedPreferences(BASIC_SETTING, MODE_PRIVATE);
        String direction = sharedPreferences.getString(ARMBAND_DIRECTION, ARMBAND_LEFT);
        return direction;
    }

    private void averageOfMaximumEMG() {
        mDatabase.getReference("user_id")
                .child("maximum1")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        final String max1 = dataSnapshot.getValue(String.class);
                        final String[] emg_max1 = max1.split(":");
                        final float e_m1_1 = Float.parseFloat(emg_max1[0]);
                        final float e_m1_2 = Float.parseFloat(emg_max1[1]);
                        final float e_m1_3 = Float.parseFloat(emg_max1[2]);
                        final float e_m1_4 = Float.parseFloat(emg_max1[3]);
                        final float e_m1_5 = Float.parseFloat(emg_max1[4]);
                        final float e_m1_6 = Float.parseFloat(emg_max1[5]);
                        final float e_m1_7 = Float.parseFloat(emg_max1[6]);
                        final float e_m1_8 = Float.parseFloat(emg_max1[7]);

                        mDatabase.getReference("user_id")
                                .child("maximum2")
                                .addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                        String max2 = dataSnapshot.getValue(String.class);
                                        String[] emg_max2 = max2.split(":");
                                        final float e_m2_1 = Float.parseFloat(emg_max2[0]);
                                        final float e_m2_2 = Float.parseFloat(emg_max2[1]);
                                        final float e_m2_3 = Float.parseFloat(emg_max2[2]);
                                        final float e_m2_4 = Float.parseFloat(emg_max2[3]);
                                        final float e_m2_5 = Float.parseFloat(emg_max2[4]);
                                        final float e_m2_6 = Float.parseFloat(emg_max2[5]);
                                        final float e_m2_7 = Float.parseFloat(emg_max2[6]);
                                        final float e_m2_8 = Float.parseFloat(emg_max2[7]);

                                        mDatabase.getReference("user_id")
                                                .child("maximum3")
                                                .addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                        String max3 = dataSnapshot.getValue(String.class);
                                                        String[] emg_max3 = max3.split(":");
                                                        float e_m3_1 = Float.parseFloat(emg_max3[0]);
                                                        float e_m3_2 = Float.parseFloat(emg_max3[1]);
                                                        float e_m3_3 = Float.parseFloat(emg_max3[2]);
                                                        float e_m3_4 = Float.parseFloat(emg_max3[3]);
                                                        float e_m3_5 = Float.parseFloat(emg_max3[4]);
                                                        float e_m3_6 = Float.parseFloat(emg_max3[5]);
                                                        float e_m3_7 = Float.parseFloat(emg_max3[6]);
                                                        float e_m3_8 = Float.parseFloat(emg_max3[7]);

                                                        float av_e1 = (e_m1_1 + e_m2_1 + e_m3_1) / 3;
                                                        float av_e2 = (e_m1_2 + e_m2_2 + e_m3_2) / 3;
                                                        float av_e3 = (e_m1_3 + e_m2_3 + e_m3_3) / 3;
                                                        float av_e4 = (e_m1_4 + e_m2_4 + e_m3_4) / 3;
                                                        float av_e5 = (e_m1_5 + e_m2_5 + e_m3_5) / 3;
                                                        float av_e6 = (e_m1_6 + e_m2_6 + e_m3_6) / 3;
                                                        float av_e7 = (e_m1_7 + e_m2_7 + e_m3_7) / 3;
                                                        float av_e8 = (e_m1_8 + e_m2_8 + e_m3_8) / 3;

                                                        emg1_max = av_e1;
                                                        emg2_max = av_e2;
                                                        emg3_max = av_e3;
                                                        emg4_max = av_e4;
                                                        emg5_max = av_e5;
                                                        emg6_max = av_e6;
                                                        emg7_max = av_e7;
                                                        emg8_max = av_e8;

                                                        mDatabase.getReference("user_id")
                                                                .child("maximum_average")
                                                                .child("EMG")
                                                                .setValue(av_e1 + ":" + av_e2 + "" + av_e3 + ":" + av_e4 + ":" + av_e5 + ":" + av_e6 + ":" + av_e7 + ":" + av_e8);
                                                    }

                                                    @Override
                                                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                                    }

                                                    @Override
                                                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                                    }

                                                    @Override
                                                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                    }

                                    @Override
                                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /** ====================== Methods End =========================== */

    /**
     * ====================== Life Cycle ===========================
     */

    @Override
    protected void onStart() {
        super.onStart();
        if (!bluetooth.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetooth.stopService();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sec_values = secValue();
        armband_direction = armDirectionValue();
        makeToast("direction = " + armband_direction + "  sec = " + sec_values);
        averageOfMaximumEMG();
    }

    /** ====================== Life Cycle End =========================== */
}
