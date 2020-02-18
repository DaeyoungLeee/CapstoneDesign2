package kr.ac.yonsei.capstonedesign2.DrawerLayout_Contents;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kr.ac.yonsei.capstonedesign2.R;

public class Maximum1_Fragment extends Fragment {

    private ProgressBar progressBar;
    private TextView txt_guide;

    // Firebase
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ChildEventListener mChildListener;

    // Compare data
    float e1, e2, e3, e4, e5, e6, e7, e8 = 0f;
    float e1s, e2s, e3s, e4s, e5s, e6s, e7s, e8s = 0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maximum1, container, false);
        progressBar = v.findViewById(R.id.progress_maximum1);
        txt_guide = v.findViewById(R.id.txt_max1_guide);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("user_id")
                .child("data");

        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

                if (e1 < emg1) {
                    e1 = emg1;
                    e1s = emg1;
                }
                if (e2 < emg2) {
                    e2 = emg2;
                    e2s = emg2;
                }
                if (e3 < emg3) {
                    e3 = emg3;
                    e3s = emg3;
                }
                if (e4 < emg4) {
                    e4 = emg4;
                    e4s = emg4;
                }
                if (e5 < emg5) {
                    e5 = emg5;
                    e5s = emg5;
                }
                if (e6 < emg6) {
                    e6 = emg6;
                    e6s = emg6;
                }
                if (e7 < emg7) {
                    e7 = emg7;
                    e7s = emg7;
                }
                if (e8 < emg8) {
                    e8 = emg8;
                    e8s = emg8;
                }

                mDatabase.getReference("user_id")
                        .child("maximum1")
                        .child("EMG")
                        .setValue(e1s + ":" + e2s + ":" + e3s + ":" + e4s + ":" + e5s + ":" + e6s + ":" + e7s + ":" + e8s);
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
        };

        progressBar.setMax(400);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ProgressBarAsyncTask progressBarAsyncTask = new ProgressBarAsyncTask();
                        progressBarAsyncTask.execute();
                        txt_guide.setText("Put maximum strength in your muscle");
                        txt_guide.setTextSize(32);
                        mRef.addChildEventListener(mChildListener);
                    }
                }, 2000);
            }
        }).start();

        return v;
    }

    public class ProgressBarAsyncTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            for (int i = 0; i < 401; i++) {
                try {
                    publishProgress(i);
                    i++;
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            if (values[0] < 200) {

            }else {
                mRef.removeEventListener(mChildListener);
            }
            if (values[0] == 200) {
                txt_guide.setText("Relax your muscle");
            }
            if (values[0] == 400) {
                txt_guide.setText("Done");
            }
            progressBar.setProgress(values[0]);
            super.onProgressUpdate(values);
        }
    }
}
