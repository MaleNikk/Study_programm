package storage;

import connection.RedisClient;

public final class RedisDataBase {
    private final RedisClient client;
    public RedisDataBase() {
        System.out.println("Start connection.");
        this.client = new RedisClient();
    }
    public void saveUser(String key,String volume) {
        print("User ".concat(key).concat(" saved : ").concat(getClient().getJedis().set(key,volume)));
    }
    public void findUserByKey(String message, String key) {
        print(message);
        print(getClient().getJedis().get(key));
    }
    private void print(String message){
        System.out.println(message);
    }
    public RedisClient getClient() { return client;  }
}
