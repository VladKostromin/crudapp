package org.crudapp.repository.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.crudapp.enums.Status;
import org.crudapp.model.Writer;
import org.crudapp.repository.WriterRepository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GsonWriterRepositoryImpl implements WriterRepository {
    private final String PATH_TO_FILE = "src/main/resources/writers.json";
    private Gson gson = new Gson();

    @Override
    public Writer getById(String id) {
        return getWritersInternal().stream()
                .filter(writer -> writer.getStatus().equals(Status.ACTIVE) && writer.getId().equals(id))
                .findFirst().get();
    }

    @Override
    public List<Writer> getAll() {
        return getWritersInternal().stream().filter(writer -> writer.getStatus().equals(Status.ACTIVE)).toList();
    }

    @Override
    public Writer save(Writer writer) {
        List<Writer> writers = getWritersInternal();
        writer.setId(UUID.randomUUID().toString());
        writers.add(writer);
        writeWritersToFile(writers);
        return writer;
    }

    @Override
    public Writer update(Writer writerToUpdate) {
        List<Writer> updatedWriters = getWritersInternal().stream()
                .map(writer -> {
                    if(writer.getId().equals(writerToUpdate.getId())) {
                        return writerToUpdate;
                    }
                    return writer;
                }).toList();
        writeWritersToFile(updatedWriters);
        return writerToUpdate;
    }

    @Override
    public void deleteById(String id) {
        List<Writer> writers = getWritersInternal().stream()
                .map(writer -> {
                    if(writer.getId().equals(id)) {
                        writer.setStatus(Status.DELETED);
                    }
                    return writer;
                }).toList();
        writeWritersToFile(writers);

    }

    private List<Writer> getWritersInternal() {
        try (Reader reader = new FileReader(PATH_TO_FILE)) {
            List<Writer> writers = gson.fromJson(reader, new TypeToken<List<Writer>>(){}.getType());
            if(writers == null) return new ArrayList<>();
            return writers;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    private void writeWritersToFile(List<Writer> labels) {
        try(FileWriter fileWriter = new FileWriter(PATH_TO_FILE)) {
            gson.toJson(labels, fileWriter);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
