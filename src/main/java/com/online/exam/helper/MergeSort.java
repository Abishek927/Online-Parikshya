package com.online.exam.helper;

import com.online.exam.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MergeSort {
    public static void mergeSort(List<Question> questions){
       List<Question> leftQuestion=new ArrayList<>();
       List<Question> rightQuestion=new ArrayList<>();
       int mid=questions.size()/2;
       for(int i=0;i<mid;i++){
           leftQuestion.add(questions.get(i));
       }
       for(int j=mid;j< questions.size();j++){
           rightQuestion.add(questions.get(j));
       }



    }
}
