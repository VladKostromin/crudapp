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

    public Post getPost(String id) throws NotFoundException {
        return postRepository.getById(id);
    }

    public Post updatePost(String id, String content) throws StatusDeletedException, NotFoundException {
       Post postToUpdate = Post.builder()
               .content(content)
               .build();

       return postRepository.update(postToUpdate, id);
    }

    public List<Label> addLabelToPost(Label label, Post post) throws StatusDeletedException, NotFoundException {
        List<Label> labels = post.getLabels();
        labels.add(label);
        Post postToUpdate = Post.builder()
                .labels(labels)
                .build();
        postRepository.update(postToUpdate, post.getId());
        return postRepository.getById(post.getId()).getLabels();
    }

    public List<Label> updateLabelToPost(Label label, Post post) throws StatusDeletedException, NotFoundException {
        List<Label> labels = post.getLabels();
        for (int i = 0; i < labels.size(); i++) {
            if(labels.get(i).getId().equals(post.getId())) {
                labels.set(i, label);
                break;
            }
        }
        post.setLabels(labels);
        return postRepository.update(post, post.getId()).getLabels();
    }

    public void deletePostById(String id) throws StatusDeletedException, NotFoundException {
        postRepository.deleteById(id);
    }
}
