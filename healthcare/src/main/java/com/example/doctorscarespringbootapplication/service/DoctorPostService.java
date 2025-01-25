package com.example.doctorscarespringbootapplication.service;

import com.example.doctorscarespringbootapplication.dao.*;
import com.example.doctorscarespringbootapplication.dto.*;
import com.example.doctorscarespringbootapplication.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DoctorPostService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private SavedPostsRepository savedPostsRepository;

    public Page<Posts> getPaginatedPosts(int page) {
        return postsRepository.findAllByOrderByIdDesc(PageRequest.of(page - 1, 5));
    }

    public boolean createPost(Posts posts, String doctorID) {
        if (!posts.getCoverPhoto().isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            posts.setPostDate(Date.valueOf(now.toLocalDate()));
            posts.setPostTime(Time.valueOf(now.toLocalTime()));
            User doctorUser = userRepository.findById(Integer.parseInt(doctorID));
            posts.setUser(doctorUser);
            postsRepository.save(posts);
            return true;
        }
        return false;
    }

    public ResponseEntity<Object> handleLikePost(LikesDTO likesDTO) {
        // Logic for processing likes
        return ResponseEntity.ok("Liked successfully!");
    }

    public ResponseEntity<Object> handleCommentPost(CommentsDTO commentsDTO) {
        Posts post = postsRepository.findById(Integer.parseInt(commentsDTO.getPostId()));
        Comments comment = new Comments(commentsDTO.getCommenterId(), commentsDTO.getCommenterName(), commentsDTO.getCommenterImage(), commentsDTO.getComment(), post);
        commentsRepository.save(comment);
        return ResponseEntity.ok("Comment added successfully!");
    }

    public ResponseEntity<Object> handleSavePost(SavedPostsDTO savedPostsDTO) {
        // Logic for saving a post
        return ResponseEntity.ok("Post saved successfully!");
    }

    public ResponseEntity<Object> handleDeletePost(DeletePostDTO deletePostDTO) {
        postsRepository.deleteById(Integer.parseInt(deletePostDTO.getPostId()));
        return ResponseEntity.ok("Post deleted successfully!");
    }

    public ResponseEntity<Object> handleUnsavePost(UnsavePostDTO unsavePostDTO) {
        // Logic for unsaving a post
        return ResponseEntity.ok("Post unsaved successfully!");
    }

    public Posts getPostById(String postId) {
        return postsRepository.findById(Integer.parseInt(postId));
    }

    public void updatePostContent(String postId, String postContent) {
        Posts post = postsRepository.findById(Integer.parseInt(postId));
        post.setPostContent(postContent);
        postsRepository.save(post);
    }

    public Page<SavedPosts> getSavedPosts(int page, Principal principal) {
        User user = userRepository.getUserByEmailNative(principal.getName());
        return savedPostsRepository.findBySaverId(user.getId() + "", PageRequest.of(page - 1, 5));
    }

    public void addCommonData(Model model, Principal principal) {
        User user = userRepository.getUserByEmailNative(principal.getName());
        model.addAttribute("user", user);
    }
}
