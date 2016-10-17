package com.base.support.redis;

import redis.clients.jedis.ShardedJedis;

/**
 * Created by liangjun on 2016/7/1 0001.
 */
public interface RedisDataSource {
    abstract ShardedJedis getRedisClient();
    void returnResource(ShardedJedis shardedJedis);
    void returnResource(ShardedJedis shardedJedis, boolean broken);

}
