package com.online.exam.model;

import java.util.Comparator;

public class TitleComparator implements Comparator<Question> {

    @Override
    public int compare(Question o1, Question o2) {
        return o1.getQuestionTitle().compareTo(o2.getQuestionTitle());
    }
}
