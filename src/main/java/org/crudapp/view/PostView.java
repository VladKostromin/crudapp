package org.crudapp.view;

import lombok.Getter;
import org.crudapp.controller.LabelController;
import org.crudapp.controller.PostController;
import org.crudapp.exceptions.NotFoundException;
import org.crudapp.exceptions.StatusDeletedException;
import org.crudapp.model.Label;
import org.crudapp.model.Post;
import org.crudapp.repository.LabelRepository;
import org.crudapp.repository.gson.GsonLabelRepositoryImpl;

import java.util.List;
import java.util.Scanner;

@Getter
public class PostView {
    private final PostController postController;
    private final LabelRepository labelRepository = new GsonLabelRepositoryImpl();
    private final LabelController labelController = new LabelController(labelRepository);
    private final LabelView labelView = new LabelView(labelController);

    private final Scanner scanner;

    public PostView(PostController postController) {
        this.postController = postController;
        this.scanner = new Scanner(System.in);
    }

    public Post run() {
        Post post;
        Label label;
        int input;
        boolean flag = true;
        while (flag) {
            System.out.println("1. Создать новый пост");
            System.out.println("2. Обновить существующий пост");
            System.out.println("3. Удалить пост");
            input = scanner.nextInt();
            scanner.nextLine();
            switch (input) {
                case 1 :
                    System.out.print("Введите контент поста: ");
                    String content = scanner.nextLine();
                    post = postController.createPost(content);
                    label = labelView.run();
                    postController.addLabelToPost(label, post);
                    return post;
                case 2 :
                    while (true) {
                        System.out.print("Введите id для поста: ");
                        String id = scanner.nextLine();
                        post = postController.getPost(id);
                        System.out.print("Обновите контент: ");
                        String contentToUpdate = scanner.nextLine();
                        post.setContent(contentToUpdate);
                        post.setId(id);
                        postController.updatePost(post);
                        label = labelView.run();
                        postController.addLabelToPost(label, post);
                        return post;
                    }
                case 3 :
                    while (true) {
                        System.out.print("Вваедите id для удаления: ");
                        String id = scanner.nextLine();
                        postController.deletePostById(id);
                        break;
                    }
                default: {
                    System.out.println("Ошибка ввода");
                }
            }
            break;
        }
        return null;
    }
}
