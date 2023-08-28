package org.crudapp.controller;

import org.crudapp.enums.Status;
import org.crudapp.exceptions.NotFoundException;
import org.crudapp.exceptions.StatusDeletedException;
import org.crudapp.model.Label;
import org.crudapp.repository.LabelRepository;

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

    public Label updateLabel(String name, Long id) throws NotFoundException, StatusDeletedException {
        Label labelToUpdate = Label.builder()
                .name(name)
                .build();
        return labelRepository.update(labelToUpdate, id);
    }

    public void deleteById(Long id) throws StatusDeletedException, NotFoundException {
        labelRepository.deleteById(id);
    }
}
