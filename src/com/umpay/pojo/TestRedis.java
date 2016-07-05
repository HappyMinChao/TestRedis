package com.umpay.pojo;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.umpay.utils.JedisPoolUtil;
import com.umpay.utils.SerializeUtil;

public class TestRedis {

    private Jedis jedis = null;

    @Test
    public void test11() {
        // 获取 Jedis连接池实例
        JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();

        // 准备测试数据
        User user1 = new User("user01", "nan", "已登录");
        User user2 = new User("user02", "nv", "未登录");

        try {
            // 获取jedis客户端
            jedis = jedisPool.getResource();

            // 清空redis中所有数据
            jedis.flushAll();

            // hset 使用在存储对象过程中避免 序列化， 可以使用： key 属性名 value 进行存储
            jedis.hset(user1.getUsername(), "username", user1.getUsername());
            jedis.hset(user1.getUsername(), "state", user1.getState());

            // 根据key获取到所有的 属性名 和 属性值
            Map<String, String> user01 = jedis.hgetAll(user1.getUsername());
            // 遍历所有的属性名和属性值
            Set<Entry<String, String>> entrySet = user01.entrySet();
            for (Entry<String, String> entry : entrySet) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println(key + " : " + value);
            }

            // 如果对应的key的值没有， 则map不是null ， 而是size为0
            Map<String, String> user02 = jedis.hgetAll(user2.getUsername());
            if (user02 == null || user02.size() == 0) {
                System.out.println("user02 is null");
            }
            // 使用序列化向 redis中存储对象
            this.set(user1, user1.getUsername());
            this.set(user2, user2.getUsername());
            // 取出时需要反序列化
            System.out.println(this.get(user1.getUsername()));
            System.out.println(this.get(user2.getUsername()));

            // 向redis中添加测试数据
            jedis.set("key01", "value01");
            jedis.set("key02", "value02");
            // 设置key02过期时间， 3秒
            jedis.expire("key02", 3);

            String key02 = jedis.get("key02");
            System.out.println("key02 : " + key02);
            // 进程阻塞3秒后在吃获取
            Thread.sleep(3000);
            // 获取到的值为null， 而不是空串
            System.out.println("Thread key02 : " + jedis.get("key02"));

            // 获取redis中所有key为key开头的键值对
            Set<String> keys = jedis.keys("key*");
            for (String key : keys) {
                System.out.println(key);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            JedisPoolUtil.release(jedisPool, jedis);
        }
    }

    /** set Object */
    public String set(Object object, String key) {
        return jedis.set(key.getBytes(), SerializeUtil.serialize(object));
    }

    /** get Object */
    public Object get(String key) {
        byte[] value = jedis.get(key.getBytes());
        return SerializeUtil.unserialize(value);
    }

    /** delete a key **/
    public boolean del(String key) {
        return jedis.del(key.getBytes()) > 0;
    }

}
