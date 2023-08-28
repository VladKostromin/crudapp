package org.crudapp.repository.gsonImpl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.crudapp.enums.Status;
import org.crudapp.exceptions.NotFoundException;
import org.crudapp.exceptions.StatusDeletedException;
import org.crudapp.model.Label;
import org.crudapp.repository.LabelRepository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class GsonLabelRepositoryImpl implements LabelRepository {
    private final String PATH_TO_FILE = "src/main/resources/labels.json";
    private Gson gson = new Gson();

    private Long idGenerator() {
        List<Label> labels = getAll();
        Long idToGenerate = 1L;
        if(labels == null || labels.isEmpty()) return idToGenerate;
        idToGenerate = labels.stream().max(Comparator.comparing(Label::getId)).get().getId();
        return ++idToGenerate;
    }
    @Override
    public Label getById(Long id) {
        return getAll().stream().filter(label -> label.getId().equals(id))
                .findFirst().orElseThrow(() -> new NoSuchElementException("No such label with id: " + id));
    }

    @Override
    public List<Label> getAll() {
        try (Reader reader = new FileReader(PATH_TO_FILE)) {
            List<Label> labels = gson.fromJson(reader, new TypeToken<List<Label>>(){}.getType());
            List<Label> filteredLabels = labels.stream().filter((l) -> l.getStatus() == Status.ACTIVE).collect(Collectors.toList());
            if(labels == null) return new ArrayList<>();
            return filteredLabels;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Label save(Label label) {
        List<Label> labels = getAll();
        label.setId(idGenerator());
        labels.add(label);
        try(FileWriter fileWriter = new FileWriter(PATH_TO_FILE)) {
            gson.toJson(labels, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return label;
    }

    @Override
    public Label update(Label label, Long id) throws StatusDeletedException, NotFoundException {
        List<Label> labels = getAll();
        for (Label l : labels) {
            if(l.getId().equals(id)) {
                if(l.getStatus() == Status.DELETED) throw new StatusDeletedException();
                l.setName(label.getName());
                try(FileWriter fileWriter = new FileWriter(PATH_TO_FILE)) {
                    gson.toJson(labels, fileWriter);
                    return l;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        throw new NotFoundException();
    }

    @Override
    public void deleteById(Long id) throws NotFoundException {
        List<Label> labels = getAll();
        try {
            labels.stream().filter(label -> label.getId().equals(id)).findFirst().get().setStatus(Status.DELETED);
        } catch (NoSuchElementException e) {
            throw new NotFoundException();
        }
        try(FileWriter fileWriter = new FileWriter(PATH_TO_FILE)) {
            gson.toJson(labels, fileWriter);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
