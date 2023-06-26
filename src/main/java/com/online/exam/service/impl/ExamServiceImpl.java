package com.online.exam.service.impl;
import com.online.exam.constant.AppConstant;
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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Transactional
@Service
public class ExamServiceImpl implements ExamService {
    public void setCourseRepo(CourseRepo courseRepo) {
        this.courseRepo = courseRepo;
    }

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
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private ResultRepo resultRepo;


    public Map<String,Object> createExam(ExamDto examDto, Principal principal) throws Exception {
        Map<String,Object> message=new HashMap<>();
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
                        message.put("status",500);
                        message.put("data","exam with the given title already exists!!!");
                        return message;
                    }

                }
            }
        }
        ExamHelper examHelper = new ExamHelper(questionRepo);
        exam.setExamTitle(examDto.getExamTitle());
        exam.setExamDesc(examDto.getExamDesc());

        exam.setQuestionPattern(QuestionPattern.random.toString());

        /*if(examDto.getQuestionPattern().equals(QuestionPattern.sort.toString())){
        exam.setQuestionPattern(QuestionPattern.sort.toString());
        }
        else if(examDto.getQuestionPattern().equals(QuestionPattern.random.toString())) {
            exam.setQuestionPattern(QuestionPattern.random.toString());
        }else {
            message.put("status",500);
            message.put("data","Please select a valid question pattern");
            return message;
        }*/
        ExecutorService executorService= Executors.newFixedThreadPool(10);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                if (examHelper.generateValidDate(examDto.getExamStartedTime(), examDto.getExamEndedTime())) {
                    exam.setExamStartedTime(examDto.getExamStartedTime());
                    exam.setExamEndedTime(examDto.getExamEndedTime());
                    Long totalTime = examHelper.generateTotalExamTime(examDto.getExamStartedTime(), examDto.getExamEndedTime());
                    exam.setExamTimeLimit(totalTime);

                }
                else {
                    throw new RuntimeException("something went wrong");
                }
            }
        });
        executorService.shutdown();
        List<Question> questions=helperClass.generateQuestionAccordingToDifficulty(examDto.getExamDifficultyType());
                    if(questions==null){
                        message.put("status",500);
                        message.put("data","Please select a valid difficulty type");
                        return message;
                    }
                    List<Question> retrievedQuestions = examHelper.generateQuestion(questions, examDto.getExamQuestionDisplayLimit(),QuestionPattern.random.toString(),mergeSort,retrievedUser,questionRepo);
                    if(questions.isEmpty()){
                        message.put("status",500);
                        message.put("data","Invalid question limit!!!");
                        return message;
                    }
                        exam.setExamQuestions(addQuestion(retrievedQuestions,exam));
                    exam.setExamTotalMarks(examHelper.generateTotalMarks(retrievedQuestions));


            exam.setCourse(retrievedCourse);
            exam.setUser(Set.of(retrievedUser));
            retrievedUser.getExams().add(exam);
            exam.setExamDifficultyType(examDto.getExamDifficultyType());
            exam.setExamMode(ExamStatus.pending.toString());
            exam.setExamQuestionDisplayLimit(examDto.getExamQuestionDisplayLimit());
            exam.setExamStatus(true);
            this.examRepo.save(exam);
            message.put("status",200);
            message.put("data","exam created successfully");
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
    public List<ExamDto> getExamByCourse(Long courseId,Principal principal){
        User loggedInUser=userRepo.findByUserEmail(principal.getName());
        List<ExamDto> examDtos=new ArrayList<>();
        ExamDto examDto;
        Course retrievedCourse=queryHelper.getCourseMethod(courseId);
        List<Exam> exams=assignExamAccordingToStudentAveragePercentage(loggedInUser);

       if(!exams.isEmpty()){
            for (Exam eachExam:exams
                 ) {
                if(eachExam.getExamEndedTime().after(new Date())){
                    continue;
                }
                StudentExamAnswer studentExamAnswer=studentExamAnswerRepo.findStudentExamAnswerByStudent(eachExam.getExamId(), loggedInUser.getUserId());
                if(studentExamAnswer!=null){
                    continue;
                }
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

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Map<String,Object> submitExam(SubmitAnswerDto submitAnswerDto, Principal principal) throws Exception {
        Map<String,Object> message=new HashMap<>();
        User loggeInUser=queryHelper.getUserMethod(submitAnswerDto.getStudentId());
        Exam retrievedExam=queryHelper.getExamMethod(submitAnswerDto.getExamId());
        StudentExamAnswer studentExamAnswer=new StudentExamAnswer();
        StudentExamAnswer studentExamAnswer1=studentExamAnswerRepo.findStudentExamAnswerByUserAndExam(loggeInUser.getUserId(),retrievedExam.getExamId());
        if(studentExamAnswer1!=null){
            retrievedExam.setExamStatus(Boolean.FALSE);
            examRepo.save(retrievedExam);
            message.put("Status",500);
            message.put("data","User already submit the exam for the given exam");
            return message;
        }
        if(checkCourseExamStudentProvided(submitAnswerDto, principal)){

            setStudentExamAnswerAdditionalDetails(submitAnswerDto,studentExamAnswer,principal);
            Integer result=studentExamAnswerRepo.insert(studentExamAnswer.getCourse().getCourseId(),studentExamAnswer.getExam().getExamId(),studentExamAnswer.getUser().getUserId());
            studentExamAnswer=studentExamAnswerRepo.findStudentExamAnswerByUserAndExam(submitAnswerDto.getExamId(), submitAnswerDto.getStudentId());
           if(!checkAndSetQuestionWithChoice(submitAnswerDto,studentExamAnswer)){
               throw new RuntimeException("something went wrong!!!");
           }
           studentExamAnswer=studentExamAnswerRepo.findStudentExamAnswerByUserAndExam(examRepo.findById(submitAnswerDto.getExamId()).get().getExamId(),userRepo.findByUserEmail(principal.getName()).getUserId());
            String resultMessage=resultService.createResult(studentExamAnswer.getId());
            Result result1=resultRepo.findByUserAndExam(loggeInUser,retrievedExam);
            message.put("ResultId",result1.getResultId());
            message.put("Status",200);
            message.put("data","Student selected choice with question id has been successfully created and "+resultMessage);
        }else {
            message.put("Status",500);
            message.put("data","Something wrong with the provided details of student,course and exam!!!");
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
    protected Boolean checkAndSetQuestionWithChoice(SubmitAnswerDto submitAnswerDto,StudentExamAnswer studentExamAnswer) throws Exception {
        Boolean status = false;
        List<Question> questions = questionRepo.findQuestionByExam(submitAnswerDto.getExamId());
        ValidateStudentAnswerChoice validateStudentAnswerChoice = new ValidateStudentAnswerChoice(questionRepo);
        List<SelectedChoiceDto> selectedChoiceDtos = submitAnswerDto.getSelectedChoiceDtos();
        if (selectedChoiceDtos.isEmpty()) {
            return status;
        }
        if (checkQuestionIdWithSubmittedQuestionIdByStudent(questions, selectedChoiceDtos) && validateStudentAnswerChoice.validateStudentAnswer(selectedChoiceDtos)) {
            if (getAllSubmitAnswer(selectedChoiceDtos, studentExamAnswer)) {
                status = true;
            } else {
                status = false;
            }
        }

        return status;

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


    private Boolean getAllSubmitAnswer(List<SelectedChoiceDto> selectedChoiceDtos,StudentExamAnswer studentExamAnswer){
        Boolean status=false;
        List<SubmitAnswer> submitAnswers=new ArrayList<>();
        for (SelectedChoiceDto eachSelectedChoiceDto:selectedChoiceDtos
             ) {
            submitAnswerRepo.insert(eachSelectedChoiceDto.getSelectedChoice(), eachSelectedChoiceDto.getQuestionId(), studentExamAnswer.getId());
            status=true;
        }
        return status;

    }

    private void setStudentExamAnswerAdditionalDetails(SubmitAnswerDto  submitAnswerDto,StudentExamAnswer studentExamAnswer,Principal principal){
        Exam retrievedExam=examRepo.findById(submitAnswerDto.getExamId()).get();
        Course retrievedCourse=courseRepo.findById(submitAnswerDto.getCourseId()).get();
        User retrievedUser=userRepo.findByUserEmail(principal.getName());
        studentExamAnswer.setExam(retrievedExam);
        studentExamAnswer.setCourse(retrievedCourse);
        studentExamAnswer.setUser(retrievedUser);
    }


    private List<Exam> assignExamAccordingToStudentAveragePercentage(User loggedInUser){
        List<StudentExamAnswer> studentExamAnswer=studentExamAnswerRepo.findByUser(loggedInUser);
        if(studentExamAnswer.isEmpty()){
            return examRepo.findByExamDifficultyType(QuestionDifficulty.EASY.toString());
        }
        float averageStudentPercentage=resultRepo.calculateAverageStudentPercentage(loggedInUser.getUserId());
        if(averageStudentPercentage> AppConstant.UPPER_PERCENTAGE){
            return examRepo.findByExamDifficultyType(QuestionDifficulty.HARD.toString());
        }
        else if (averageStudentPercentage<=AppConstant.UPPER_PERCENTAGE&&averageStudentPercentage>=AppConstant.LOWER_PERCENTAGE){
            return examRepo.findByExamDifficultyType(QuestionDifficulty.MEDIUM.toString());
        }
        return examRepo.findByExamDifficultyType(QuestionDifficulty.EASY.toString());
    }




}

