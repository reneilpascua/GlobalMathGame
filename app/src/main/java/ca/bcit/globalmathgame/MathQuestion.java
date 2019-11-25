package ca.bcit.globalmathgame;

import java.util.Random;

public class MathQuestion {


    private int leftOp;
    private int rightOp;
    private int correctAnswer;
    private char operation;
    private static Random r = new Random();

    static final int MAX = 100;

    MathQuestion(int type) {
        leftOp = randomOperand();
        rightOp = randomOperand();

        switch(type) {
            case 0: addition(); break;
            case 1: subtraction(); break;
            case 2: multiplication(); break;
            case 3: division(); break;
            default: addition();
        }

        System.out.println("question created!");
        System.out.println(leftOp+" "+operation+" "+rightOp+" = "+correctAnswer);
        System.out.println("answer: "+correctAnswer+"\n");
    }

    public int randomOperand() {
        return r.nextInt(MAX+1);
    }

    public int randomAnswer() {
        return (operation=='x')?
                r.nextInt(MAX*MAX):
                ((operation=='+')?r.nextInt(2*MAX):randomOperand());
    }

    private void addition() {
        operation = '+';
        correctAnswer = leftOp+rightOp;
    }

    private void subtraction() {
        operation = '-';
        if (leftOp - rightOp < 0) {
            int temp = leftOp;
            leftOp = rightOp;
            rightOp = leftOp;
        }
        correctAnswer = leftOp - rightOp;
    }

    private void multiplication() {
        operation = 'x';
        correctAnswer = leftOp*rightOp;
    }

    private void division() {
        operation= '/';
        rightOp=getNonZeroDivisor();
        while (leftOp%rightOp != 0) {
            leftOp = randomOperand();
            rightOp = getNonZeroDivisor();
        }
        correctAnswer = leftOp/rightOp;
    }

    private int getNonZeroDivisor() {
        int divisor = 0;
        while (divisor==0) {
            divisor=r.nextInt(MAX);
        }
        return divisor;
    }

    public int getLeftOp() {
        return leftOp;
    }

    public void setLeftOp(int leftOp) {
        this.leftOp = leftOp;
    }

    public int getRightOp() {
        return rightOp;
    }

    public void setRightOp(int rightOp) {
        this.rightOp = rightOp;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public char getOperation() {
        return operation;
    }

    public void setOperation(char operation) {
        this.operation = operation;
    }
}
