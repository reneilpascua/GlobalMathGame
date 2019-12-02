package ca.bcit.globalmathgame

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*

class KotlinGameActivity : AppCompatActivity() {


    private var tv_questionLabel: TextView? = null
    private var tv_question: TextView? = null
    private lateinit var choices: Array<Button?>
    private var tv_score: TextView? = null
    private var btn_skip: Button? = null
    private var finished = false

    val CHANNEL_1_ID = "channel1"

    private var nmc: NotificationManagerCompat? = null

    internal var r = Random()
    private lateinit var questions: Array<MathQuestion?>

    private var qNum: Int = 0
    private var curScore: Int = 0

    val NUMQUESTIONS = 5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_game)

        createNotificationChannels()
        nmc = NotificationManagerCompat.from(this)

        qNum = 0
        curScore = 0

        findViews()
        generateQuiz()
        renderQuestion()

    }


    override fun onResume() {
        super.onResume()

    }

    private fun findViews() {
        tv_questionLabel = findViewById(R.id.tv_questionLabel)
        tv_question = findViewById(R.id.tv_question)

        choices = arrayOfNulls(3)
        choices[0] = findViewById(R.id.btn_ans1)
        choices[1] = findViewById(R.id.btn_ans2)
        choices[2] = findViewById(R.id.btn_ans3)
        btn_skip = findViewById(R.id.btn_skip)

        tv_score = findViewById(R.id.tv_score)
    }

    private fun generateQuiz() {
        questions = arrayOfNulls(NUMQUESTIONS)
        for (i in 0 until NUMQUESTIONS) {
            questions[i] = MathQuestion(r.nextInt(4))
        }
    }

    private fun renderQuestion() {
        val cur = questions!![qNum]

        val qLabel = resources.getString(R.string.questionLabel)
        tv_questionLabel!!.text = qLabel + " " + (qNum + 1)
        tv_question!!.text = cur?.leftOp.toString() + " " + cur?.operation + " " + cur?.rightOp

        val correctBtn = r.nextInt(3)
        for (i in 0..2) {
            if (i == correctBtn) {
                choices!![i]?.text = "" + cur?.correctAnswer
            } else {
                choices!![i]?.text = "" + cur?.randomAnswer()
            }
        }

        tv_score!!.text = "$curScore / $NUMQUESTIONS"
    }

    fun makeGuess(view: View) {
        if (finished) return
        if (qNum < NUMQUESTIONS) {
            val b = view as Button
            val guess = Integer.parseInt(b.text.toString())
            guess(guess)

            if (qNum < NUMQUESTIONS - 1) {
                nextQuestion()
            } else {
                tv_score!!.text = "$curScore / $NUMQUESTIONS"
                finishGame()
            }
        } else {
            finishGame()

        }
    }

    private fun guess(guess: Int) {
        if (guess == questions!![qNum]?.correctAnswer) {
            curScore++
            Toast.makeText(this, R.string.correct, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, R.string.wrong, Toast.LENGTH_SHORT).show()
        }
    }

    fun skipQuestion(v: View) {
        if (qNum < NUMQUESTIONS - 1) {
            nextQuestion()
        } else {
            if (!finished) {
                finishGame()
            }

        }
    }

    private fun nextQuestion() {
        qNum++
        renderQuestion()
    }

    fun resetQuiz(view: View) {
        qNum = 0
        curScore = 0
        generateQuiz()
        renderQuestion()
        btn_skip!!.setText(R.string.skip)
        finished = false
    }

    private fun finishGame() {
        for (btn in choices!!) {
            btn?.text = "--"
        }
        btn_skip!!.text = "--"
        finished = true

        // notification
        makeNotification(curScore)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "to show your score"

            val nm = getSystemService(NotificationManager::class.java)
            nm!!.createNotificationChannel(channel1)
        }
    }

    private fun makeNotification(curScore: Int) {
        val appname = resources.getString(R.string.app_name)
        val scorelabel = resources.getString(R.string.yourScoreIs)
        val n = NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(appname)
                .setContentText("$scorelabel $curScore / $NUMQUESTIONS")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build()

        nmc!!.notify(1, n)
    }

}
