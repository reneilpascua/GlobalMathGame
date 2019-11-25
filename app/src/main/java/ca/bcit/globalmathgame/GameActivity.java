package ca.bcit.globalmathgame;

import androidx.appcompat.app.AppCompatActivity;

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

    Random r = new Random();
    private MathQuestion[] questions;

    private int qNum;
    private int curScore;

    public static final int NUMQUESTIONS =5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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

        tv_questionLabel.setText("Question "+ (qNum+1));
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
        Button b = (Button) view;
        int guess = Integer.parseInt(b.getText().toString());

        if (guess == questions[qNum].getCorrectAnswer()) {
            curScore++;
            Toast.makeText(this, R.string.correct,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.wrong,Toast.LENGTH_SHORT).show();
        }

        qNum++;

        if (qNum == NUMQUESTIONS) { // game has finished
            qNum=0;
            curScore=0;
            generateQuiz();
        }
        renderQuestion();

    }
}
