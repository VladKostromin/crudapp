package org.crudapp.view;

import lombok.Getter;
import org.crudapp.controller.PostController;
import org.crudapp.controller.WriterController;
import org.crudapp.exceptions.NotFoundException;
import org.crudapp.exceptions.StatusDeletedException;
import org.crudapp.model.Post;
import org.crudapp.model.Writer;
import org.crudapp.repository.PostRepository;
import org.crudapp.repository.gson.GsonPostRepositoryImpl;

import java.util.Scanner;

@Getter
public class WriterView {
    private final WriterController writerController;
    private final PostRepository postRepository = new GsonPostRepositoryImpl();
    private final PostController postController = new PostController(postRepository);
    private final PostView postView = new PostView(postController);
    private final Scanner scanner;

    public WriterView(WriterController writerController) {
        this.writerController = writerController;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        Writer writer;
        Post post;
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
                    writer = writerController.createWriter(firstName, lastName);
                    post = postView.run();
                    writerController.addNewPostToWriter(post, writer);
                    break;
                case 2 :
                    while(true) {
                        System.out.print("Введите id: ");
                        String id = scanner.nextLine();
                        writer = writerController.getWriterById(id);
                        post = postView.run();
                        writerController.updatePostToWriter(post, writer);
                        break;
                    }
                    break;
                case 3 :
                    while(true) {
                        System.out.print("Введите id: ");
                        String id = scanner.nextLine();
                        writer = writerController.getWriterById(id);
                        while (true) {
                            System.out.print("Введите имя для обновления: ");
                            String nameForUpdate = scanner.nextLine();
                            System.out.print("Введите фамилию для обновления: ");
                            String lastNameForUpdate = scanner.nextLine();
                            writer.setFirstName(nameForUpdate);
                            writer.setLastName(lastNameForUpdate);
                            writerController.updateWriter(writer);
                            break;
                        }
                        break;
                    }
                    break;
                case 4 :
                    while (true) {
                        System.out.println("Введите id для удаления");
                        String id = scanner.nextLine();
                        writerController.deleteById(id);
                        break;
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
