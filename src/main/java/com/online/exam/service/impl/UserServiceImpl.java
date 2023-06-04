package com.online.exam.service.impl;

import com.online.exam.dto.*;
import com.online.exam.exception.ResourceNotFoundException;
import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.model.Category;
import com.online.exam.repo.FacultyRepo;
import com.online.exam.repo.RoleRepo;
import com.online.exam.repo.CategoryRepo;
import com.online.exam.repo.UserRepo;

import com.online.exam.service.UserService;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Data
public class UserServiceImpl implements UserService {

    public static final String[] ADMIN_ACCESS={"ROLE_ADMIN","ROLE_TEACHER","ROLE_STUDENT"};

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;


    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private QueryHelper queryHelper;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FacultyRepo facultyRepo;

@Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public Map<Integer,String> createUser(UserDto userDto) throws Exception {
        Map<Integer,String> message=new HashMap<>();
        if(checkIfUserNameExists(userDto)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User Name already Exists");
        }
        if(checkIfUserEmailExists(userDto)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User email id already exists");
        }
        Role retrievedRole=roleRepo.findByRoleName(userDto.getRoleName().toUpperCase());
        User user=new User();
        if (retrievedRole.getRoleName().equals("ROLE_STUDENT")) {
                         user=setUserBasicAttribute(userDto);
            user.setUserRoles(Set.of(retrievedRole));
            retrievedRole.getUserSet().add(user);
                         userRepo.save(user);
                         message.put(200,"Student created successfully");


        } else if (retrievedRole.getRoleName().equals("ROLE_TEACHER")) {
            user=setUserBasicAttribute(userDto);
            user.setUserRoles(Set.of(retrievedRole));
            retrievedRole.getUserSet().add(user);
            userRepo.save(user);
            message.put(200,"teacher created successfully!!!");
        }else if(retrievedRole.getRoleName().equals("ROLE_ADMIN")){
            user=setUserBasicAttribute(userDto);
            user.setUserRoles(Set.of(retrievedRole));
            retrievedRole.getUserSet().add(user);
            userRepo.save(user);
            message.put(200,"admin created successfully");


        }else {
                            throw new Exception("Invalid role!!!!");
                        }
        return message;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User retrievedUser=this.userRepo.findByUserEmail(email);
        if(retrievedUser==null){
            throw new ResourceNotFoundException("user","email "+email,null);
        }
        UserDto  userDto=this.getUserDto(retrievedUser);
        //.setFacultySet(Set.of(retrievedUser.getFaculties()));
        return userDto;
    }

    @Override
    public String deleteUser(Long userId) {
        String message="";
        User retrievedUser=queryHelper.getUserMethod(userId);
      String userStatus=retrievedUser.getUserStatus();
      if(userStatus.equals(UserStatus.pending.toString())||userStatus.equals(UserStatus.approved.toString())){
          retrievedUser.setIsEnabled(Boolean.FALSE);
          Set<Faculty> faculties=retrievedUser.getFaculties();
          if(!faculties.isEmpty()){
              for (Faculty faculty:faculties
                   ) {
                  faculty.getUsers().remove(retrievedUser);
              }
          }


          Set<Category> categories=retrievedUser.getCategories();
          if(!categories.isEmpty()){
              for (Category category:categories
                   ) {
                  category.getUsers().remove(retrievedUser);

              }
          }
          Set<Course> courses=retrievedUser.getCourses();
          if(!courses.isEmpty()){
              for (Course course:courses
                   ) {
                  course.getUsers().remove(retrievedUser);
              }
          }
          this.userRepo.deleteById(retrievedUser.getUserId());
          message="User deleted successfully";
          return message;
      }
      return message;

    }



    // @Override
    /*public UserDto updateUser(Long userId, UserDto updatedUser) throws Exception {
        User retrievedUser=this.queryHelper.getUserMethod(userId);

        List<Faculty>  faculty=this.facultyRepo.findAll();
        FacultyDto facultyDto =updatedUser.getFacultyDto();

        if(retrievedUser.getUserStatus().equals(UserStatus.approved)) {
            if (facultyDto != null) {
                Set<Faculty> faculties = new HashSet<>();
                Faculty changedFaculty = this.modelMapper.map(facultyDto, Faculty.class);
                faculties.add(changedFaculty);
                for (Faculty eachFaculty : faculty
                ) {
                    if (eachFaculty.getFacultyName().equals(facultyDto.getFacultyName())) {
                        retrievedUser.setFaculties(faculties);
                    }
                }


            } else {
                retrievedUser.setFaculties(retrievedUser.getFaculties());
            }
        }else {
            throw new Exception("Invalid user status for the update operation!!!!");
        }



        =updatedUser.getFacultyDtos();


    }*/

    @Override
    public List<User> getAllUser() {
        List<User> users=this.userRepo.findAll();
        return users;
    }

    @Override
    public String approveUser(Long userId) {
       User user= this.userRepo.findById(userId).get();
       user.setUserStatus(UserStatus.approved.toString());
       user.setIsEnabled(Boolean.TRUE);
       userRepo.save(user);
        return "User approved!!!";
    }

    @Override
    public String approveAllUser() {
        this.userRepo.findAll().stream().filter(user -> user.getUserStatus().equals(UserStatus.pending)).forEach(user ->{
            user.setUserStatus(UserStatus.approved.toString());
            user.setIsEnabled(Boolean.TRUE);
            userRepo.save(user);

        });
        return "Approved all user" ;
    }

    @Override
    public String rejectUser(Long userId) {
        User user=this.userRepo.findById(userId).get();
        user.setUserStatus(UserStatus.rejected.toString());
        userRepo.save(user);
        return "User Rejected!!!";
    }

    @Override
    public String rejectAll() {
        this.userRepo.findAll().stream().filter(user -> user.getUserStatus().equals(UserStatus.pending)).forEach(user -> {
            user.setUserStatus(UserStatus.rejected.toString());
            userRepo.save(user);
        });
        return "Reject All User";
    }

    @Override
    public List<UserDto> viewAllApprovedStudent() {

        List<User> users= userRepo.findAll().stream().filter(user -> {
            user.getUserRoles().stream().filter(role -> role.getRoleName().equals("ROLE_STUDENT"));
            user.getUserStatus().equals(UserStatus.approved);

            List<User> users1=new ArrayList<>();

            return users1.add(user);

        }).collect(Collectors.toList());

        List<UserDto> userDtos=users.stream().map(user -> this.modelMapper.map(user,UserDto.class)).collect(Collectors.toList());

        return userDtos;
    }

    @Override
    public List<UserDto> viewAllApprovedTeacher() {


        List<User> users= userRepo.findAll().stream().filter(user -> {
            user.getUserRoles().stream().filter(role -> role.getRoleName().equals("ROLE_TEACHER"));
            user.getUserStatus().equals(UserStatus.approved);
            List<User> users1=new ArrayList<>();

            return users1.add(user);

        }).collect(Collectors.toList());

        List<UserDto> userDtos=users.stream().map(user -> this.modelMapper.map(user,UserDto.class)).collect(Collectors.toList());

        return userDtos;
    }

    @Override
    public List<UserDto> viewPendingTeacher() {
        List<UserDto> userDtos=new ArrayList<>();
        List<User> users=userRepo.findPendingTeacher("pending");
        if(users.isEmpty()){
            return null;
        }
        for (User eachPendingUser:users
             ) {
            Set<Role> userRoles=eachPendingUser.getUserRoles();
            for (Role eachRole:userRoles
                 ) {
                if(eachRole.getRoleName().equals("ROLE_TEACHER")){
                    UserDto userDto=new UserDto();
                    userDto.setUserName(eachPendingUser.getUserName());
                    userDto.setUserEmail(eachPendingUser.getUserEmail());
                    userDto.setUserPassword(eachPendingUser.getUserPassword());
                    userDto.setUserStatus(eachPendingUser.getUserStatus());
                    userDto.setRoleName(eachRole.getRoleName());
                    userDto.setUserContactNumber(eachPendingUser.getUserContactNumber());
                    userDtos.add(userDto);
                }
            }

        }

        return userDtos;
    }

    @Override
    public List<UserDto> viewPendingStudent() {
        List<UserDto> userDtos=new ArrayList<>();
        List<User> users=userRepo.findPendingStudent("pending");
        if(users.isEmpty()){
            return null;
        }
        for (User eachPendingStudent:users
             ) {
            Set<Role> userRoles=eachPendingStudent.getUserRoles();
            for (Role eachRole:userRoles
                 ) {
                if(eachRole.getRoleName().equals("ROLE_STUDENT")){
                        UserDto userDto=new UserDto();
                        userDto.setUserEmail(eachPendingStudent.getUserEmail());
                        userDto.setUserName(eachPendingStudent.getUserName());
                        userDto.setUserStatus(eachPendingStudent.getUserStatus().toString());
                        userDto.setRoleName(eachRole.getRoleName());
                        userDto.setUserGender(eachPendingStudent.getUserGender());
                        userDto.setUserContactNumber(eachPendingStudent.getUserContactNumber());
                        userDtos.add(userDto);
                }

            }

        }
        return userDtos;
    }

    public Boolean checkIfUserNameExists(UserDto userDto){
        return StringUtils.hasText(userDto.getUserName()) && userRepo.findByUserName(userDto.getUserName().toLowerCase())!=null;
    }
    private boolean checkIfUserEmailExists(UserDto userDto){
        return StringUtils.hasText(userDto.getUserEmail())&&userRepo.findByUserEmail(userDto.getUserEmail().toLowerCase())!=null;
    }



 /*   public String giveAccessToUser(UserDto userDto,String userRole,Principal principal){
            User user=userRepo.findById(userId).get();
    }

    private Set<String> getRolesByLoggedInUser(Principal principal){
        Set<Role> roles=getLoggedInUser(principal).getUserRoles();
        if(roles.contains("ROLE_ADMIN")){
            return Arrays.stream(ADMIN_ACCESS).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }


    private User getLoggedInUser(Principal principal){
        return userRepo.findByUserName(principal.getName());
    }*/


   public UserDto getUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserEmail(user.getUserEmail());
        userDto.setEnabled(user.getIsEnabled());
        userDto.setUserName(user.getUserName());
        userDto.setUserId(user.getUserId());
        userDto.setUserStatus(user.getUserStatus());
        userDto.setUserGender(user.getUserGender());
        userDto.setUserRollNo(user.getUserRollNo());
        userDto.setUserPassword(user.getUserPassword());
        //userDto.setUserStatus(user.getUserStatus());
        for (Role eachRole:user.getUserRoles()
             ) {
            userDto.setRoleName(eachRole.getRoleName());
        }
        return userDto;
    }


    private User setUserBasicAttribute(UserDto userDto) throws Exception {
        User user=new User();
        switch (userDto.getRoleName()){
            case "ROLE_TEACHER":
                user=setUserAdditionalAttribute(userDto);
                user.setUserName(userDto.getUserName().toLowerCase());
                user.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
                user.setUserEmail(userDto.getUserEmail().toLowerCase());
                user.setUserContactNumber(userDto.getUserContactNumber());
                user.setUserStatus(UserStatus.pending.toString());
                break;
            case "ROLE_STUDENT":
                user=setUserAdditionalAttribute(userDto);
                user.setUserContactNumber(userDto.getUserContactNumber());
                user.setUserName(userDto.getUserName().toLowerCase());
                user.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
                user.setUserEmail(userDto.getUserEmail().toLowerCase());
                user.setUserGender(userDto.getUserGender());
                user.setUserDateOfBirth(userDto.getUserDateOfBirth());
                user.setUserStatus(UserStatus.pending.toString());
                break;
            case "ROLE_ADMIN":
                user.setUserEmail(userDto.getUserEmail().toLowerCase());
                user.setUserName(userDto.getUserName().toLowerCase());
                user.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
                user.setUserStatus(UserStatus.approved.toString());
                user.setIsEnabled(true);
                break;
        }
        return user;
    }
    private User setUserAdditionalAttribute(UserDto userDto) throws Exception {
        User user=new User();
        switch (userDto.getRoleName()){
            case "ROLE_TEACHER":
                Boolean resultStatus=checkProvidedAttribute(userDto);

                Faculty retrievedFaculty=queryHelper.getFacultyMethod(userDto.getFacultyId());
                Category retrievedCategory=queryHelper.getCategoryMethod(userDto.getCategoryId());
                Course retrievedCourse=queryHelper.getCourseMethod(userDto.getCourseId());
                if(resultStatus){
                user.setCourses(Set.of(retrievedCourse));
                retrievedCourse.getUsers().add(user);
                user.setFaculties(Set.of(retrievedFaculty));
                retrievedFaculty.getUsers().add(user);
                user.setCategories(Set.of(retrievedCategory));
                retrievedCategory.getUsers().add(user);
                }
                break;
                case "ROLE_STUDENT":
                    Faculty retrievedFaculty1=queryHelper.getFacultyMethod(userDto.getFacultyId());
                    Category retrievedCategory1=queryHelper.getCategoryMethod(userDto.getCategoryId());
                    Boolean status=checkProvidedAttribute(userDto);
                    if(status){
                        user.setCategories(Set.of(retrievedCategory1));
                        retrievedCategory1.getUsers().add(user);
                        user.setFaculties(Set.of(retrievedFaculty1));
                        retrievedFaculty1.getUsers().add(user);
                    }
                    break;
        }
        return user;
    }
    private boolean checkProvidedAttribute(UserDto userDto) throws Exception {//check whether the provided attribute is coorect or not
        boolean resultStatus=false;
        switch (userDto.getUserName()){
            case "ROLE_TEACHER":
                Faculty retrievedFaculty=queryHelper.getFacultyMethod(userDto.getFacultyId());
                Category retrievedCategory=queryHelper.getCategoryMethod(userDto.getCategoryId());
                Course retrievedCourse=queryHelper.getCourseMethod(userDto.getCourseId());
                List<Category> categories=retrievedFaculty.getCategoryList();
                if(!categories.isEmpty()){
                    for (Category eachCategory:categories
                         ) {
                        if(eachCategory.getCategoryId().equals(retrievedCategory.getCategoryId())){
                            List<Course> courses=retrievedCategory.getCourseList();
                            if(courses.isEmpty()){
                                throw new Exception("there is no courses for the given category");
                            }
                            for (Course eachCourse:courses
                                 ) {
                                if(eachCourse.getCourseId().equals(retrievedCourse.getCourseId())){
                                    resultStatus=true;

                                }
                            }
                        }
                    }
                }
                else {
                    throw new Exception("there is no categories for the given faculty");
                }
                break;
            case "ROLE_STUDENT":
                Faculty retrievedFaculty1=queryHelper.getFacultyMethod(userDto.getFacultyId());
                Category retrievedCategory1=queryHelper.getCategoryMethod(userDto.getCategoryId());
                List<Category> categories1=retrievedFaculty1.getCategoryList();
                if(categories1.isEmpty()){
                    throw new Exception("there is no categories for the given faculty");

                }
                for (Category eachCategory:categories1
                     ) {
                    if(eachCategory.getCategoryId().equals(retrievedCategory1)){
                        resultStatus=true;

                    }
                }
                break;

        }
        return resultStatus;
    }


}





