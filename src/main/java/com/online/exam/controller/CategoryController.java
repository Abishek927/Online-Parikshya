package com.online.exam.controller;

import com.online.exam.dto.CategoryDto;
import com.online.exam.helper.ApiResponse;
import com.online.exam.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('manage_category')")
    ResponseEntity<?> createCategoryController(@RequestBody CategoryDto categoryDto, Principal principal) throws Exception {
        CategoryDto resultCategoryDto=this.categoryService.createCategory(categoryDto,principal);
        if(resultCategoryDto.equals(null)){
            return new ResponseEntity<>(new ApiResponse("Something went wrong!!!",false), HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(resultCategoryDto,HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('manage_category')")
    ResponseEntity<ApiResponse> deleteCategoryController(@RequestParam("catId")Long catId,Principal principal) throws Exception {
        String message =this.categoryService.deleteCategory(catId,principal);
        if(message.isEmpty()){
            return new ResponseEntity<>(new ApiResponse("Something went wrong",false),HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(new ApiResponse(message,true),HttpStatusCode.valueOf(200));
    }

    @GetMapping("/readByFaculty/{facultyId}")
    ResponseEntity<?> readCategoryByFacultyController(@PathVariable("facultyId")Long facultyId) throws Exception {
        List<CategoryDto> categories =this.categoryService.readCategoryByFaculty(facultyId);
        if(categories.equals(null)){
            return new ResponseEntity<>(new ApiResponse("There is no category for the given faculty",true),HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(categories,HttpStatusCode.valueOf(200));
    }

    @GetMapping("/readById/{catId}")
    ResponseEntity<?> readCategoryByIdController(@RequestParam("catId")Long catId){
        CategoryDto resultCategory=this.categoryService.readCategoryById(catId);
        if(resultCategory.equals(null)){
            return new ResponseEntity<>(new ApiResponse("There is no category by the given id "+catId,true),HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(resultCategory,HttpStatusCode.valueOf(200));
    }
    @GetMapping("/readAll")
    @PreAuthorize("hasAuthority('manage_category')")
    ResponseEntity<?> readAllCategoryController(Principal principal){
        List<CategoryDto> resultCategory=this.categoryService.readAllCategory(principal);
        if(resultCategory.isEmpty()){
            return new ResponseEntity<>(new ApiResponse("There is no category created by the given user",true),HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(resultCategory,HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{categoryId}")
    @PreAuthorize("hasAuthority('manage_category')")
    ResponseEntity<CategoryDto> updateCategoryController(@RequestBody CategoryDto categoryDto,Principal principal) throws Exception {
        CategoryDto categoryDto1=this.categoryService.updateCategory(categoryDto,principal);

        return new ResponseEntity<>(categoryDto1,HttpStatusCode.valueOf(200));
    }
    @GetMapping("/count")
    @PreAuthorize("hasAuthority('manage_category')")
    ResponseEntity<?> countController(Principal principal){
        Integer integer=this.categoryService.countCategory(principal);
        return ResponseEntity.status(HttpStatus.OK).body(integer);
    }

}
