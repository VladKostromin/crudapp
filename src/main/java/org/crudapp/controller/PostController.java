package org.crudapp.controller;

import org.crudapp.enums.PostStatus;
import org.crudapp.exceptions.NotFoundException;
import org.crudapp.exceptions.StatusDeletedException;
import org.crudapp.model.Label;
import org.crudapp.model.Post;
import org.crudapp.model.Writer;
import org.crudapp.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(String content) {
        Post post =  Post.builder()
                .postStatus(PostStatus.ACTIVE)
                .labels(new ArrayList<>())
                .content(content)
                .build();
        return postRepository.save(post);
    }

    public Post getPost(String id) {
        return postRepository.getById(id);
    }

    public Post updatePost(Post post) {
        Post postToUpdate = Post.builder()
                .labels(post.getLabels())
                .id(post.getId())
                .content(post.getContent())
                .postStatus(post.getPostStatus())
                .build();
        return postRepository.update(postToUpdate);
    }

    public List<Label> addLabelToPost(Label label, Post post) {
        List<Label> labels = post.getLabels();
        labels.add(label);
        Post postToUpdate = Post.builder()
                .id(post.getId())
                .content(post.getContent())
                .postStatus(PostStatus.ACTIVE)
                .labels(labels)
                .build();
        return postRepository.update(postToUpdate).getLabels();
    }

    public void deletePostById(String id) {
        postRepository.deleteById(id);
    }
}
