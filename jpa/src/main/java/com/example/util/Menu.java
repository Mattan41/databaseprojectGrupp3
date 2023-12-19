package com.example.util;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final String name;
    private final List<MenuItem> menuItems = new ArrayList<>();

    public Menu(String name) {
        this.name = name;
    }

    public void addMenuItem(String description, Runnable action) {
        menuItems.add(new MenuItem(description, action));
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n" + name.toUpperCase());
            System.out.println("Options:");
            for (int i = 0; i < menuItems.size(); i++) {
                System.out.println((i + 1) + " - " + menuItems.get(i).description());
            }
            System.out.println("0. Exit");

            int choice = InputReader.inputInt("Choose an option: ");

            if (choice == 0) {
                break;
            }

            if (choice > 0 && choice <= menuItems.size()) {
                menuItems.get(choice - 1).action().run();
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private record MenuItem(String description, Runnable action) {
    }
}
