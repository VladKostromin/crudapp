package org.crudapp.repository.gsonImpl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.crudapp.enums.PostStatus;
import org.crudapp.enums.Status;
import org.crudapp.exceptions.NotFoundException;
import org.crudapp.exceptions.StatusDeletedException;
import org.crudapp.model.Post;
import org.crudapp.model.Writer;
import org.crudapp.repository.WriterRepository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

public class GsonWriterRepositoryImpl implements WriterRepository {
    private final String PATH_TO_FILE = "src/main/resources/writers.json";
    private Gson gson = new Gson();

    @Override
    public Writer getById(String id) throws NotFoundException {
        return getAll().stream().filter(writer -> writer.getId().equals(id))
                .findFirst().orElseThrow(() -> new NotFoundException());
    }

    @Override
    public List<Writer> getAll() {
        try (Reader reader = new FileReader(PATH_TO_FILE)) {
            List<Writer> writers = gson.fromJson(reader, new TypeToken<List<Writer>>(){}.getType());
            if(writers == null) return new ArrayList<>();
            List<Writer> filteredWriters = writers.stream().filter((w) -> w.getStatus() == Status.ACTIVE).collect(Collectors.toList());
            return filteredWriters;
        } catch (Exception e) {
            System.err.println("something went wrong" + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Writer save(Writer writer) {
        List<Writer> writers = getAll();
        try(FileWriter fileWriter = new FileWriter(PATH_TO_FILE)) {
            writer.setId(UUID.randomUUID().toString());
            writers.add(writer);
            gson.toJson(writers, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer;
    }

    @Override
    public Writer update(Writer writer, String id) throws NotFoundException, StatusDeletedException {
        List<Writer> writers = getAll();
        for (Writer wr : writers) {
            if(wr.getId().equals(id)) {
                if(wr.getStatus() == Status.DELETED) throw new StatusDeletedException();
                if(writer.getFirstName() != null) wr.setFirstName(writer.getFirstName());
                if(writer.getLastName() != null) wr.setLastName(writer.getLastName());
                if(writer.getPosts() != null) wr.setPosts(writer.getPosts());
                try(FileWriter fileWriter = new FileWriter(PATH_TO_FILE)) {
                    gson.toJson(writers, fileWriter);
                    return wr;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new NotFoundException();
    }

    @Override
    public void deleteById(String id) throws NotFoundException {
        List<Writer> writers = getAll();
        try {
            writers.stream().filter(post -> post.getId().equals(id)).findFirst().get().setStatus(Status.DELETED);
        } catch (NoSuchElementException e) {
            throw new NotFoundException();
        }
        try(FileWriter fileWriter = new FileWriter(PATH_TO_FILE)) {
            gson.toJson(writers, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
