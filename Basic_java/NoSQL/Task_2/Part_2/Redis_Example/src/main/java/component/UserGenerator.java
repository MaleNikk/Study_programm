package component;

import storage.RedisDataBase;
import java.util.Random;

public final class UserGenerator {
    private final RedisDataBase dataBase;

    public UserGenerator(RedisDataBase redisDataBase) {
        System.out.println("Init generate default users.");
        this.dataBase = redisDataBase;
    }

    public void generateUsers() {
        String name = "name";
        String surname = "surname";
        String age = "age";
        Random random = new Random();
        int countUser = 0;
        do {
            countUser++;
            User user = new User(
                    name.concat(":").concat(String.valueOf(countUser)),
                    surname.concat(":").concat(String.valueOf(countUser)),
                    age.concat(":").concat(String.valueOf(random.nextInt(19, 35))));
            getDataBase().saveUser(String.valueOf(countUser),user.toString());
        } while (countUser < 25);
        System.out.println("\nAll users added.\n");
    }
    public RedisDataBase getDataBase() {
        return dataBase;
    }
}
