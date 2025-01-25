package com.example.doctorscarespringbootapplication.controller.doctor;

import com.example.doctorscarespringbootapplication.dto.*;
import com.example.doctorscarespringbootapplication.entity.*;
import com.example.doctorscarespringbootapplication.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/doctor")
public class DoctorPostHomepageController {

    @Autowired
    public DoctorPostService doctorPostService;

    @GetMapping("/post-homepage/{page}")
    public String postHomepage(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "Doctor Tips Homepage");
        Page<Posts> postsList = doctorPostService.getPaginatedPosts(page);
        model.addAttribute("postsList", postsList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postsList.getTotalPages());
        doctorPostService.addCommonData(model, principal);
        return "doctor/doctor_post_homepage";
    }

    @PostMapping("/do-post-homepage")
    public String doPostHomepage(@ModelAttribute Posts posts, @RequestParam String doctorID, Model model, Principal principal) {
        boolean isPosted = doctorPostService.createPost(posts, doctorID);
        model.addAttribute("posted", isPosted);
        return "redirect:/doctor/post-homepage/1";
    }

    @PostMapping("/process-like")
    public ResponseEntity<Object> doLikePost(@RequestBody LikesDTO likesDTO) {
        return doctorPostService.handleLikePost(likesDTO);
    }

    @PostMapping("/process-comment")
    public ResponseEntity<Object> doCommentPost(@RequestBody CommentsDTO commentsDTO) {
        return doctorPostService.handleCommentPost(commentsDTO);
    }

    @PostMapping("/process-savepost")
    public ResponseEntity<Object> doSavePost(@RequestBody SavedPostsDTO savedPostsDTO) {
        return doctorPostService.handleSavePost(savedPostsDTO);
    }

    @PostMapping("/process-deletepost")
    public ResponseEntity<Object> doDeletePost(@RequestBody DeletePostDTO deletePostDTO) {
        return doctorPostService.handleDeletePost(deletePostDTO);
    }

    @PostMapping("/process-unsavepost")
    public ResponseEntity<Object> doUnsavePost(@RequestBody UnsavePostDTO unsavePostDTO) {
        return doctorPostService.handleUnsavePost(unsavePostDTO);
    }

    @PostMapping("/edit-post")
    public String editPost(@RequestParam("postId") String postId, Model model) {
        Posts post = doctorPostService.getPostById(postId);
        model.addAttribute("post", post);
        return "doctor/doctor_edit_post";
    }

    @PostMapping("/process-save-editedpost")
    public String saveEditedPost(@RequestParam("postId") String postId, @RequestParam("postContent") String postContent) {
        doctorPostService.updatePostContent(postId, postContent);
        return "redirect:/doctor/post-homepage/1";
    }

    @GetMapping("/saved-tips-posts/{page}")
    public String savedTipsPosts(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "Doctor Saved Tips");
        Page<SavedPosts> savedPostsList = doctorPostService.getSavedPosts(page, principal);
        model.addAttribute("postsList", savedPostsList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", savedPostsList.getTotalPages());
        return "doctor/doctor_saved_tips";
    }
}
