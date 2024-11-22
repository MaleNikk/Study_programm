package component;

public class User {
    private final String name, surname, age;
    public User(String name,String surname,String age){
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    @Override
    public String toString(){
      return getName().concat(" ").concat(getSurname()).concat(" ").concat(getAge());
    }
    public String getName() { return name; }

    public String getSurname() { return surname; }

    public String getAge() { return age; }
}
