package org.crudapp.view;

import lombok.Getter;
import org.crudapp.controller.LabelController;
import org.crudapp.controller.PostController;
import org.crudapp.exceptions.NotFoundException;
import org.crudapp.exceptions.StatusDeletedException;
import org.crudapp.model.Label;
import org.crudapp.model.Post;
import org.crudapp.repository.LabelRepository;
import org.crudapp.repository.gsonImpl.GsonLabelRepositoryImpl;

import java.util.List;
import java.util.Scanner;

@Getter
public class PostView {
    private PostController postController;
    private LabelRepository labelRepository = new GsonLabelRepositoryImpl();
    private LabelController labelController = new LabelController(labelRepository);
    private LabelView labelView = new LabelView(labelController);

    private Scanner scanner;

    protected Post post;

    public PostView(PostController postController) {
        this.postController = postController;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
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
                    labelView.run();
                    try {
                        post.setLabels(postController.addLabelToPost(labelView.getLabel(), post));
                        break;
                    } catch (StatusDeletedException e) {
                        System.out.println("Пост удален");
                    } catch (NotFoundException e) {
                        System.out.println("Такого поста не существует");
                    }
                    break;
                case 2 :
                    while (true) {
                        System.out.print("Введите id для поста: ");
                        String id = scanner.nextLine();
                        try {
                            System.out.print("Обновите контент: ");
                            String contentToUpdate = scanner.nextLine();
                            post = postController.updatePost(id, contentToUpdate);
                            labelView.run();
                            try {
                                post.setLabels(postController.updateLabelToPost(labelView.getLabel(), post));
                                break;
                            } catch (StatusDeletedException e) {
                                System.out.println("Пост удален");
                            } catch (NotFoundException e) {
                                System.out.println("Такого поста не существует");
                            }
                            break;
                        } catch (NotFoundException e) {
                            System.out.println("Такого поста не существует ");
                        } catch (StatusDeletedException e) {
                            System.out.println("Пост удален");
                        }
                    }
                    break;
                case 3 :
                    while (true) {
                        System.out.print("Вваедите id для удаления: ");
                        String id = scanner.nextLine();
                        try {
                            postController.deletePostById(id);
                        } catch (StatusDeletedException e) {
                            System.out.println("Поста удален");
                        } catch (NotFoundException e) {
                            System.out.println("Поста с таким id не существует");
                        }
                        break;
                    }
                default: {
                    System.out.println("Ошибка ввода");
                }
            }
            break;
        }
    }
}
