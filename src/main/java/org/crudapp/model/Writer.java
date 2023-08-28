package org.crudapp.model;

import lombok.*;
import org.crudapp.enums.Status;

import java.util.List;


@Builder
@Getter
@Setter
@ToString
public class Writer {
    private String id;
    private String firstName;
    private String lastName;
    private List<Post> posts;
    private Status status;
}
