package org.crudapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.crudapp.enums.Status;

@Builder
@Getter
@Setter
@ToString
public class Label {
    private Long id;
    private String name;
    private Status status;
}
