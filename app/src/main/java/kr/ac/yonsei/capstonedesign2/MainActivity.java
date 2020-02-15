package kr.ac.yonsei.capstonedesign2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private RadarChart radarChart;

    // Bluetooth
    private BluetoothSPP bluetooth;

    //Firebase
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Drawer layout setting
        drawerLayoutSetting();

        // Radar Chart setting
        //radarChartSetting();

        // Bluetooth Setting
        bluetoothSetting();

        // get emg realtime data from firebase
        firebaseData();
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

    private void radarChartEMGData(float emg1, float emg2, float emg3, float emg4, float emg5, float emg6, float emg7, float emg8) {
        radarChart = findViewById(R.id.radarChart);

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
        radarChart.invalidate();
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

                radarChartEMGData(emg1, emg2, emg3, emg4, emg5, emg6, emg7, emg8);
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

                radarChartEMGData(emg1, emg2, emg3, emg4, emg5, emg6, emg7, emg8);
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

    /** ====================== Methods End =========================== */

    /** ====================== Life Cycle =========================== */

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

    /** ====================== Life Cycle End =========================== */
}
