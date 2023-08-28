package org.crudapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.crudapp.enums.PostStatus;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class Post {
    private String id;
    private String content;
    private String created;
    private String updated;
    private List<Label> labels;
    private PostStatus postStatus;
}
