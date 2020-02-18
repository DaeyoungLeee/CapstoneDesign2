package kr.ac.yonsei.capstonedesign2.DrawerLayout_Contents;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kr.ac.yonsei.capstonedesign2.R;

public class Set2_Activity extends AppCompatActivity implements View.OnClickListener {

    int count = 1;
    int i_rest, i_lateral, i_cylinder, i_palmar, i_v, i_index, i_thumb = 0;
    float e1, e2, e3, e4, e5, e6, e7, e8 = 0f;
    float el1, el2, el3, el4, el5, el6, el7, el8 = 0f;
    float ec1, ec2, ec3, ec4, ec5, ec6, ec7, ec8 = 0f;
    float ep1, ep2, ep3, ep4, ep5, ep6, ep7, ep8 = 0f;
    float ev1, ev2, ev3, ev4, ev5, ev6, ev7, ev8 = 0f;
    float ei1, ei2, ei3, ei4, ei5, ei6, ei7, ei8 = 0f;
    float et1, et2, et3, et4, et5, et6, et7, et8 = 0f;


    // UI
    private ImageButton nextButton;
    private ProgressBar progressBar;
    private TextView txt_guide;
    private ImageView img_guide;

    // Firebase
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef, mRef_set1_rest, mRef_set1_lateral, mRef_set1_cylinder, mRef_set1_palmar, mRef_set1_v, mRef_set1_index, mRef_set1_thumb;
    private ChildEventListener mChild_rest, mChild_lateral, mChild_cylinder, mChild_palmar, mChild_v, mChild_index, mChild_thumb;

    // Posture Flag
    private boolean flag_rest, flag_lateral, flag_cylinder, flag_palmar, flag_v, flag_index, flag_thumb = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_2);

        nextButton = findViewById(R.id.imgbtn_set2_next);
        progressBar = findViewById(R.id.progress_set2);
        txt_guide = findViewById(R.id.txt_set2_guide);
        img_guide = findViewById(R.id.img_set2_guide);

        progressBar.setMax(400);

        nextButton.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance();

        mRef = mDatabase.getReference("user_id")
                .child("data");

        mRef_set1_rest = mDatabase.getReference("user_id")
                .child("data")
                .child("rest2");
        mRef_set1_lateral = mDatabase.getReference("user_id")
                .child("data")
                .child("lateral_pinch2");
        mRef_set1_cylinder = mDatabase.getReference("user_id")
                .child("data")
                .child("cylinder_grasp2");
        mRef_set1_palmar = mDatabase.getReference("user_id")
                .child("data")
                .child("palmar_pinch2");
        mRef_set1_v = mDatabase.getReference("user_id")
                .child("data")
                .child("v2");
        mRef_set1_index = mDatabase.getReference("user_id")
                .child("data")
                .child("index2");
        mRef_set1_thumb = mDatabase.getReference("user_id")
                .child("data")
                .child("thumb_up2");

        mChild_rest = new ChildEventListener() {
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

                e1 += emg1;
                e2 += emg2;
                e3 += emg3;
                e4 += emg4;
                e5 += emg5;
                e6 += emg6;
                e7 += emg7;
                e8 += emg8;

                i_rest++;

                mDatabase.getReference("user_id")
                        .child("rest_average2")
                        .child("EMG")
                        .setValue(e1 / i_rest + ":" + e2 / i_rest + ":" + e3 / i_rest + ":" + e4 / i_rest + ":" + e5 / i_rest + ":" + e6 / i_rest + ":" + e7 / i_rest + ":" + e8 / i_rest);
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
        mChild_lateral= new ChildEventListener() {
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

                el1 += emg1;
                el2 += emg2;
                el3 += emg3;
                el4 += emg4;
                el5 += emg5;
                el6 += emg6;
                el7 += emg7;
                el8 += emg8;

                i_lateral++;

                mDatabase.getReference("user_id")
                        .child("lateral_average2")
                        .child("EMG")
                        .setValue(el1 / i_lateral + ":" + el2 / i_lateral+ ":" + el3 / i_lateral+ ":" + el4 / i_lateral+ ":" + el5 / i_lateral+ ":" + el6 / i_lateral+ ":" + el7 / i_lateral+ ":" + el8 / i_lateral);
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
        mChild_cylinder = new ChildEventListener() {
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

                ec1 += emg1;
                ec2 += emg2;
                ec3 += emg3;
                ec4 += emg4;
                ec5 += emg5;
                ec6 += emg6;
                ec7 += emg7;
                ec8 += emg8;

                i_cylinder++;

                mDatabase.getReference("user_id")
                        .child("cylinder_average2")
                        .child("EMG")
                        .setValue(ec1 / i_cylinder + ":" + ec2 / i_cylinder + ":" + ec3 / i_cylinder + ":" + ec4 / i_cylinder + ":" + ec5 / i_cylinder + ":" + ec6 / i_cylinder + ":" + ec7 / i_cylinder + ":" + ec8 / i_cylinder);
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
        mChild_palmar = new ChildEventListener() {
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

                ep1 += emg1;
                ep2 += emg2;
                ep3 += emg3;
                ep4 += emg4;
                ep5 += emg5;
                ep6 += emg6;
                ep7 += emg7;
                ep8 += emg8;

                i_palmar++;

                mDatabase.getReference("user_id")
                        .child("palmar_average2")
                        .child("EMG")
                        .setValue(ep1 / i_palmar + ":" + ep2 / i_palmar + ":" + ep3 / i_palmar + ":" + ep4 / i_palmar + ":" + ep5 / i_palmar + ":" + ep6 / i_palmar + ":" + ep7 / i_palmar + ":" + ep8 / i_palmar);
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
        mChild_v = new ChildEventListener() {
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

                ev1 += emg1;
                ev2 += emg2;
                ev3 += emg3;
                ev4 += emg4;
                ev5 += emg5;
                ev6 += emg6;
                ev7 += emg7;
                ev8 += emg8;

                i_v++;

                mDatabase.getReference("user_id")
                        .child("v_average2")
                        .child("EMG")
                        .setValue(ev1 / i_v + ":" + ev2 / i_v + ":" + ev3 / i_v + ":" + ev4 / i_v + ":" + ev5 / i_v + ":" + ev6 / i_v + ":" + ev7 / i_v + ":" + ev8 / i_v);
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
        mChild_index = new ChildEventListener() {
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

                ei1 += emg1;
                ei2 += emg2;
                ei3 += emg3;
                ei4 += emg4;
                ei5 += emg5;
                ei6 += emg6;
                ei7 += emg7;
                ei8 += emg8;

                i_index++;

                mDatabase.getReference("user_id")
                        .child("index_average2")
                        .child("EMG")
                        .setValue(ei1 / i_index + ":" + ei2 / i_index + ":" + ei3 / i_index + ":" + ei4 / i_index + ":" + ei5 / i_index + ":" + ei6 / i_index + ":" + ei7 / i_index + ":" + ei8 / i_index);
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
        mChild_thumb = new ChildEventListener() {
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

                et1 += emg1;
                et2 += emg2;
                et3 += emg3;
                et4 += emg4;
                et5 += emg5;
                et6 += emg6;
                et7 += emg7;
                et8 += emg8;

                i_thumb++;

                mDatabase.getReference("user_id")
                        .child("thumb_up_average2")
                        .child("EMG")
                        .setValue(et1 / i_thumb + ":" + et2 / i_thumb + ":" + et3 / i_thumb + ":" + et4 / i_thumb + ":" + et5 / i_thumb + ":" + et6 / i_thumb + ":" + et7 / i_thumb + ":" + et8 / i_thumb);
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

        img_guide.setImageResource(R.drawable.rest_posture);
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

                        mRef.addChildEventListener(mChild_rest);
                    }
                }, 2000);
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        count ++;
        if (count == 1) {

        }else if (count == 2) {
            flag_rest = false;
            flag_lateral = true;
            flag_cylinder = false;
            flag_palmar = false;
            flag_v = false;
            flag_index = false;
            flag_thumb = false;

            img_guide.setImageResource(R.drawable.lateral_pinch_posture);
        }else if (count == 3) {
            flag_rest = false;
            flag_lateral = false;
            flag_cylinder = true;
            flag_palmar = false;
            flag_v = false;
            flag_index = false;
            flag_thumb = false;

            img_guide.setImageResource(R.drawable.cylinder_grasp_posture);
        }else if (count == 4) {
            flag_rest = false;
            flag_lateral = false;
            flag_cylinder = false;
            flag_palmar = true;
            flag_v = false;
            flag_index = false;
            flag_thumb = false;

            img_guide.setImageResource(R.drawable.palmar_pinch_posture);
        }else if (count == 5) {
            flag_rest = false;
            flag_lateral = false;
            flag_cylinder = false;
            flag_palmar = false;
            flag_v = true;
            flag_index = false;
            flag_thumb = false;

            img_guide.setImageResource(R.drawable.v_posture);
        }else if (count == 6) {
            flag_rest = false;
            flag_lateral = false;
            flag_cylinder = false;
            flag_palmar = false;
            flag_v = false;
            flag_index = true;
            flag_thumb = false;

            img_guide.setImageResource(R.drawable.index_posture);
        }else if (count == 7) {
            flag_rest = false;
            flag_lateral = false;
            flag_cylinder = false;
            flag_palmar = false;
            flag_v = false;
            flag_index = false;
            flag_thumb = true;

            img_guide.setImageResource(R.drawable.thumb_up_posture);
        }else {
            finish();
        }

        if (flag_rest) {
            firebaseRecordStart(mChild_rest);
        }else if (flag_lateral) {
            firebaseRecordStart(mChild_lateral);
        }else if (flag_cylinder) {
            firebaseRecordStart(mChild_cylinder);
        }else if (flag_palmar) {
            firebaseRecordStart(mChild_palmar);
        }else if (flag_v) {
            firebaseRecordStart(mChild_v);
        }else if (flag_index) {
            firebaseRecordStart(mChild_index);
        }else if (flag_thumb) {
            firebaseRecordStart(mChild_thumb);
        }
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
                mRef.removeEventListener(mChild_rest);
                mRef.removeEventListener(mChild_cylinder);
                mRef.removeEventListener(mChild_index);
                mRef.removeEventListener(mChild_lateral);
                mRef.removeEventListener(mChild_palmar);
                mRef.removeEventListener(mChild_thumb);
                mRef.removeEventListener(mChild_v);
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

    /** Methods */

    private void firebaseRecordStart(final ChildEventListener eventListener) {
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

                        mRef.addChildEventListener(eventListener);
                    }
                }, 2000);
            }
        }).start();
    }
}
