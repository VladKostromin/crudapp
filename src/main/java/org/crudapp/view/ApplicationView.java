package org.crudapp.view;

import org.crudapp.controller.PostController;
import org.crudapp.controller.WriterController;
import org.crudapp.repository.PostRepository;
import org.crudapp.repository.WriterRepository;
import org.crudapp.repository.gsonImpl.GsonPostRepositoryImpl;
import org.crudapp.repository.gsonImpl.GsonWriterRepositoryImpl;

import java.util.Scanner;

public class ApplicationView {
    private Scanner scanner = new Scanner(System.in);
    WriterRepository writerRepository = new GsonWriterRepositoryImpl();
    WriterController writerController = new WriterController(writerRepository);
    private WriterView writerView = new WriterView(writerController);

    private PostRepository postRepository = new GsonPostRepositoryImpl();
    private PostController postController = new PostController(postRepository);
    private PostView postView = new PostView(postController);


//    private LabelView labelView = new LabelView();


    public void init() {
        System.out.println("Добро подаловать в приложение CRUD");
        System.out.println("Выберите опцию:");
        writerView.run();
        System.out.println("Программа завершается");

    }
}
