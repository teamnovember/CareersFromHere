package models;

import com.avaje.ebean.*;

import java.util.Collections;
import java.util.List;

/**
 * Created by Louise on 05/02/2015.
 */
public class QuestionDAO {

    public QuestionDAO() {}

    public Question getQuestion(Long id) {
        return Question.find.byId(id);
    }

    public void deleteQuestion(Long id) {
        Question q = Question.find.byId(id);
        q.setActive(false);
        q.save();
        int order = q.getOrder();
        List<Question> qs = getActiveQuestions(q.getSchool());
        for (Question ques : qs) {
            if (order < ques.getOrder()) {
                ques.setOrder(order);
                ques.save();
                order++;
            }
        }
    }

    public void newQuestion(Question q) {
        List<Question> qs = getActiveQuestions(q.getSchool()); //we haven't saved q yet so it shouldn't be in here yet
        Question q2 = qs.get(qs.size() - 1); //get last question in the active list
        q.setOrder(q2.getOrder() + 1);
        q.save();
    }

    public void reorderQuestion() {} //TODO: how do we want to do this? swapping?

    public List<Question> getActiveQuestions(School school) {
        ExpressionList<Question> expList = Question.find.where().eq("school.schoolId",school.getId()).eq("active",true); //TODO: check that this works
        List<Question> result = expList.findList();
        Collections.sort(result);
        return result;
    }
}
