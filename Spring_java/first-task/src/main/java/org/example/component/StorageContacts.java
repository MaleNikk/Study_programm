package org.example.component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class StorageContacts {
    private Person person;
    private List<String> contactsInFile = null;

    public void addContact(String dataIncome) throws IOException {
        initList();
        String addContact = "";
        for (String contact : getContactsInFile()) {
            addContact = addContact.concat(contact).concat("\n");
        }

        fileWriter(addContact.concat(dataIncome));

        print("Контакт добавлен.");
    }

    public void removeContact(String email) throws IOException {
        initList();
        String fileData = "";
        for (String contact : getContactsInFile()) {
            if (!contact.contains(email)) {
                fileData = fileData.concat(contact).concat("\n");
            }
        }
        fileWriter(fileData);
        print("Контакт удален.");
    }

    public void printContacts() {
        initList();
        for (String contact : getContactsInFile()) {
            if (contact.contains(";")) {
                setPerson(new Person(personInfo(contact)[0], personInfo(contact)[1],
                        personInfo(contact)[2], personInfo(contact)[3]));
                print(String.valueOf(getPerson().toString()));
            } else {
                print(contact);
            }
        }
    }

    private void fileWriter(String writeFile) throws IOException {
        File file = new File("src/main/resources/contacts.txt");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(writeFile);
        fileWriter.close();
    }

    public void initNewDirectory() {
        try {
            fileWriter("Directory contacts.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initList() {
        try {
            contactsInFile = Files.readAllLines(Paths.get("src/main/resources/contacts.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] personInfo(String incomeData) {
        String[] personData = incomeData.split(";");
        String[] personName = personData[0].split(" ");
        return new String[]{personName[0], personName[1], personData[1], personData[2]};
    }

    private void setPerson(Person person) {
        this.person = person;
    }

    private Person getPerson() {
        return person;
    }

    public List<String> getContactsInFile() {
        return contactsInFile;
    }

    private void print(String message) {
        System.out.println(message);
    }

}
