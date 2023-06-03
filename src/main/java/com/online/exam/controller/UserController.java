package com.online.exam.controller;

import com.online.exam.dto.UserDto;
import com.online.exam.helper.ApiResponse;
import com.online.exam.helper.JwtHelper;
import com.online.exam.model.UserStatus;
import com.online.exam.repo.RoleRepo;
import com.online.exam.security.CustomUserDetailService;
import com.online.exam.security.UserPrincipal;
import com.online.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private RoleRepo roleRepo;
    @PostMapping("/create")
    ResponseEntity<Map<Integer,String>> createUser(@RequestBody UserDto userDto) throws Exception {





        Map<Integer,String> resultUser=this.userService.createUser(userDto);
        if(resultUser.containsKey(200)) {
            return ResponseEntity.status(HttpStatus.OK).body(resultUser);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultUser);
    }

/*    @GetMapping("/readByEmail")
    ResponseEntity<?> getUserByUserEmail(@RequestParam("userEmail") String userEmail){
        User user=this.userService.getUserByEmail(userEmail);
        if(user==null){
            return new ResponseEntity<>(new ApiResponse("user doesnot exist with the given email",false),HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<User>(user,HttpStatusCode.valueOf(200));


    }*/
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('delete_user')")
    ResponseEntity<ApiResponse> deleteUser(@RequestParam("userId")Long userId){
        String message=this.userService.deleteUser(userId);
        if(message.isEmpty()){
            return new ResponseEntity<>(new ApiResponse("something went wrong",false),HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(new ApiResponse(message,true),HttpStatusCode.valueOf(200));
    }
 /*   @PutMapping("/update/{userId}")
    @PreAuthorize("hasAuthority('update_user')")
    ResponseEntity<User> updateUser(@PathVariable("userId")Long userId,@RequestBody UserDto userDto){
                        User updatedUser=this.userService.updateUser(userId,userDto);
                        return new ResponseEntity<User>(updatedUser,HttpStatusCode.valueOf(200));

    }
*/
    @PostMapping("/approveUser/{userId}")
    @PreAuthorize("hasAuthority('approve_user')")
    public ResponseEntity<ApiResponse> approveUserController(@PathVariable Long userId){
        String message=this.userService.approveUser(userId);
        return new ResponseEntity<>(new ApiResponse(message,true),HttpStatusCode.valueOf(200));

    }
    @GetMapping("/approveAll")
    @PreAuthorize("hasAuthority('approve_user')")
    public ResponseEntity<ApiResponse> approveAllUserController(){
        String message=this.userService.approveAllUser();
        return new ResponseEntity<>(new ApiResponse(message,true),HttpStatusCode.valueOf(200));
    }
    @GetMapping("/rejectUser/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> rejectUserController(@PathVariable Long userId){
        String message=this.userService.rejectUser(userId);
        return new ResponseEntity<>(new ApiResponse(message,true),HttpStatusCode.valueOf(200));
    }
    @GetMapping("/rejectAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> rejectAllUserController(){
        String message=this.userService.rejectAll();
        return new ResponseEntity<>(new ApiResponse(message,true),HttpStatusCode.valueOf(200));

    }
    @GetMapping("/view-all-approved-student")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> viewAllApprovedStudent(){
        List<UserDto> allApprovedStudent=this.userService.viewAllApprovedStudent();
        return new ResponseEntity<>(allApprovedStudent,HttpStatusCode.valueOf(200));
    }
    @GetMapping("/view-all-approved-teacher")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> viewAllApprovedTeacher(){
                List<UserDto> allApprovedTeacher=this.userService.viewAllApprovedTeacher();
                return new ResponseEntity<>(allApprovedTeacher,HttpStatusCode.valueOf(200));
    }

    @PostMapping(path = "/login",consumes = {MediaType.APPLICATION_JSON_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> login(@RequestBody UserDto userDto) throws Exception {

        String userName= userDto.getUserEmail();
        String password=userDto.getUserPassword();
        UserDetails userDetails=this.customUserDetailService.loadUserByUsername(userName);
        this.authenticate(userName,password,userDetails.getAuthorities());
        UserDto user = this.userService.getUserByEmail(userDto.getUserEmail());
        if(user.getUserStatus().equals(UserStatus.approved) && user.isEnabled()==Boolean.TRUE) {
            user.setUserPassword(null);
            String jwtToken = this.jwtHelper.generateToken(new UserPrincipal(user,roleRepo));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwtToken);
            return ResponseEntity.ok().headers(headers).body("Bearer "+jwtToken);
        }else{
            throw new Exception("Invalid userststatus to generate token");
        }

    }

    private void authenticate(String userName, String password, Collection<? extends GrantedAuthority> authorities) throws Exception {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userName,password,authorities);
        try{
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch (DisabledException e){
            throw new Exception("user is disabled!!!");
        }
    }


}
