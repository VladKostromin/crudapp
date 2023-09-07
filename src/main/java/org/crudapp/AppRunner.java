package org.crudapp;


import org.crudapp.view.ApplicationView;

public class AppRunner {
    public static void main(String[] args) {
        ApplicationView applicationView = new ApplicationView();
        applicationView.init();
    }
}
