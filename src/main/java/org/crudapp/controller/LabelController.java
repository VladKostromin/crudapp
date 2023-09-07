package org.crudapp.controller;

import org.crudapp.enums.Status;
import org.crudapp.exceptions.NotFoundException;
import org.crudapp.exceptions.StatusDeletedException;
import org.crudapp.model.Label;
import org.crudapp.repository.LabelRepository;

import java.util.List;

public class LabelController {
    private final LabelRepository labelRepository;

    public LabelController(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public Label createLabel(String name) {
        Label label = Label.builder()
                .name(name)
                .status(Status.ACTIVE)
                .build();
        return labelRepository.save(label);
    }

    public Label updateLabel(Label label) {
        Label labelToUpdate = Label.builder()
                .id(label.getId())
                .name(label.getName())
                .status(label.getStatus())
                .build();
        return labelRepository.update(labelToUpdate);
    }

    public void deleteById(Long id) {
        labelRepository.deleteById(id);
    }

}
