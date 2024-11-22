package connection;

import redis.clients.jedis.Jedis;
public final class RedisClient {
    private final Jedis jedis;
    public RedisClient() {
        System.out.println("Start Redis client.");
        this.jedis = new Jedis("localhost", 6379);
    }
    public Jedis getJedis() {
        return jedis;
    }
}
