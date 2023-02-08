package com.online.exam.helper;

import com.online.exam.model.Question;

import java.util.Comparator;

public class QuestionComparator implements Comparator<Question> {
    @Override
    public int compare(Question q1, Question q2) {
        return (int)(q1.getQuestionId()-q2.getQuestionId());
    }
}
