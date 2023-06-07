package com.online.exam.helper;

import com.online.exam.model.Question;
import com.online.exam.model.TitleComparator;
import com.online.exam.model.User;
import com.online.exam.repo.QuestionRepo;
import com.online.exam.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@Component
public class MergeSort {
    public  List<Question> mergeSort(List<Question> questions,Comparator<Question> comparator){
     if(questions==null||questions.size()==0){
         return null;
     }
     ArrayList<Question> temp=new ArrayList<Question>(/*questions.size()*/);
     for (int i=0;i< questions.size();i++){
         temp.add(null);
     }
     return mergeSortHelper(questions,temp,0,questions.size()-1,comparator);

    }

    private  List<Question> mergeSortHelper(List<Question> questions, List<Question> temp, int left, int right, Comparator<Question> comparator) {
        if(left>=right){
            return null;
        }
        int mid=left +(right-left)/2;
        mergeSortHelper(questions,temp,left,mid,comparator);
        mergeSortHelper(questions,temp,mid+1,right,comparator);
        return merge(questions,temp,left,mid,right,comparator);
    }
    private  List<Question> merge(List<Question> questions,List<Question> temp,int left,int mid,int right,Comparator<Question> comparator){
        for (int i=left;i<=right;i++){
            temp.set(i,questions.get(i));
        }
        int i=left;
        int j=mid+1;
        int k=left;
        while (i<=mid&&j<=right){
            if(comparator.compare(temp.get(i),temp.get(j))<=0){
                questions.set(k++,temp.get(i++));
            }else {
                questions.set(k++,temp.get(j++));
            }
        }
        while (i<=mid){
            questions.set(k++,temp.get(i++));
        }
        while (j<=right){
            questions.set(k++,temp.get(j++));
        }
        return questions;
    }
}
