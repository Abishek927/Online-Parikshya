package com.online.exam.controller;

import com.online.exam.helper.ApiResponse;
import com.online.exam.model.Category;
import com.online.exam.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/user/{userId}/faculty/{facultyId}/category/create")
    ResponseEntity<?> createCategoryController(@PathVariable("userId")Long userId, @PathVariable("facultyId")Long facultyId, @RequestBody Category category) throws Exception {
        Category resultCategory=this.categoryService.createCategory(userId,facultyId,category);
        if(resultCategory.equals(null)){
            return new ResponseEntity<>(new ApiResponse("Something went wrong!!!",false), HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(resultCategory,HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/user/{userId}/faculty/{facultyId}/category/delete")
    ResponseEntity<ApiResponse> deleteCategoryController(@PathVariable("userId")Long userId, @PathVariable("facultyId")Long facultyId, @RequestParam("catName")String catName) throws Exception {
        String message =this.categoryService.deleteCategory(userId,facultyId,catName);
        if(message.isEmpty()){
            return new ResponseEntity<>(new ApiResponse("Something went wrong",false),HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(new ApiResponse(message,true),HttpStatusCode.valueOf(200));
    }

    @GetMapping("/user/{userId}/faculty/{facultyId}/category/read")
    ResponseEntity<?> readCategoryByFacultyController(@PathVariable("userId")Long userId,@PathVariable("facultyId")Long facultyId){
        List<Category> categories =this.categoryService.readCategoryByFaculty(userId, facultyId);
        if(categories.equals(null)){
            return new ResponseEntity<>(new ApiResponse("Something went wrong!!!",false),HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(categories,HttpStatusCode.valueOf(200));
    }

    @GetMapping("/user/{userId}/faculty/{facultyId}/category/readByName")
    ResponseEntity<?> readCategoryByNameController(@PathVariable("userId")Long userId,@PathVariable("facultyId")Long facultyId,@RequestParam("catName")String catName){
        Category resultCategory=this.categoryService.readCategoryByName(userId,facultyId,catName);
        if(resultCategory.equals(null)){
            return new ResponseEntity<>(new ApiResponse("Something went wrong!!!",false),HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(resultCategory,HttpStatusCode.valueOf(200));
    }

}
