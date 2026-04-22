package com.favorites.web;

import com.favorites.domain.Category;
import com.favorites.domain.User;
import com.favorites.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public Map<String, Object> addCategory(@RequestParam String name, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            result.put("code", 401);
            result.put("msg", "Please login first!");
            return result;
        }
        try {
            Category category = categoryService.addCategory(name, loginUser.getId());
            result.put("code", 200);
            result.put("msg", "Add category success!");
            result.put("data", category);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @GetMapping("/list")
    public Map<String, Object> getUserCategories(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            result.put("code", 401);
            result.put("msg", "Please login first!");
            return result;
        }
        List<Category> categories = categoryService.getUserCategories(loginUser.getId());
        result.put("code", 200);
        result.put("data", categories);
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> updateCategory(@RequestParam Long id, @RequestParam String name, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            result.put("code", 401);
            result.put("msg", "Please login first!");
            return result;
        }
        try {
            Category category = categoryService.updateCategory(id, name, loginUser.getId());
            result.put("code", 200);
            result.put("msg", "Update category success!");
            result.put("data", category);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @PostMapping("/delete")
    public Map<String, Object> deleteCategory(@RequestParam Long id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            result.put("code", 401);
            result.put("msg", "Please login first!");
            return result;
        }
        try {
            categoryService.deleteCategory(id, loginUser.getId());
            result.put("code", 200);
            result.put("msg", "Delete category success!");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }
}