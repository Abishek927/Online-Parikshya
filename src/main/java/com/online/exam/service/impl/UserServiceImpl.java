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
    public UserDto createUser(UserDto userDto) throws Exception {
        if(checkIfUserNameExists(userDto)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User Name already Exists");
        }
        if(checkIfUserEmailExists(userDto)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User email id already exists");
        }
        Role retrievedRole=queryHelper.getRoleMethod(userDto.getRoleId());
        User user=new User();
        this.modelMapper.map(userDto,user);
        user.setUserEmail(user.getUserEmail().toLowerCase());
        user.setUserName(user.getUserName().toLowerCase());
        if (retrievedRole.getRoleName().equals("ROLE_STUDENT")) {
                            Faculty faculty = this.queryHelper.getFacultyMethod(userDto.getFacultyId());
                            Category category = queryHelper.getCategoryMethod(userDto.getCategoryId());
                            Course course = this.queryHelper.getCourseMethod(userDto.getCourseId());
                            List<Category> categories = faculty.getCategoryList();
                                if (!categories.isEmpty()) {
                                    for (Category eachCategory : categories
                                    ) {
                                        if (eachCategory.getCategoryId().equals(category.getCategoryId())) {
                                            List<Course> courses = eachCategory.getCourseList();
                                            if (!courses.isEmpty()) {
                                                for (Course eachCourse : courses
                                                ) {
                                                    if (eachCourse.getCourseId().equals(course.getCourseId())) {
                                                        user.setCategories(Set.of(category));
                                                        category.getUsers().add(user);
                                                        user.setCourses(Set.of(course));
                                                        course.getUsers().add(user);
                                                        user.setFaculties(Set.of(faculty));
                                                        faculty.getUsers().add(user);
                                                        user.setUserStatus(UserStatus.pending);
                                                    }

                                                }


                                            } else {
                                                throw new Exception("there is no courses for the given category!!!");

                                            }
                                        }

                                    }


                                } else {
                                    throw new Exception("there is no categories for the given faculty");
                                }

                        } else if (retrievedRole.getRoleName().equals("ROLE_TEACHER")) {
                            Faculty faculty = this.queryHelper.getFacultyMethod(userDto.getFacultyId());

                            Category category = queryHelper.getCategoryMethod(userDto.getCategoryId());
                            Course course = this.queryHelper.getCourseMethod(userDto.getUserId());
                            List<Category> categories=faculty.getCategoryList();
                                if(!categories.isEmpty()){
                                    for (Category eachCategory:categories
                                         ) {
                                        if(eachCategory.getCategoryId().equals(category.getCategoryId())){
                                            List<Course> courses=eachCategory.getCourseList();
                                            if(!courses.isEmpty()){
                                                for (Course eachCourse:courses
                                                     ) {
                                                    if(eachCourse.getCourseId().equals(course.getCourseId())){
                                                        user.setUserGender(null);
                                                        user.setUserRollNo(null);
                                                        user.setUserDateOfBirth(null);
                                                        user.setCourses(Set.of(eachCourse));
                                                        course.getUsers().add(user);
                                                        user.setFaculties(Set.of(faculty));
                                                        faculty.getUsers().add(user);
                                                        user.setCategories(Set.of(category));
                                                        category.getUsers().add(user);
                                                        user.setUserStatus(UserStatus.pending);
                                                    }
                                                }

                                            }else {
                                                throw new Exception("there is no courses for the given category");
                                            }


                                        }

                                    }
                                }else {
                                    throw new Exception("there is no categories for the given faculty");
                                }


                        }else if(retrievedRole.getRoleName().equals("ROLE_ADMIN")){
                                    user.setUserRollNo(null);
                                    user.setUserGender(null);
                                    user.setUserDateOfBirth(null);
                                    user.setUserContactNumber(null);
                                    user.setUserStatus(UserStatus.approved);
                                    user.setIsEnabled(Boolean.TRUE);


        }else {
                            throw new Exception("Invalid role!!!!");
                        }
                        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
                        Set<Role> roles=new HashSet<>();
                        roles.add(retrievedRole);
                        user.setUserRoles(roles);
                        user = this.userRepo.save(user);
                        UserDto userDto1=this.modelMapper.map(user,UserDto.class);
                        userDto1.setUserId(user.getUserId());
                        return userDto1;
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
      UserStatus userStatus=retrievedUser.getUserStatus();
      if(userStatus.equals(UserStatus.pending)||userStatus.equals(UserStatus.approved)){
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
       user.setUserStatus(UserStatus.approved);
       user.setIsEnabled(Boolean.TRUE);
       userRepo.save(user);
        return "User approved!!!";
    }

    @Override
    public String approveAllUser() {
        this.userRepo.findAll().stream().filter(user -> user.getUserStatus().equals(UserStatus.pending)).forEach(user ->{
            user.setUserStatus(UserStatus.approved);
            user.setIsEnabled(Boolean.TRUE);
            userRepo.save(user);

        });
        return "Approved all user" ;
    }

    @Override
    public String rejectUser(Long userId) {
        User user=this.userRepo.findById(userId).get();
        user.setUserStatus(UserStatus.rejected);
        userRepo.save(user);
        return "User Rejected!!!";
    }

    @Override
    public String rejectAll() {
        this.userRepo.findAll().stream().filter(user -> user.getUserStatus().equals(UserStatus.pending)).forEach(user -> {
            user.setUserStatus(UserStatus.rejected);
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
        for (Role eachRole:user.getUserRoles()
             ) {
            userDto.setRoleId(eachRole.getRoleId());
        }
        return userDto;
    }
}





