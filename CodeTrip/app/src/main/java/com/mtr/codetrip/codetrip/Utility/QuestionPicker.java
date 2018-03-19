package com.mtr.codetrip.codetrip.Utility;

import android.database.Cursor;
import android.util.Log;
import android.view.ViewGroup;

import com.mtr.codetrip.codetrip.MainActivity;
import com.mtr.codetrip.codetrip.Object.Question;
import com.mtr.codetrip.codetrip.Object.QuestionDragAndDrop;
import com.mtr.codetrip.codetrip.Object.QuestionMultipleChoice;
import com.mtr.codetrip.codetrip.Object.QuestionRearrange;
import com.mtr.codetrip.codetrip.Object.QuestionShortAnswer;
import com.mtr.codetrip.codetrip.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mtr.codetrip.codetrip.Utility.DataBaseUtility.getStrArrayFromDB;
import static com.mtr.codetrip.codetrip.Utility.DataBaseUtility.getIntArrayFromDB;

/**
 * Created by catrina on 2018-03-17.
 */

public class QuestionPicker {

    private Map<String, QuestionTree> questionMap;
    private List<Difficulty> difficultyList;
    private List<String> topicList;
    private int courseID;
    int evaluationScore;
    int passScore;

    private List<Integer> incorrectList;
    int topicIndex;
    QuestionTree currentQuestionTree;

    public QuestionPicker(int courseID){
        generateQuestionMap(courseID);
        initTopicDictionary();
        topicIndex = 0;
        initQuestionTree();
    }


    public Question getCuurrentQuestion(){
        return  currentQuestionTree.currentQuestion;
    }

    private void initQuestionTree(){
        currentQuestionTree = questionMap.get(topicList.get(topicIndex));
    }

    enum Difficulty {
        EASY(1), MEDIUM(2), DIFFICULT(3);

        private final int value;
        private Difficulty(int value) {
            this.value = value;
        }

        public int getScore() {
            return value;
        }

        public static Difficulty fromInteger(int numuricalLevel) {
            switch(numuricalLevel) {
                case 1:
                    return EASY;
                case 2:
                    return MEDIUM;
                case 3:
                    return DIFFICULT;
            }
            return null;
        }

        public  Difficulty changeLevel( boolean upgrade){
            int numLevelChange = upgrade? 1 : -1;
            return  fromInteger(getScore() + numLevelChange);
        }

        public String toString(){
            switch (value){
                case 1:
                    return "Easy";
                case 2:
                    return "Medium";
                case 3:
                    return "Hard";
                default:
                    return null;
            }
        }
    }


    public class QuestionTree {
        private Question currentQuestion;
        private QuestionTree nextRightQuestion;
        private QuestionTree nextWrongQuestion;

        public QuestionTree(Difficulty difficulty, Map<Difficulty,List<Question>>topicDict) {
            Map<Difficulty, List<Question>> currentTopicDic = topicDict;
//            List<Question> questionList = currentTopicDic.
            if (difficulty==null || topicDict==null || currentTopicDic.get(difficulty) == null || currentTopicDic.get(difficulty).size()==0){
                currentQuestion = null;
            }else{
                currentQuestion = currentTopicDic.get(difficulty).remove(0);
                Log.d("medium list", Integer.toString(currentTopicDic.get(Difficulty.MEDIUM).size()));
                addNextRight(difficulty.changeLevel(true), currentTopicDic);
                addNextWrong(difficulty.changeLevel(false), currentTopicDic);
            }
        }

        public void addNextRight(Difficulty difficulty,Map<Difficulty,List<Question>>topicDict){
            nextRightQuestion = new QuestionTree(difficulty, topicDict);
        }

        public void addNextWrong(Difficulty difficulty,Map<Difficulty,List<Question>>topicDict){
            nextWrongQuestion = new QuestionTree(difficulty, topicDict);
        }
    }

    public void generateQuestionMap(int courseID){
        this.courseID = courseID;
        String sql = String.format("SELECT * FROM course WHERE courseid=%d",courseID);
        Cursor cursor = MainActivity.myDB.rawQuery(sql, null);
        cursor.moveToFirst();

        topicList = getStrArrayFromDB(cursor, "topics");

        difficultyList = new ArrayList<>();
        difficultyList.add(Difficulty.EASY);
        difficultyList.add(Difficulty.MEDIUM);
        difficultyList.add(Difficulty.DIFFICULT);
        evaluationScore = 0;
        passScore = 3;
//        round = 0;
    }

    public void initTopicDictionary(){
        questionMap = new HashMap<>();
        for (String topicString : topicList){
            Map<Difficulty,List<Question>>topicDict = new HashMap<>();
            QuestionTree questionTreeRoot;
            for(Difficulty difficulty : difficultyList){
                String sql = String.format("SELECT * FROM question WHERE courseid=%d AND topic=\"%s\" AND difficulty=\"%s\"",courseID,topicString,difficulty.toString());
                Cursor cursor = MainActivity.myDB.rawQuery(sql,null);
                cursor.moveToFirst();
                List<Question> difficultyQuesionList = new ArrayList<>();

                while(!cursor.isAfterLast()){
                    String questionType = cursor.getString(cursor.getColumnIndex("type"));

                    Question question;
                    switch (questionType) {
                        case "Rearrange":
                            question = new QuestionRearrange();
                            break;
                        case "MultipleChoice":
                            question = new QuestionMultipleChoice();
                            break;
                        case "ShortAnswer":
                            question = new QuestionShortAnswer();
                            break;
                        case "Drag&Drop":
                            question = new QuestionDragAndDrop();
                            break;
                        default:
                            question = null;
                            break;
                    }
                    question.populateFromDB(cursor);

                    difficultyQuesionList.add(question);
                    cursor.moveToNext();
                }
                topicDict.put(difficulty,difficultyQuesionList);
            }
            questionTreeRoot = new QuestionTree(Difficulty.MEDIUM, topicDict);
            questionMap.put(topicString,questionTreeRoot);
        }
    }


    public void changeEvaluationScore(boolean correctAnswer){
        // TODO remove from map
        if (correctAnswer){
            // TODO - add score
        }else {
            // TODO - add to incorrect list
        }
        // TODO nagerateNextQuestion
    }

    public void ganerateNextQuestion(boolean levelup){
        // TODO - add round

        // change score
//        changeEvaluationScore(levelup);

        // if pass - next topic
        if (evaluationScore >= passScore){
            ++topicIndex;
            // TODO - reset round
            // TODO - reset evaluation score
        }

        // else
    }



}
