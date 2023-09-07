package org.crudapp.repository.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.crudapp.enums.PostStatus;
import org.crudapp.model.Post;
import org.crudapp.repository.PostRepository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GsonPostRepositoryImpl implements PostRepository {

    private final String PATH_TO_FILE = "src/main/resources/posts.json";
    private final Gson gson = new Gson();

    @Override
    public Post getById(String id) {
        return getPostInternal().stream().filter(post -> post.getId().equals(id))
                .findFirst().get();
    }

    @Override
    public List<Post> getAll() {
        return getPostInternal().stream()
                .filter(post -> post.getPostStatus().equals(PostStatus.ACTIVE)).toList();
    }

    @Override
    public Post save(Post post) {
        List<Post> posts = getPostInternal();
        post.setId(UUID.randomUUID().toString());
        post.setCreated(LocalDateTime.now().toString());
        posts.add(post);
        writePostToPosts(posts);
        return post;
    }

    @Override
    public Post update(Post postToUpdate) {
        List<Post> updatedPosts = getPostInternal().stream()
                .map(post -> {
                    if(post.getId().equals(postToUpdate.getId())) {
                        return postToUpdate;
                    }
                    return post;
                }).toList();
        writePostToPosts(updatedPosts);
        return postToUpdate;
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

    private List<Post> getPostInternal() {
        try (Reader reader = new FileReader(PATH_TO_FILE)) {
            List<Post> posts = gson.fromJson(reader, new TypeToken<List<Post>>(){}.getType());
            if(posts == null) return new ArrayList<>();
            return posts;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    private void writePostToPosts(List<Post> posts) {
        try(FileWriter fileWriter = new FileWriter(PATH_TO_FILE)) {
            gson.toJson(posts, fileWriter);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
