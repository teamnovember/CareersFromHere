package models;

import com.avaje.ebean.*;

import java.util.List;

/**
 * Created by Louise on 05/02/2015.
 */
public class QuestionDAO {

    public QuestionDAO() {

    }

    public Question getQuestion(Long id) {
        return Question.find.byId(id);
    }

    public void deleteQuestion(Long id) {
        Question.find.byId(id).delete();
    }

    public List<Question> getQuestions(School school) {
        ExpressionList<Question> expList = Question.find.where().eq("SCHOOL",school);
        List<Question> result = expList.findList();
        return result;
    }
}
