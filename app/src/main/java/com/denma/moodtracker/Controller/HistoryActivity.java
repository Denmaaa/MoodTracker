package com.denma.moodtracker.Controller;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denma.moodtracker.Model.DailyMood;
import com.denma.moodtracker.Model.DailyMoodDAO;
import com.denma.moodtracker.R;

import java.util.ArrayList;
import java.util.List;

import static com.denma.moodtracker.Model.DailyMoodDAO.KEY_ID_MOOD;
import static com.denma.moodtracker.Model.DailyMoodDAO.KEY_MOOD_COMMENTARY;
import static com.denma.moodtracker.Model.DailyMoodDAO.KEY_MOOD_STAT;

public class HistoryActivity extends AppCompatActivity {

    private RelativeLayout mDay7Layout;
    private TextView mDay7Mood;
    private ImageView mDay7Commentary;

    private RelativeLayout mDay6Layout;
    private TextView mDay6Mood;
    private ImageView mDay6Commentary;

    private RelativeLayout mDay5Layout;
    private TextView mDay5Mood;
    private ImageView mDay5Commentary;

    private RelativeLayout mDay4Layout;
    private TextView mDay4Mood;
    private ImageView mDay4Commentary;

    private RelativeLayout mDay3Layout;
    private TextView mDay3Mood;
    private ImageView mDay3Commentary;

    private RelativeLayout mDay2Layout;
    private TextView mDay2Mood;
    private ImageView mDay2Commentary;

    private RelativeLayout mDay1Layout;
    private TextView mDay1Mood;
    private ImageView mDay1Commentary;

    private List<DailyMood> mWeekMoodList = new ArrayList<>();
    private TextView[] mMoodTable;
    private ImageView[] mCommentaryTable;
    private RelativeLayout[] mLayoutTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Reference view's items
        mDay7Layout = (RelativeLayout) findViewById(R.id.activity_history_day_7_layout);
        mDay7Mood = (TextView) findViewById(R.id.activity_history_day_7_mood);
        mDay7Commentary = (ImageView) findViewById(R.id.activity_history_day_7_commentary);

        mDay6Layout = (RelativeLayout) findViewById(R.id.activity_history_day_6_layout);
        mDay6Mood = (TextView) findViewById(R.id.activity_history_day_6_mood);
        mDay6Commentary = (ImageView) findViewById(R.id.activity_history_day_6_commentary);

        mDay5Layout = (RelativeLayout) findViewById(R.id.activity_history_day_5_layout);
        mDay5Mood = (TextView) findViewById(R.id.activity_history_day_5_mood);
        mDay5Commentary = (ImageView) findViewById(R.id.activity_history_day_5_commentary);

        mDay4Layout = (RelativeLayout) findViewById(R.id.activity_history_day_4_layout);
        mDay4Mood = (TextView) findViewById(R.id.activity_history_day_4_mood);
        mDay4Commentary = (ImageView) findViewById(R.id.activity_history_day_4_commentary);

        mDay3Layout = (RelativeLayout) findViewById(R.id.activity_history_day_3_layout);
        mDay3Mood = (TextView) findViewById(R.id.activity_history_day_3_mood);
        mDay3Commentary = (ImageView) findViewById(R.id.activity_history_day_3_commentary);

        mDay2Layout = (RelativeLayout) findViewById(R.id.activity_history_day_2_layout);
        mDay2Mood = (TextView) findViewById(R.id.activity_history_day_2_mood);
        mDay2Commentary = (ImageView) findViewById(R.id.activity_history_day_2_commentary);

        mDay1Layout = (RelativeLayout) findViewById(R.id.activity_history_day_1_layout);
        mDay1Mood = (TextView) findViewById(R.id.activity_history_day_1_mood);
        mDay1Commentary = (ImageView) findViewById(R.id.activity_history_day_1_commentary);

        // Init table that allow us to dynamically set our view's item
        mMoodTable = new TextView[]{mDay1Mood, mDay2Mood, mDay3Mood, mDay4Mood, mDay5Mood, mDay6Mood, mDay7Mood};
        mCommentaryTable = new ImageView[]{mDay1Commentary, mDay2Commentary, mDay3Commentary, mDay4Commentary, mDay5Commentary, mDay6Commentary, mDay7Commentary};
        mLayoutTable = new RelativeLayout[]{mDay1Layout, mDay2Layout, mDay3Layout, mDay4Layout, mDay5Layout, mDay6Layout, mDay7Layout};

        // Set Commentary Invisble
        mDay1Commentary.setVisibility(View.INVISIBLE);
        mDay2Commentary.setVisibility(View.INVISIBLE);
        mDay3Commentary.setVisibility(View.INVISIBLE);
        mDay4Commentary.setVisibility(View.INVISIBLE);
        mDay5Commentary.setVisibility(View.INVISIBLE);
        mDay6Commentary.setVisibility(View.INVISIBLE);
        mDay7Commentary.setVisibility(View.INVISIBLE);

        // Set Listener on commentary
        mDay1Commentary.setOnClickListener(CommentaryListener);
        mDay2Commentary.setOnClickListener(CommentaryListener);
        mDay3Commentary.setOnClickListener(CommentaryListener);
        mDay4Commentary.setOnClickListener(CommentaryListener);
        mDay5Commentary.setOnClickListener(CommentaryListener);
        mDay6Commentary.setOnClickListener(CommentaryListener);
        mDay7Commentary.setOnClickListener(CommentaryListener);

        // Open database + connect with DAO
        DailyMoodDAO dM = new DailyMoodDAO(this);
        dM.open();

        // Get last 7 DailyMood (maximum, but you can get less depending on your data)
        Cursor cursor = dM.getLast7DailyMood();
        if (cursor.moveToFirst()) {
            do {
                // Create and Init a temporary DailyMood Object
                DailyMood tempDailyMood = new DailyMood(0, "", "");
                tempDailyMood.setId_dailyMood(cursor.getInt(cursor.getColumnIndex(KEY_ID_MOOD)));
                tempDailyMood.setDailyMood(cursor.getString(cursor.getColumnIndex(KEY_MOOD_STAT)));
                tempDailyMood.setDailyCommentary(cursor.getString(cursor.getColumnIndex(KEY_MOOD_COMMENTARY)));

                // Init our list with those Objects -> we can now use them easily
                mWeekMoodList.add(tempDailyMood);
            }while (cursor.moveToNext());
        }

        // Always close door that we open
        cursor.close();
        dM.close();

        // Modify Xml properties to suit our DailyMood Objects
        setXmlFromData(mWeekMoodList);
    }

    // Toast the commentary depending on wich ImageView user clicked
    private View.OnClickListener CommentaryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String message = "";

            switch (v.getId()) {
                case R.id.activity_history_day_1_commentary:
                    message = mWeekMoodList.get(0).getDailyCommentary();
                    Toast.makeText(HistoryActivity.this, message, Toast.LENGTH_LONG).show();
                    break;
                case R.id.activity_history_day_2_commentary:
                    message = mWeekMoodList.get(1).getDailyCommentary();
                    Toast.makeText(HistoryActivity.this, message, Toast.LENGTH_LONG).show();
                    break;
                case R.id.activity_history_day_3_commentary:
                    message = mWeekMoodList.get(2).getDailyCommentary();
                    Toast.makeText(HistoryActivity.this, message, Toast.LENGTH_LONG).show();
                    break;
                case R.id.activity_history_day_4_commentary:
                    message = mWeekMoodList.get(3).getDailyCommentary();
                    Toast.makeText(HistoryActivity.this, message, Toast.LENGTH_LONG).show();
                    break;
                case R.id.activity_history_day_5_commentary:
                    message = mWeekMoodList.get(4).getDailyCommentary();
                    Toast.makeText(HistoryActivity.this, message, Toast.LENGTH_LONG).show();
                    break;
                case R.id.activity_history_day_6_commentary:
                    message = mWeekMoodList.get(5).getDailyCommentary();
                    Toast.makeText(HistoryActivity.this, message, Toast.LENGTH_LONG).show();
                    break;
                case R.id.activity_history_day_7_commentary:
                    message = mWeekMoodList.get(6).getDailyCommentary();
                    Toast.makeText(HistoryActivity.this, message, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    // Dynamically set the mood text and set the commentary visible if it exist
    private void setXmlFromData(List<DailyMood> WeekMoodList){

        for(int i = 0; i < WeekMoodList.size(); i ++){
            mMoodTable[i].setText(WeekMoodList.get(i).getDailyMood());
            if(!(WeekMoodList.get(i).getDailyCommentary().equals("")))
                mCommentaryTable[i].setVisibility(View.VISIBLE);

            // Call our method to set RelativeLayout depending on his mood correspondance
            setWidthForMoodLayout(WeekMoodList.get(i), mLayoutTable[i]);
        }
    }

    // Dynamically set the RelativeLayout width and color depending on mood
    private void setWidthForMoodLayout(DailyMood currentMood, RelativeLayout currentLayout){
        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int happySize = (int) (screenWidth * 0.85);
        int normalSize = (int) (screenWidth * 0.70);
        int disappointedSize = (int) (screenWidth * 0.55);
        int sadSize = (int) (screenWidth * 0.30);

        String userMood = currentMood.getDailyMood();

        switch (userMood){
            case ":D":
                currentLayout.getLayoutParams().width = screenWidth;
                currentLayout.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                break;
            case ":)":
                currentLayout.getLayoutParams().width = happySize;
                currentLayout.setBackgroundColor(getResources().getColor(R.color.light_sage));
                break;
            case ":|":
                currentLayout.getLayoutParams().width = normalSize;
                currentLayout.setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                break;
            case ":/":
                currentLayout.getLayoutParams().width = disappointedSize;
                currentLayout.setBackgroundColor(getResources().getColor(R.color.warm_grey));
                break;
            case ":(":
                currentLayout.getLayoutParams().width = sadSize;
                currentLayout.setBackgroundColor(getResources().getColor(R.color.faded_red));
                break;
        }
    }
}
