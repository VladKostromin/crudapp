package org.crudapp.view;

import lombok.Getter;
import org.crudapp.controller.PostController;
import org.crudapp.controller.WriterController;
import org.crudapp.exceptions.NotFoundException;
import org.crudapp.exceptions.StatusDeletedException;
import org.crudapp.model.Writer;
import org.crudapp.repository.PostRepository;
import org.crudapp.repository.gsonImpl.GsonPostRepositoryImpl;

import java.util.Scanner;

@Getter
public class WriterView {
    private final WriterController writerController;
    private PostRepository postRepository = new GsonPostRepositoryImpl();
    private PostController postController = new PostController(postRepository);
    private PostView postView = new PostView(postController);
    private Scanner scanner;
    private Writer writer;

    public WriterView(WriterController writerController) {
        this.writerController = writerController;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean flag = true;
        int input;
        while (flag) {
            System.out.println("1. Создать нового Writer");
            System.out.println("2. Выбрать существующего Writer");
            System.out.println("3. Обновить существующего Writer");
            System.out.println("4. Удалить Writer");
            System.out.println("5. Выход");
            input = scanner.nextInt();
            scanner.nextLine();
            switch (input) {
                case 1 :
                    System.out.print("Введите имя: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Введите фамилиюю: ");
                    String lastName = scanner.nextLine();
                    writer = writerController.createWriter(firstName,lastName);
                    postView.run();
                    try {
                        writer.setPosts(writerController.addNewPostToWriter(postView.getPost(), writer));
                        break;
                    } catch (StatusDeletedException e) {
                        System.out.println("Writer удален");
                    } catch (NotFoundException e) {
                        System.out.println("Такого Writer не существует");
                    }
                    return;
                case 2 :
                    while(true) {
                        System.out.print("Введите id: ");
                        String id = scanner.nextLine();
                        try {
                            writer = writerController.getWriterById(id);
                            postView.run();
                            writer.setPosts(writerController.updatePostToWriter(postView.getPost(), writer));
                            break;
                        } catch (NotFoundException e) {
                            System.out.println("Такого id не существует");
                        } catch (StatusDeletedException e) {
                            System.out.println("Writer удален");
                        }
                        break;
                    }
                    break;
                case 3 :
                    while(true) {
                        System.out.print("Введите id: ");
                        String id = scanner.nextLine();
                        try {
                            writer = writerController.getWriterById(id);
                        } catch (NotFoundException e) {
                            System.out.println("Такого id не существует");
                        }
                        while (true) {
                            System.out.print("Введите имя для обновления: ");
                            String nameForUpdate = scanner.nextLine();
                            System.out.print("Введите фамилию для обновления: ");
                            String lastNameForUpdate = scanner.nextLine();
                            try {
                                writer = writerController.updateWriter(nameForUpdate, lastNameForUpdate, id);
                                break;
                            } catch (NotFoundException e) {
                                System.out.println("Такого Writer не существует, введите правильного Writer");
                            } catch (StatusDeletedException e) {
                                System.out.println("Writer удален, введите другого Writer");
                            }

                        }
                        break;
                    }
                    break;
                case 4 :
                    while (true) {
                        System.out.println("Введите id для удаления");
                        String id = scanner.nextLine();
                        try {
                            writerController.deleteById(id);
                            break;
                        } catch (StatusDeletedException e) {
                            System.out.println("Ваш Writer уже удален.");
                        } catch (NotFoundException e) {
                            System.out.println("Такого id не существует");
                        }
                    }
                    break;
                case 5 :
                    flag = false;
                    break;
                default: {
                    System.out.println("Ошибка ввода");
                }
            }
        }
    }
}
