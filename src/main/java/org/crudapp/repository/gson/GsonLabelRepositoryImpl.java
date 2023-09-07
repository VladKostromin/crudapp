package org.crudapp.repository.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.crudapp.enums.Status;
import org.crudapp.model.Label;
import org.crudapp.repository.LabelRepository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class GsonLabelRepositoryImpl implements LabelRepository {
    private final String PATH_TO_FILE = "src/main/resources/labels.json";
    private final Gson gson = new Gson();

    private Long idGenerator(List<Label> labels) {
        if(labels == null || labels.isEmpty()) return 1L;
        return labels.stream().mapToLong(l -> l.getId()).max().orElseGet(() -> 0L) + 1;
    }

    private List<Label> getLabelsInternal() {
        try (Reader reader = new FileReader(PATH_TO_FILE)) {
            List<Label> labels = gson.fromJson(reader, new TypeToken<List<Label>>(){}.getType());
            if(labels == null) return new ArrayList<>();
            return labels;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    @Override
    public Label getById(Long id) {
        return getLabelsInternal().stream().filter(label -> label.getId().equals(id))
                .findFirst().orElseThrow(() -> new NoSuchElementException("No such label with id: " + id));
    }

    @Override
    public List<Label> getAll() {
        return getLabelsInternal().stream().filter(l -> l.getStatus().equals(Status.ACTIVE)).toList();
    }

    @Override
    public Label save(Label label) {
        List<Label> labels = getLabelsInternal();
        label.setId(idGenerator(labels));
        labels.add(label);
        writeLabelsToFile(labels);
        return label;
    }

    @Override
    public Label update(Label labelToUpdate) {
        List<Label> updatedLabels = getLabelsInternal().stream()
                .map(label -> {
            if(label.getId().equals(labelToUpdate.getId())) {
                return labelToUpdate;
            }
            return label;
        }).toList();
        writeLabelsToFile(updatedLabels);
        return labelToUpdate;
    }

    @Override
    public void deleteById(Long id) {
        List<Label> updatedLabels = getAll().stream().map(label -> {
            if(label.getId().equals(id)) {
                label.setStatus(Status.DELETED);
            }
            return label;
        }).toList();
        writeLabelsToFile(updatedLabels);
    }

    private void writeLabelsToFile(List<Label> labels) {
        try(FileWriter fileWriter = new FileWriter(PATH_TO_FILE)) {
            gson.toJson(labels, fileWriter);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
