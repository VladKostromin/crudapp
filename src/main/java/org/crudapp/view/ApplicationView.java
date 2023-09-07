package org.crudapp.view;

import org.crudapp.controller.PostController;
import org.crudapp.controller.WriterController;
import org.crudapp.repository.PostRepository;
import org.crudapp.repository.WriterRepository;
import org.crudapp.repository.gson.GsonPostRepositoryImpl;
import org.crudapp.repository.gson.GsonWriterRepositoryImpl;

import java.util.Scanner;

public class ApplicationView {
    private final WriterRepository writerRepository = new GsonWriterRepositoryImpl();
    private final WriterController writerController = new WriterController(writerRepository);
    private final WriterView writerView = new WriterView(writerController);



    public void init() {
        System.out.println("Добро подаловать в приложение CRUD");
        System.out.println("Выберите опцию:");
        writerView.run();
        System.out.println("Программа завершается");

    }
}
