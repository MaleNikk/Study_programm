package org.example.component;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Scanner;

@Component
public class ManagementStorage {
    private final Scanner scanner = new Scanner(System.in);
    private final String[] dataPerson = new String[4];
    private final Test test = new Test();
    public void start() {
        while (true) {
            print("""
                    Справочник email адресов.
                      Для добавления адреса введите '1'.
                      Для получения списка адресов введите '2'.
                      Для удаления адреса введите '3'.
                      Для инициализации справочника(очистка списка) введите '4'.
                      Для выхода из справочника введите '5'.
                    """);
            String command = scanner.nextLine();
            StorageContacts storageContacts;

            if (command.contains("1")) {
                try {
                    while (true) {
                        print("Введите имя:");
                        String name = scanner.nextLine();
                        if (test.testString(name)) {
                            dataPerson[0] = name;
                            break;
                        } else {
                            print("Вводите только буквы! Попробуйте еще.");
                        }
                    }
                    while (true) {
                        print("Введите фамилию:");
                        String surname = scanner.nextLine();
                        if (test.testString(surname)) {
                            dataPerson[1] = surname;
                            break;
                        } else {
                            print("Вводите только буквы! Попробуйте еще.");
                        }
                    }
                    while (true) {
                        print("Введите телефон:");
                        String phone = scanner.nextLine();
                        if (test.testPhone(phone)) {
                            dataPerson[2] = phone;
                            break;
                        } else {
                            print("Телефон может содержать только цифры, '+' , '-'! Попробуйте еще.");
                        }
                    }
                    while (true) {
                        print("Введите email:");
                        String email = scanner.nextLine();
                        if (test.testEmail(email)) {
                            dataPerson[3] = email;
                            break;
                        } else {
                            print("Адрес должен содержать буквы, '@', '.'! Попробуйте еще.");
                        }
                    }

                    storageContacts = new StorageContacts();
                    storageContacts.addContact(dataPerson[0].concat(" ").concat(dataPerson[1]).concat(";").
                            concat(dataPerson[2]).concat(";").concat(dataPerson[3]));
                } catch (IOException exception) {
                    print("Что-то пошло не так, попробуйте еще!");
                    continue;
                }
            }
            if (command.contains("2")) {
                storageContacts = new StorageContacts();
                print("Список контактов:\n");
                if (test.testFile("src/main/resources/contacts.txt")) {
                    storageContacts.printContacts();
                } else {
                    print("Список контактов не найден или пуст, добавьте контакт!");
                }
            }
            if (command.contains("3")) {
                storageContacts = new StorageContacts();
                print("Введите email для удаления контакта из справочника.");
                try {
                    storageContacts.removeContact(scanner.nextLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (command.contains("4")) {
                storageContacts = new StorageContacts();
                print("Справочник очищен.\n");
                storageContacts.initNewDirectory();
            }
            if (command.contains("5")) {
                break;
            }
        }
    }
    private void print(String message) {
        System.out.println(message);
    }
}
