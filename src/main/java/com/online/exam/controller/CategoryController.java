package com.online.exam.controller;

import com.online.exam.dto.CategoryDto;
import com.online.exam.helper.ApiResponse;
import com.online.exam.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/user/{userId}/faculty/{facultyId}/category/create")
    @PreAuthorize("hasAuthority('manage_category')")
    ResponseEntity<?> createCategoryController(@PathVariable("userId")Long userId, @PathVariable("facultyId")Long facultyId, @RequestBody CategoryDto categoryDto) throws Exception {
        CategoryDto resultCategoryDto=this.categoryService.createCategory(userId,facultyId,categoryDto);
        if(resultCategoryDto.equals(null)){
            return new ResponseEntity<>(new ApiResponse("Something went wrong!!!",false), HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(resultCategoryDto,HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/user/{userId}/faculty/{facultyId}/category/delete")
    @PreAuthorize("hasAuthority('manage_category')")
    ResponseEntity<ApiResponse> deleteCategoryController(@PathVariable("userId")Long userId, @PathVariable("facultyId")Long facultyId, @RequestParam("catId")Long catId) throws Exception {
        String message =this.categoryService.deleteCategory(userId,facultyId,catId);
        if(message.isEmpty()){
            return new ResponseEntity<>(new ApiResponse("Something went wrong",false),HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(new ApiResponse(message,true),HttpStatusCode.valueOf(200));
    }

    @GetMapping("/user/{userId}/faculty/{facultyId}/category/read")
    @PreAuthorize("hasAuthority('manage_category')")
    ResponseEntity<?> readCategoryByFacultyController(@PathVariable("userId")Long userId,@PathVariable("facultyId")Long facultyId) throws Exception {
        List<CategoryDto> categories =this.categoryService.readCategoryByFaculty(userId, facultyId);
        if(categories.equals(null)){
            return new ResponseEntity<>(new ApiResponse("There is no category for the given faculty",true),HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(categories,HttpStatusCode.valueOf(200));
    }

    @GetMapping("/user/{userId}/faculty/{facultyId}/category/readById")
    @PreAuthorize("hasAuthority('manage_category')")
    ResponseEntity<?> readCategoryByNameController(@PathVariable("userId")Long userId,@PathVariable("facultyId")Long facultyId,@RequestParam("catId")Long catId){
        CategoryDto resultCategory=this.categoryService.readCategoryByName(userId,facultyId,catId);
        if(resultCategory.equals(null)){
            return new ResponseEntity<>(new ApiResponse("There is no category by the given id "+catId,true),HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(resultCategory,HttpStatusCode.valueOf(200));
    }

    @PutMapping("/user/{userId}/faculty/{facultyId}/category/update/{categoryId}")
    @PreAuthorize("hasAuthority('manage_category')")
    ResponseEntity<CategoryDto> updateCategoryController(@PathVariable Long userId,@PathVariable Long facultyId,@PathVariable Long categoryId,@RequestBody CategoryDto categoryDto) throws Exception {
        CategoryDto categoryDto1=this.categoryService.updateCategory(userId,facultyId,categoryId,categoryDto);

        return new ResponseEntity<>(categoryDto1,HttpStatusCode.valueOf(200));
    }

}
