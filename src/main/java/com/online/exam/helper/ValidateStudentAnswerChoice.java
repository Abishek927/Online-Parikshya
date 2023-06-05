package com.online.exam.helper;

import com.online.exam.dto.SelectedChoiceDto;
import com.online.exam.model.Question;
import com.online.exam.repo.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ValidateStudentAnswerChoice {
    @Autowired
    private QuestionRepo questionRepo;

    public List<String> getAllChoices(Long questionId){
        List<String> choices=new ArrayList<>();
        Question retrievedQuestion=questionRepo.findById(questionId).get();
      String choice=retrievedQuestion.getChoice1();
      choices.add(choice);
      choice=retrievedQuestion.getChoice2();
      choices.add(choice);
      choice=retrievedQuestion.getChoice3();
      choices.add(choice);
      choice=retrievedQuestion.getChoice4();
      choices.add(choice);
      return choices;
    }

    public boolean validateStudentAnswer(List<SelectedChoiceDto> selectedChoiceDtos){//used to validate the student submitted answer choice belongs to the given question choices or not
        boolean status=false;
        int i=0;
        for (SelectedChoiceDto selectedChoiceDto:selectedChoiceDtos
             ) {
            List<String> choices=getAllChoices(selectedChoiceDto.getQuestionId());
            if(choices.contains(selectedChoiceDto.getSelectedChoice())){
                i=i+1;
            }
        }
        if(i== selectedChoiceDtos.size()){
            status=true;
        }
        return true;
    }



}
