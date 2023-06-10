package com.online.exam.service.impl;
import com.online.exam.dto.ExamDto;
import com.online.exam.dto.QuestionDto;
import com.online.exam.dto.SelectedChoiceDto;
import com.online.exam.dto.SubmitAnswerDto;
import com.online.exam.helper.*;
import com.online.exam.model.*;
import com.online.exam.model.StudentExamAnswer;
import com.online.exam.repo.*;
import com.online.exam.service.ExamService;
import com.online.exam.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.util.*;

@Transactional
@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    private ExamRepo examRepo;
    @Autowired
    private QueryHelper queryHelper;
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ExamQuestionRepo examQuestionRepo;
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private StudentExamAnswerRepo studentExamAnswerRepo;
    @Autowired
    private MergeSort mergeSort;
    @Autowired
    private ResultService resultService;
    @Autowired
    private SubmitAnswerRepo submitAnswerRepo;


    public Map<Integer,String> createExam(ExamDto examDto, Principal principal) throws Exception {
        Map<Integer,String> message=new HashMap<>();
        User retrievedUser = userRepo.findByUserEmail(principal.getName());
        HelperClass helperClass = new HelperClass(mergeSort,retrievedUser,questionRepo);
        Course retrievedCourse = new Course();
        Set<Course> courses = retrievedUser.getCourses();
        Exam exam = new Exam();
        if (courses.size() == 1) {
            for (Course eachCourse : courses
            ) {
                retrievedCourse = this.queryHelper.getCourseMethod(eachCourse.getCourseId());
                List<Exam> exams = eachCourse.getExams();
                if (!exams.isEmpty()) {
                    if (checkWhetherExamWithGivenTitleExists(exams,examDto.getExamTitle())) {
                        message.put(500,"exam with the given title already exists!!!");
                        return message;
                    }

                }
            }
        }
        ExamHelper examHelper = new ExamHelper(questionRepo);
        exam.setExamTitle(examDto.getExamTitle());
        exam.setExamDesc(examDto.getExamDesc());
        if(examDto.getQuestionPattern().equals(QuestionPattern.sort.toString())){
        exam.setQuestionPattern(QuestionPattern.sort.toString());
        }
        else if(examDto.getQuestionPattern().equals(QuestionPattern.random.toString())) {
            exam.setQuestionPattern(QuestionPattern.random.toString());
        }else {
            message.put(500,"Please select a valid question pattern");
            return message;
        }

        if (examHelper.generateValidDate(examDto.getExamStartedTime(), examDto.getExamEndedTime())) {
            exam.setExamStartedTime(examDto.getExamStartedTime());
            exam.setExamEndedTime(examDto.getExamEndedTime());
            Long totalTime = examHelper.generateTotalExamTime(examDto.getExamStartedTime(), examDto.getExamEndedTime());
            exam.setExamTimeLimit(totalTime);

        } else {
            message.put(500,"Something wrong went with starting time");
            return message;
        }

                if(examDto.getQuestionPattern().equals(QuestionPattern.random.toString())||examDto.getQuestionPattern().equals(QuestionPattern.sort.toString())) {
                    List<Question> questions = examHelper.generateQuestion(helperClass.findAllQuestionByUser(retrievedUser), examDto.getExamQuestionDisplayLimit(),examDto.getQuestionPattern(),mergeSort,retrievedUser,questionRepo);
                    if(questions.isEmpty()){
                        message.put(500,"Invalid question limit!!!");
                        return message;
                    }
                        exam.setExamQuestions(addQuestion(questions,exam));
                    exam.setExamTotalMarks(examHelper.generateTotalMarks(questions));
                }else{
                    message.put(500,"please select a proper question pattern!!");
                    return message;
                }

            exam.setCourse(retrievedCourse);
            exam.setUser(Set.of(retrievedUser));
            retrievedUser.getExams().add(exam);
            exam.setExamMode(ExamStatus.pending.toString());
            exam.setExamQuestionDisplayLimit(examDto.getExamQuestionDisplayLimit());
            exam.setExamStatus(true);
            this.examRepo.save(exam);
            message.put(200,"exam created successfully");
            return message;

    }

    @Override
    public Map<Integer,String> deleteExam(Long examId,Principal principal){
        Map<Integer,String> message=new HashMap<>();
        Exam retrievedExam=this.queryHelper.getExamMethod(examId);
        User retrievedUser=userRepo.findByUserEmail(principal.getName());
        if(retrievedExam.getExamMode().equals(ExamStatus.pending.toString())||retrievedExam.getExamMode().equals(ExamStatus.finish.toString())) {
            if (retrievedExam.getUser().contains(retrievedUser)) {
                Set<User> users=retrievedExam.getUser();
                for (User eachUser:users
                     ) {
                    eachUser.setExams(null);
                }
                retrievedExam.setUser(null);

                List<ExamQuestion> examQuestions=retrievedExam.getExamQuestions();
                for (ExamQuestion eachExamQuestion:examQuestions
                     ) {
                        eachExamQuestion.setExam(null);
                        eachExamQuestion.setQuestion(null);
                    examQuestionRepo.delete(eachExamQuestion);
                }

                examRepo.deleteById(examId);
                message.put(200,"exam deleted successfully");
            }
            else {
                message.put(500,"Invalid loggedInUser for delete operation");
                return message;
            }
        }else {
            message.put(500, "Invalid exam mode for the delete operation");
        }
        return message;
    }

    @Override
    public List<ExamDto> getExamByCourse(Long courseId){
        List<ExamDto> examDtos=new ArrayList<>();
        ExamDto examDto;
        Course retrievedCourse=queryHelper.getCourseMethod(courseId);
        List<Exam> exams=examRepo.findByCourse(retrievedCourse);

        if(!exams.isEmpty()){
            for (Exam eachExam:exams
                 ) {
                examDto=getExamDto(eachExam);
                examDtos.add(examDto);
            }
            return examDtos;
        }

        return null;
    }

    @Override
    public ExamDto getExamById(Long examId) {
        Exam retrievedExam=queryHelper.getExamMethod(examId);
        return getExamDto(retrievedExam);
    }


    @Override
    public Map<Integer,String> updateExam(ExamDto examDto, Principal principal) throws Exception {
        Map<Integer,String> messageMap=new HashMap<>();
        Exam retrievedExam=queryHelper.getExamMethod(examDto.getExamId());
        User loggedInUser=userRepo.findByUserEmail(principal.getName());
        ExamHelper examHelper=new ExamHelper();
        if(retrievedExam.getExamMode().equals(ExamStatus.pending)||retrievedExam.getExamMode().equals(ExamStatus.finish)) {
            if (retrievedExam.getUser().contains(loggedInUser)) {
                retrievedExam.setExamStatus(false);
                examRepo.save(retrievedExam);
                if (!examDto.getExamTitle().isEmpty() && !examDto.getExamTitle().equals(retrievedExam.getExamTitle().toLowerCase())) {
                    if (!checkWhetherExamWithGivenTitleExists(getAllExam(retrievedExam.getCourse()), examDto.getExamTitle())) {
                        retrievedExam.setExamTitle(examDto.getExamTitle());

                    } else {
                        messageMap.put(500, "exam with the given title already exists!!");
                        return messageMap;
                    }
                }
                if (!examDto.getExamDesc().isEmpty() && !examDto.getExamDesc().equals(retrievedExam.getExamDesc().toLowerCase())) {
                    retrievedExam.setExamDesc(examDto.getExamDesc());
                }
                if (!examDto.getExamStartedTime().toString().isEmpty() && !examDto.getExamEndedTime().toString().isEmpty()) {

                    if (examHelper.generateValidDate(examDto.getExamStartedTime(), examDto.getExamEndedTime())) {
                        retrievedExam.setExamStartedTime(examDto.getExamStartedTime());
                        retrievedExam.setExamEndedTime(examDto.getExamEndedTime());
                        retrievedExam.setExamTimeLimit(examHelper.generateTotalExamTime(examDto.getExamStartedTime(), examDto.getExamEndedTime()));
                    } else {
                        messageMap.put(500, "invalid date!!");
                        return messageMap;
                    }
                }
                if (examDto.getExamQuestionDisplayLimit() > 0) {
                    HelperClass helperClass = new HelperClass(mergeSort,loggedInUser,questionRepo);
                    List<Question> questions = helperClass.findAllQuestionByUser(loggedInUser);
                        if (examDto.getQuestionPattern().equals(QuestionPattern.sort) || examDto.getQuestionPattern().equals(QuestionPattern.random)) {
                            List<Question> questionList = examHelper.generateQuestion(questions, examDto.getExamQuestionDisplayLimit(), examDto.getQuestionPattern(),mergeSort,loggedInUser,questionRepo);
                            retrievedExam.setQuestionPattern(examDto.getQuestionPattern());
                            retrievedExam.setExamTotalMarks(examHelper.generateTotalMarks(questionList));
                            deleteOldQuestion(retrievedExam.getExamQuestions());
                            retrievedExam.setExamQuestions(addQuestion(questionList, retrievedExam));
                        } else {
                            messageMap.put(500, "Please select a valid question pattern!!!");
                        }


                } else {
                    messageMap.put(500, "Invalid questionDisplayLimit!!!!");
                    return messageMap;
                }
                retrievedExam.setExamMode(ExamStatus.pending.toString());
                retrievedExam.setExamStatus(true);
                examRepo.save(retrievedExam);
                messageMap.put(200,"exam updated successfully");
                return messageMap;
            }
            messageMap.put(500, "Invalid loggedInUser");
            return messageMap;
        }
        messageMap.put(500,"inavlid exam mode for updation");
        return messageMap;
    }

    @Override
    public ExamDto startExam(Long examId, Principal principal) {
        return getExamById(examId);
    }

    //@Transactional(rollbackFor = Exception.class)
    @Override
    public Map<Integer, String> submitExam(SubmitAnswerDto submitAnswerDto, Principal principal) throws Exception {
        Map<Integer,String> message=new HashMap<>();
        StudentExamAnswer studentExamAnswer=new StudentExamAnswer();

        if(checkCourseExamStudentProvided(submitAnswerDto, principal)){

            if(studentExamAnswer==null){
                message.put(500,"Something went wrong!!!!");
                return message;
            }
            setStudentExamAnswerAdditionalDetails(submitAnswerDto,studentExamAnswer,principal);
            Integer result=studentExamAnswerRepo.insert(studentExamAnswer.getCourse().getCourseId(),studentExamAnswer.getExam().getExamId(),studentExamAnswer.getUser().getUserId());
            studentExamAnswer=studentExamAnswerRepo.findStudentExamAnswerByUserAndExam(submitAnswerDto.getExamId(), submitAnswerDto.getStudentId());
           checkAndSetQuestionWithChoice(submitAnswerDto,studentExamAnswer);
           studentExamAnswer=studentExamAnswerRepo.findStudentExamAnswerByUserAndExam(examRepo.findById(submitAnswerDto.getExamId()).get().getExamId(),userRepo.findByUserEmail(principal.getName()).getUserId());
            String resultMessage=resultService.createResult(studentExamAnswer.getId());
            message.put(200,"Student selected choice with question id has been successfully created and "+resultMessage);
        }else {
            message.put(500,"Something wrong with the provided details of student,course and exam!!!");
            return message;

        }

        return message;
    }




    private boolean checkWhetherExamWithGivenTitleExists(List<Exam> exams,String title){
        Boolean resultStatus=false;
        for (Exam eachExam:exams
             ) {
            Exam exam=this.examRepo.findByExamTitle(title);
            if(exam!=null) {
                if (exam.getExamTitle().equals(eachExam.getExamTitle())) {
                    resultStatus = true;
                }
            }
        }
        return resultStatus;
    }


    private List<Exam> getAllExam(Course course){
        return examRepo.findByCourse(course);
    }

    private void deleteOldQuestion(List<ExamQuestion> examQuestions){
        for (ExamQuestion eachExamQuestion:examQuestions
             ) {
            eachExamQuestion.setExam(null);
            eachExamQuestion.setQuestion(null);
            examQuestionRepo.delete(eachExamQuestion);
        }
    }
    private List<ExamQuestion> addQuestion(List<Question> questions,Exam exam){
        List<ExamQuestion> examQuestions=new ArrayList<>();

        for (Question eachQuestion:questions
             ) {
            ExamQuestion examQuestion=new ExamQuestion();
            examQuestion.setQuestion(eachQuestion);
            examQuestion.setExam(exam);
            examQuestions.add(examQuestion);
        }
        return examQuestions;
    }

    private ExamDto getExamDto(Exam exam){
        ExamDto examDto=new ExamDto();
        examDto.setExamId(exam.getExamId());
        examDto.setExamTitle(exam.getExamTitle());
        examDto.setExamDesc(exam.getExamDesc());
        examDto.setExamTotalTime(exam.getExamTimeLimit());
        examDto.setTotalMarks(exam.getExamTotalMarks());
        examDto.setExamStatus(exam.getExamStatus());
        examDto.setExamStartedTime(exam.getExamStartedTime());
        List<ExamQuestion> examQuestions=exam.getExamQuestions();
        List<QuestionDto> questionDtos=new ArrayList<>();

        for (ExamQuestion eachExamQuestion:examQuestions
             ) {
            QuestionDto questionDto=new QuestionDto();
            questionDto=QuestionHelper.getQuestionDto(eachExamQuestion.getQuestion());
            questionDtos.add(questionDto);

        }
        examDto.setQuestionDtos(questionDtos);


        return examDto;
    }



    private User getStudent(Principal principal){
        return userRepo.findByUserEmail(principal.getName());
    }

    private boolean checkCourseExamStudentProvided(SubmitAnswerDto submitAnswerDto,Principal principal){
        boolean status=false;
        User loggedInStudent=getStudent(principal);
        Set<Course> retrievedCourse=loggedInStudent.getCourses();

        OuterLoop:for (Course eachCourse:retrievedCourse
             ) {
            if(eachCourse.getCourseId().equals(submitAnswerDto.getCourseId())) {
                if (checkExamProvided(eachCourse, submitAnswerDto.getExamId())) {
                    status = true;
                    break OuterLoop;
                }
            }
        }
        return status;

    }

    private boolean checkExamProvided(Course course,Long examId){
        boolean status=false;
        Exam retrievedExam=queryHelper.getExamMethod(examId);
        List<Exam> exams=course.getExams();
        for (Exam eachExam:exams
             ) {
            if(eachExam.getExamId().equals(examId)){
                status=true;
                break;
            }

        }
        return status;
    }
    @Transactional()
    protected List<SubmitAnswer> checkAndSetQuestionWithChoice(SubmitAnswerDto submitAnswerDto,StudentExamAnswer studentExamAnswer) throws Exception {

        List<Question> questions=questionRepo.findQuestionByExam(submitAnswerDto.getExamId());
        ValidateStudentAnswerChoice validateStudentAnswerChoice=new ValidateStudentAnswerChoice(questionRepo);
        List< SelectedChoiceDto> selectedChoiceDtos =submitAnswerDto.getSelectedChoiceDtos();
        if(selectedChoiceDtos.isEmpty()){
            return null;
        }
        if(checkQuestionIdWithSubmittedQuestionIdByStudent(questions,selectedChoiceDtos)&&validateStudentAnswerChoice.validateStudentAnswer(selectedChoiceDtos)){
            return getAllSubmitAnswer(selectedChoiceDtos,studentExamAnswer);
        }else {
            return null;
        }

    }
    
    private boolean checkQuestionIdWithSubmittedQuestionIdByStudent(List<Question> questions,List<SelectedChoiceDto> selectedChoiceDtos){
        boolean status=false;
        List<Long> retrievedQuestionId=new ArrayList<>();
        List<Long> submittedQuestionId=new ArrayList<>();
        for (Question eachRetrievedQuestion:questions
             ) {
            retrievedQuestionId.add(eachRetrievedQuestion.getQuestionId());
        }
        for (SelectedChoiceDto eachSelectedChoiceDto:selectedChoiceDtos
             ) {
            submittedQuestionId.add(eachSelectedChoiceDto.getQuestionId());
        }
        if(retrievedQuestionId.containsAll(submittedQuestionId)){
            status=true;
        }
        return status;
        
    }


    private List<SubmitAnswer> getAllSubmitAnswer(List<SelectedChoiceDto> selectedChoiceDtos,StudentExamAnswer studentExamAnswer){
        List<SubmitAnswer> submitAnswers=new ArrayList<>();
        for (SelectedChoiceDto eachSelectedChoiceDto:selectedChoiceDtos
             ) {
            submitAnswerRepo.insert(eachSelectedChoiceDto.getSelectedChoice(), eachSelectedChoiceDto.getQuestionId(), studentExamAnswer.getId());

        }
        return submitAnswers;

    }

    private void setStudentExamAnswerAdditionalDetails(SubmitAnswerDto  submitAnswerDto,StudentExamAnswer studentExamAnswer,Principal principal){
        Exam retrievedExam=examRepo.findById(submitAnswerDto.getExamId()).get();
        Course retrievedCourse=courseRepo.findById(submitAnswerDto.getCourseId()).get();
        User retrievedUser=userRepo.findByUserEmail(principal.getName());
        studentExamAnswer.setExam(retrievedExam);
        studentExamAnswer.setCourse(retrievedCourse);
        studentExamAnswer.setUser(retrievedUser);
    }




}

