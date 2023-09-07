package org.crudapp.view;

import lombok.Getter;
import org.crudapp.controller.LabelController;
import org.crudapp.exceptions.NotFoundException;
import org.crudapp.exceptions.StatusDeletedException;
import org.crudapp.model.Label;

import java.util.Scanner;
@Getter
public class LabelView {

    private final LabelController labelController;
    private final Scanner scanner;


    public LabelView(LabelController labelController) {
        this.labelController = labelController;
        this.scanner = new Scanner(System.in);
    }

    public Label run() {
        Label label = null;
        int input;
        boolean flag = true;
        while (flag) {
            System.out.println("Добро пожаловать в создание Label, выберите доступные опции:");
            System.out.println("1. Создать Label");
            System.out.println("2. Обновить Label");
            System.out.println("3. Удалить Label");
            System.out.println("4. Вернутся в меню Writer");
            input = scanner.nextInt();
            scanner.nextLine();

            switch (input) {
                case 1 :
                    System.out.print("Введите название Label: ");
                    String name = scanner.nextLine();
                    label = labelController.createLabel(name);
                    return label;
                case 2 :
                    while (true) {
                        System.out.print("Введите id Label: ");
                        Long id = scanner.nextLong();
                        scanner.nextLine();
                        System.out.print("Введите название для обновления Label: ");
                        String nameToUpdate = scanner.nextLine();
                        label.setId(id);
                        label.setName(nameToUpdate);
                        labelController.updateLabel(label);
                        return label;
                    }
                case 3 :
                    while (true) {
                        System.out.print("Введите id для удаления: ");
                        Long id = scanner.nextLong();
                        labelController.deleteById(id);
                    }
                case 4 :
                    flag = false;
                    break;
                default: {
                    System.out.println("Ошибка ввода");
                }
            }
            break;
        }
        return null;
    }
}
