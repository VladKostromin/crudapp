package org.crudapp.repository.gsonImpl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.crudapp.enums.PostStatus;
import org.crudapp.exceptions.NotFoundException;
import org.crudapp.exceptions.StatusDeletedException;
import org.crudapp.model.Post;
import org.crudapp.repository.PostRepository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GsonPostRepositoryImpl implements PostRepository {

    private final String PATH_TO_FILE = "src/main/resources/posts.json";
    private Gson gson = new Gson();

    @Override
    public Post getById(String id) throws NotFoundException {
        return getAll().stream().filter(post -> post.getId().equals(id))
                .findFirst().orElseThrow(() -> new NotFoundException());
    }

    @Override
    public List<Post> getAll() {
        try (Reader reader = new FileReader(PATH_TO_FILE)) {
            List<Post> posts = gson.fromJson(reader, new TypeToken<List<Post>>(){}.getType());
            List<Post> filteredPosts = posts.stream()
                    .filter((p) -> p.getPostStatus() == PostStatus.ACTIVE)
                    .collect(Collectors.toList());
            if(posts == null) return new ArrayList<>();
            return filteredPosts;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Post save(Post post) {
        List<Post> posts = getAll();
        try(FileWriter fileWriter = new FileWriter(PATH_TO_FILE)) {
            post.setId(UUID.randomUUID().toString());
            post.setCreated(LocalDateTime.now().toString());
            posts.add(post);
            gson.toJson(posts, fileWriter);
            return post;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public Post update(Post post, String id) throws StatusDeletedException, NotFoundException {
        List<Post> posts = getAll();
        for (Post p : posts) {
            if (p.getId().equals(id)) {
                if(p.getPostStatus() == PostStatus.DELETED) throw new StatusDeletedException();
                p.setUpdated(LocalDateTime.now().toString());
                if(post.getLabels() != null) p.setLabels(post.getLabels());
                if(post.getContent() != null) p.setContent(post.getContent());
                try (FileWriter fileWriter = new FileWriter(PATH_TO_FILE)) {
                    gson.toJson(posts, fileWriter);
                    return p;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new NotFoundException();
    }

    @Override
    public void deleteById(String id) {
        List<Post> posts = getAll();
        posts.stream().filter(post -> post.getId().equals(id)).findFirst().get().setPostStatus(PostStatus.DELETED);
        try(FileWriter fileWriter = new FileWriter(PATH_TO_FILE)) {
            gson.toJson(posts, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
