package ca.bcit.globalmathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity {


    private TextView tv_questionLabel;
    private TextView tv_question;
    private Button[] choices;
    private TextView tv_score;
    private Button btn_skip;
    private boolean finished=false;

    private NotificationManager nm;

    Random r = new Random();
    private MathQuestion[] questions;

    private int qNum;
    private int curScore;

    public static final int NUMQUESTIONS =5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        qNum =0;
        curScore=0;

        findViews();
        generateQuiz();
        renderQuestion();

    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    private void findViews() {
        tv_questionLabel = findViewById(R.id.tv_questionLabel);
        tv_question = findViewById(R.id.tv_question);

        choices = new Button[3];
        choices[0] = findViewById(R.id.btn_ans1);
        choices[1] = findViewById(R.id.btn_ans2);
        choices[2] = findViewById(R.id.btn_ans3);
        btn_skip = findViewById(R.id.btn_skip);

        tv_score = findViewById(R.id.tv_score);
    }

    private void generateQuiz() {
        questions = new MathQuestion[NUMQUESTIONS];
        for (int i=0; i<NUMQUESTIONS; i++) {
            questions[i] = new MathQuestion(r.nextInt(4));
        }
    }

    private void renderQuestion() {
        MathQuestion cur = questions[qNum];

        String qLabel = getResources().getString(R.string.questionLabel);
        tv_questionLabel.setText(qLabel + " "+ (qNum+1));
        tv_question.setText(cur.getLeftOp()+" "+cur.getOperation()+" "+cur.getRightOp());

        int correctBtn = r.nextInt(3);
        for (int i=0; i<3; i++) {
            if (i==correctBtn) {
                choices[i].setText(""+cur.getCorrectAnswer());
            } else {
                choices[i].setText(""+cur.randomAnswer());
            }
        }

        tv_score.setText(curScore+" / "+NUMQUESTIONS);
    }

    public void makeGuess(View view) {
        if (finished) return;
        if (qNum<NUMQUESTIONS) {
            Button b = (Button) view;
            int guess = Integer.parseInt(b.getText().toString());
            guess(guess);

            if (qNum<NUMQUESTIONS-1) {
                nextQuestion();
            } else {
                tv_score.setText(curScore+" / "+NUMQUESTIONS);
                finishGame();
            }
        } else {
            finishGame();

        }
    }

    private void guess(int guess) {
        if (guess == questions[qNum].getCorrectAnswer()) {
            curScore++;
            Toast.makeText(this, R.string.correct,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.wrong,Toast.LENGTH_SHORT).show();
        }
    }

    public void skipQuestion(View v) {
        if (qNum<NUMQUESTIONS-1) {
            nextQuestion();
        } else {
            finishGame();
        }
    }

    private void nextQuestion() {
        qNum++;
        renderQuestion();
    }

    public void resetQuiz(View view) {
        qNum=0;
        curScore=0;
        generateQuiz();
        renderQuestion();
        btn_skip.setText(R.string.skip);
        finished=false;
    }

    private void finishGame() {
        for (Button btn:choices) {
            btn.setText("--");
        }
        btn_skip.setText("--");
        finished=true;

        // notification
        makeNotification();
    }

    private void makeNotification() {
        Intent intent = new Intent(this, GameActivity.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setContentTitle("New mail from " + "test@gmail.com")
                .setContentText("Subject")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();


        nm.notify(0, n);
    }
}
