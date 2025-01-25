package com.example.doctorscarespringbootapplication.Controller;

import com.example.doctorscarespringbootapplication.controller.doctor.DoctorPostHomepageController;
import com.example.doctorscarespringbootapplication.dto.*;
import com.example.doctorscarespringbootapplication.entity.*;
import com.example.doctorscarespringbootapplication.service.DoctorPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DoctorPostHomepageControllerTest {

    private DoctorPostHomepageController controller;

    @Mock
    private DoctorPostService doctorPostService;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new DoctorPostHomepageController();
        controller.doctorPostService = doctorPostService;
    }

    @Test
    void testPostHomepage() {
        // Mock data
        Page<Posts> postsPage = new PageImpl<>(Collections.emptyList());
        when(doctorPostService.getPaginatedPosts(1)).thenReturn(postsPage);

        // Execute
        String viewName = controller.postHomepage(1, model, principal);

        // Verify
        assertEquals("doctor/doctor_post_homepage", viewName);
        verify(model).addAttribute("title", "Doctor Tips Homepage");
        verify(model).addAttribute("postsList", postsPage);
        verify(model).addAttribute("currentPage", 1);
        verify(model).addAttribute("totalPages", postsPage.getTotalPages());
        verify(doctorPostService).addCommonData(model, principal);
    }

    @Test
    void testDoPostHomepage_Success() {
        // Mock data
        Posts posts = new Posts();
        String doctorID = "1";
        when(doctorPostService.createPost(posts, doctorID)).thenReturn(true);

        // Execute
        String redirectUrl = controller.doPostHomepage(posts, doctorID, model, principal);

        // Verify
        assertEquals("redirect:/doctor/post-homepage/1", redirectUrl);
        verify(doctorPostService).createPost(posts, doctorID);
    }

    @Test
    void testDoLikePost() {
        // Mock data
        LikesDTO likesDTO = new LikesDTO();
        when(doctorPostService.handleLikePost(likesDTO)).thenReturn(ResponseEntity.ok("Liked successfully!"));

        // Execute
        ResponseEntity<Object> response = controller.doLikePost(likesDTO);

        // Verify
        assertEquals(ResponseEntity.ok("Liked successfully!"), response);
        verify(doctorPostService).handleLikePost(likesDTO);
    }

    @Test
    void testDoCommentPost() {
        // Mock data
        CommentsDTO commentsDTO = new CommentsDTO();
        when(doctorPostService.handleCommentPost(commentsDTO)).thenReturn(ResponseEntity.ok("Comment added successfully!"));

        // Execute
        ResponseEntity<Object> response = controller.doCommentPost(commentsDTO);

        // Verify
        assertEquals(ResponseEntity.ok("Comment added successfully!"), response);
        verify(doctorPostService).handleCommentPost(commentsDTO);
    }

    @Test
    void testDoSavePost() {
        // Mock data
        SavedPostsDTO savedPostsDTO = new SavedPostsDTO();
        when(doctorPostService.handleSavePost(savedPostsDTO)).thenReturn(ResponseEntity.ok("Post saved successfully!"));

        // Execute
        ResponseEntity<Object> response = controller.doSavePost(savedPostsDTO);

        // Verify
        assertEquals(ResponseEntity.ok("Post saved successfully!"), response);
        verify(doctorPostService).handleSavePost(savedPostsDTO);
    }

    @Test
    void testDoDeletePost() {
        // Mock data
        DeletePostDTO deletePostDTO = new DeletePostDTO();
        when(doctorPostService.handleDeletePost(deletePostDTO)).thenReturn(ResponseEntity.ok("Post deleted successfully!"));

        // Execute
        ResponseEntity<Object> response = controller.doDeletePost(deletePostDTO);

        // Verify
        assertEquals(ResponseEntity.ok("Post deleted successfully!"), response);
        verify(doctorPostService).handleDeletePost(deletePostDTO);
    }

    @Test
    void testEditPost() {
        // Mock data
        Posts post = new Posts();
        when(doctorPostService.getPostById("1")).thenReturn(post);

        // Execute
        String viewName = controller.editPost("1", model);

        // Verify
        assertEquals("doctor/doctor_edit_post", viewName);
        verify(model).addAttribute("post", post);
    }

    @Test
    void testSaveEditedPost() {
        // Execute
        String redirectUrl = controller.saveEditedPost("1", "Updated Content");

        // Verify
        assertEquals("redirect:/doctor/post-homepage/1", redirectUrl);
        verify(doctorPostService).updatePostContent("1", "Updated Content");
    }

    @Test
    void testSavedTipsPosts() {
        // Mock data
        Page<SavedPosts> savedPostsPage = new PageImpl<>(Collections.emptyList());
        when(doctorPostService.getSavedPosts(1, principal)).thenReturn(savedPostsPage);

        // Execute
        String viewName = controller.savedTipsPosts(1, model, principal);

        // Verify
        assertEquals("doctor/doctor_saved_tips", viewName);
        verify(model).addAttribute("title", "Doctor Saved Tips");
        verify(model).addAttribute("postsList", savedPostsPage);
        verify(model).addAttribute("currentPage", 1);
        verify(model).addAttribute("totalPages", savedPostsPage.getTotalPages());
    }
}
