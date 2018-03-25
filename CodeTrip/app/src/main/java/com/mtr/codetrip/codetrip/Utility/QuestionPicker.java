package com.mtr.codetrip.codetrip.Utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mtr.codetrip.codetrip.MainActivity;
import com.mtr.codetrip.codetrip.Object.Question;
import com.mtr.codetrip.codetrip.Object.QuestionDragAndDrop;
import com.mtr.codetrip.codetrip.Object.QuestionMultipleChoice;
import com.mtr.codetrip.codetrip.Object.QuestionRearrange;
import com.mtr.codetrip.codetrip.Object.QuestionShortAnswer;
import com.mtr.codetrip.codetrip.QuestionActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mtr.codetrip.codetrip.Utility.DataBaseUtility.getStrArrayFromDB;

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
    public int currentProgress;
    public int lastProgress;
    private int MAX_DEPTH;
    private Context mContext;


    private List<Integer> incorrectList;
    int topicIndex;
    QuestionTree currentQuestionTree;

    public QuestionPicker(Context context,int courseID, int max_depth){
        mContext = context;
        MAX_DEPTH = max_depth;
        currentProgress = 0;
        generateQuestionMap(courseID);
        initTopicDictionary();
        topicIndex = 0;
        initQuestionTree();
    }


    public Question getCurrentQuestion(){
        if(currentQuestionTree==null) return null;
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
            if(value==1&& !upgrade) return Difficulty.EASY;
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

    private Map<Difficulty,List<Question>> deepCopyDictionary(Map<Difficulty,List<Question>> dict){
        Map<Difficulty,List<Question>> newMap = new HashMap<>();
        for (Difficulty key : dict.keySet()){
            List<Question> tmpList = new ArrayList<>(dict.get(key));
            newMap.put(key,tmpList);
        }
        return newMap;
    }


    public class QuestionTree {
        private Question currentQuestion;
        private QuestionTree nextRightQuestion;
        private QuestionTree nextWrongQuestion;

        public QuestionTree(Difficulty difficulty, Map<Difficulty,List<Question>>topicDict, int round) {
            Map<Difficulty, List<Question>> currentTopicDic = deepCopyDictionary(topicDict);
            if (round==0||difficulty==null || topicDict==null || currentTopicDic.get(difficulty) == null || currentTopicDic.get(difficulty).size()==0){
                currentQuestion = null;
            }else{
                currentQuestion = currentTopicDic.get(difficulty).remove(0);

                addNextRight(difficulty.changeLevel(true), currentTopicDic,round-1);

                addNextWrong(difficulty.changeLevel(false), currentTopicDic,round-1);
            }
        }

        public void addNextRight(Difficulty difficulty,Map<Difficulty,List<Question>>topicDict, int round){
            nextRightQuestion = new QuestionTree(difficulty, topicDict,round);
        }

        public void addNextWrong(Difficulty difficulty,Map<Difficulty,List<Question>>topicDict, int round){
            nextWrongQuestion = new QuestionTree(difficulty, topicDict,round);
        }
    }

    public void generateQuestionMap(int courseID){
        this.courseID = courseID;

        String course = "codetrip.db";
        SQLiteDatabase appDB = mContext.openOrCreateDatabase(course, Context.MODE_PRIVATE,null);

        String sql = String.format("SELECT * FROM course WHERE courseid=%d",courseID);
        Cursor cursor = appDB.rawQuery(sql, null);
        cursor.moveToFirst();

        topicList = getStrArrayFromDB(cursor, "topics");
        cursor.close();
        appDB.close();

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
        String course = "codetrip.db";
        SQLiteDatabase appDB = mContext.openOrCreateDatabase(course, Context.MODE_PRIVATE,null);

        for (String topicString : topicList){
            Map<Difficulty,List<Question>>topicDict = new HashMap<>();
            QuestionTree questionTreeRoot;
            for(Difficulty difficulty : difficultyList){

                String sql = String.format("SELECT * FROM question WHERE courseid=%d AND topic=\"%s\" AND difficulty=\"%s\"",courseID,topicString,difficulty.toString());
                Cursor cursor = appDB.rawQuery(sql,null);

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
                cursor.close();
                topicDict.put(difficulty,difficultyQuesionList);
            }
            questionTreeRoot = new QuestionTree(Difficulty.MEDIUM, topicDict,MAX_DEPTH);
            questionMap.put(topicString,questionTreeRoot);
        }
        appDB.close();
    }


//    public void changeEvaluationScore(boolean correctAnswer){
//        // TODO remove from map
//        if (correctAnswer){
//            // TODO - add score
//        }else {
//        }
//        // TODO nagerateNextQuestion
//    }

    public void ganerateNextQuestion(boolean levelup){

        // change score
//        changeEvaluationScore(levelup);

        // if pass - next topic
//        if (evaluationScore >= passScore){
//            ++topicIndex;
//            // TODO - reset evaluation score
//        }else{
//
//            currentQuestionTree = currentQuestionTree.nextRightQuestion;
//        }

        QuestionTree newQuestionNode;

        if (levelup){
            newQuestionNode = currentQuestionTree.nextRightQuestion;

        }else{
            //  TODO - add to incorrect list
            newQuestionNode = currentQuestionTree.nextWrongQuestion;

        }

        lastProgress = currentProgress;
        if (newQuestionNode.currentQuestion==null){
            if (topicIndex<topicList.size()-1){
                topicIndex++;
                initQuestionTree();
                currentProgress = topicIndex * 4;
            }else{
                // no more question
                currentQuestionTree = null;
            }
        }else{
            currentProgress++;
            currentQuestionTree = newQuestionNode;
        }

        // else
    }



}
