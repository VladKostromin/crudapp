package org.crudapp.controller;

import org.crudapp.enums.Status;
import org.crudapp.exceptions.NotFoundException;
import org.crudapp.exceptions.StatusDeletedException;
import org.crudapp.model.Label;
import org.crudapp.model.Post;
import org.crudapp.model.Writer;
import org.crudapp.repository.WriterRepository;

import java.util.ArrayList;
import java.util.List;

public class WriterController {

    private final WriterRepository writerRepository;

    public WriterController(WriterRepository writerRepository) {
        this.writerRepository = writerRepository;
    }

    public Writer createWriter(String firstName, String lastName) {
        return writerRepository.save(Writer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .status(Status.ACTIVE)
                .posts(new ArrayList<>())
                .build());
    }

    public Writer createWriter(String firstName, String lastName, List<Post> posts) {
        return writerRepository.save(Writer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .status(Status.ACTIVE)
                .posts(posts)
                .posts(new ArrayList<>())
                .build());
    }



    public Writer getWriterById(String id) {
        return writerRepository.getById(id);
    }

    public List<Writer> getAllWriters() {
        return writerRepository.getAll();
    }

    public void deleteById(String id){
        writerRepository.deleteById(id);
    }

    public Writer updateWriter(Writer writer) {
        Writer writerToUpdate = Writer.builder()
                .firstName(writer.getFirstName())
                .lastName(writer.getLastName())
                .build();
        return writerRepository.update(writerToUpdate);
    }

    public List<Post> addNewPostToWriter(Post post, Writer writer){
        List<Post> posts = writer.getPosts();
        posts.add(post);
        Writer writerToUpdate = Writer.builder()
                .firstName(writer.getFirstName())
                .lastName(writer.getLastName())
                .status(writer.getStatus())
                .posts(posts)
                .id(writer.getId())
                .build();
        writerRepository.update(writerToUpdate);
        return posts;
    }

    public List<Post> updatePostToWriter(Post post, Writer writer) {
        List<Post> posts = writer.getPosts();
        for (int i = 0; i < posts.size(); i++) {
            if(posts.get(i).getId().equals(post.getId())) {
                posts.set(i, post);
                break;
            }
        }
        writer.setPosts(posts);
        return writerRepository.update(writer).getPosts();
    }


}
