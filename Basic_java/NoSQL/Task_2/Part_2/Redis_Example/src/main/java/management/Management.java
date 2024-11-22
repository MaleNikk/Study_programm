package management;

import component.UserGenerator;
import storage.RedisDataBase;

import java.text.MessageFormat;
import java.util.Random;

public class Management {
    private final RedisDataBase dataBase;
    private final UserGenerator userGenerator;

    public Management(){
        System.out.println("Start management.");
        this.dataBase = new RedisDataBase();
        this.userGenerator = new UserGenerator(getDataBase());
        getUserGenerator().generateUsers();
    }

    public void showAllUsers() throws InterruptedException {
        int countUser = 1;
        String message = "\n{ Показ пользователя на главной странице! }\n";
        do {
            getDataBase().findUserByKey(message,String.valueOf(countUser));
            if (checkShowFirst()){
                int userViP = keyFirst();
                String messageViP = MessageFormat.format("\n* UserViP {0} show first! * \n",userViP);
                getDataBase().findUserByKey(messageViP,String.valueOf(userViP));
                System.out.println("\n\t\t*** You want to be in the forefront! You need to purchase ViP! ***\n");
                Thread.sleep(1000);
            }
            countUser++;
            Thread.sleep(500);
            if (countUser == 25){ countUser = 1;}
        } while (getDataBase().getClient().getJedis().exists(String.valueOf(countUser)));
    }
    private boolean checkShowFirst(){
        return new Random().nextBoolean();
    }
    private RedisDataBase getDataBase() {
        return dataBase;
    }
    public UserGenerator getUserGenerator() {
        return userGenerator;
    }
    private Integer keyFirst(){
        return new Random().nextInt(1,25);
    }
}
