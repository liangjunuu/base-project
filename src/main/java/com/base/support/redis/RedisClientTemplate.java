package com.base.support.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liangjun on 2016/7/1 0001.
 */
@Repository("redisClientTemplate")
public class RedisClientTemplate {
    private static final Logger logger = LoggerFactory.getLogger(RedisClientTemplate.class);
    @Autowired
    private RedisDataSource redisDataSource;

    public void disconnect() {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        shardedJedis.disconnect();
    }

    /**
     * 向redis存入key和value,并释放连接资源
     * 如果key已经存在 则覆盖
     * @param key
     * @param value
     * @return 成功 返回OK 失败返回 null
     */
    public String set(String key, String value) {
        String result = null;

        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.set(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 通过key获取储存在redis中的value
     * 并释放连接
     * @param key
     * @return 成功返回value 失败返回null
     */
    public String get(String key) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }

        boolean broken = false;
        try {
            result = shardedJedis.get(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 判断key是否存在
     * @param key
     * @return true OR false
     */
    public Boolean exists(String key) {
        Boolean result = false;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.exists(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 通过key判断值得类型
     * @param key
     *
     * @return
     */
    public String type(String key) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.type(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除
     * @param key
     * @param seconds
     * @return 设置成功返回 1 当 key 不存在或者不能为 key 设置生存时间时返回 0
     */
    public Long expire(String key, int seconds) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.expire(key, seconds);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置生存时间
     * 不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix timestamp)
     * @param key
     * @param unixTime
     * @return 如果生存时间设置成功，返回 1 当 key 不存在或没办法设置生存时间，返回 0 
     */
    public Long expireAt(String key, long unixTime) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.expireAt(key, unixTime);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)
     * @param key
     * @return 当 key 不存在时，返回 -2 
     * 当 key 存在但没有设置剩余生存时间时，返回 -1 
     * 否则，以秒为单位，返回 key 的剩余生存时间
     */
    public Long ttl(String key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.ttl(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)
     * 位的设置或清除取决于 value 参数，可以是 0 也可以是 1
     * 当 key 不存在时，自动生成一个新的字符串值
     * offset 参数必须大于或等于 0 ，小于 2^32 (bit 映射被限制在 512 MB 之内)
     * @param key
     * @param offset
     * @param value
     * @return 指定偏移量原来储存的位
     */
    public boolean setbit(String key, long offset, boolean value) {

        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        boolean result = false;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setbit(key, offset, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)
     * 当 offset 比字符串值的长度大，或者 key 不存在时，返回 0
     * @param key
     * @param offset
     * @return 字符串值指定偏移量上的位(bit)
     */
    public boolean getbit(String key, long offset) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        boolean result = false;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;

        try {
            result = shardedJedis.getbit(key, offset);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 用 value 参数覆写(overwrite)给定 key 所储存的字符串值，从偏移量 offset 开始
     * 不存在的 key 当作空白字符串处理
     * SETRANGE 命令会确保字符串足够长以便将 value 设置在指定的偏移量上，如果给定 key 原来储存的字符串长度比偏移量小(比如字符串只有 5 个字符长，但你设置的 offset 是 10 )，
     * 那么原字符和偏移量之间的空白将用零字节(zerobytes, "\x00" )来填充
     * 注意你能使用的最大偏移量是 2^29-1(536870911) ，因为 Redis 字符串的大小被限制在 512 兆(megabytes)以内。如果你需要使用比这更大的空间，你可以使用多个 key
     * @param key
     * @param offset
     * @param value
     * @return 被 SETRANGE 修改之后，字符串的长度
     */
    public long setrange(String key, long offset, String value) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        long result = 0;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setrange(key, offset, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 返回 key 中字符串值的子字符串，字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内)
     * 负数偏移量表示从字符串最后开始计数， -1 表示最后一个字符， -2 表示倒数第二个，以此类推
     * GETRANGE 通过保证子字符串的值域(range)不超过实际字符串的值域来处理超出范围的值域请求
     * @param key
     * @param startOffset
     * @param endOffset
     * @return 截取得出的子字符串
     */
    public String getrange(String key, long startOffset, long endOffset) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        String result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getrange(key, startOffset, endOffset);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
     * 当 key 存在但不是字符串类型时，返回一个错误
     * @param key
     * @param value
     * @return 返回给定 key 的旧值 当 key 没有旧值时，也即是， key 不存在时，返回 nil
     */
    public String getSet(String key, String value) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getSet(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在
     * 若给定的 key 已经存在，则 SETNX 不做任何动作
     * SETNX 是『SET if Not eXists』(如果不存在，则 SET)的简写
     * @param key
     * @param value
     * @return 设置成功，返回 1 设置失败，返回 0
     */
    public Long setnx(String key, String value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setnx(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)
     * 如果 key 已经存在， SETEX 命令将覆写旧值
     * 不同之处是， SETEX 是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成，该命令在 Redis 用作缓存时，非常实用
     * @param key
     * @param seconds
     * @param value
     * @return 设置成功时返回 OK  当 seconds 参数不合法时，返回一个错误
     */
    public String setex(String key, int seconds, String value) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setex(key, seconds, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param integer
     * @return
     */
    public Long decrBy(String key, long integer) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.decrBy(key, integer);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 将 key 所储存的值减去减量 decrement
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     * 关于更多递增(increment) / 递减(decrement)操作的更多信息，请参见 INCR 命令
     * @param key
     * @return 减去 decrement 之后， key 的值
     */
    public Long decr(String key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.decr(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 将 key 所储存的值加上增量 increment
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     * 关于递增(increment) / 递减(decrement)操作的更多信息，参见 INCR 命令
     * @param key
     * @param integer
     * @return 加上 increment 之后， key 的值
     */
    public Long incrBy(String key, long integer) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.incrBy(key, integer);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 将 key 中储存的数字值增一
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     * @param key
     * @return 执行 INCR 命令之后 key 的值
     */
    public Long incr(String key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.incr(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾
     * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样
     * @param key
     * @param value
     * @return 追加 value 之后， key 中字符串的长度
     */
    public Long append(String key, String value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.append(key, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 截取字符串
     * @param key
     * @param start
     * @param end
     * @return 返回名称为key的string的value的子串
     */
    public String substr(String key, int start, int end) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.substr(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 将哈希表 key 中的域 field 的值设为 value
     * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作
     * 如果域 field 已经存在于哈希表中，旧值将被覆盖
     * @param key
     * @param field
     * @param value
     * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0
     */
    public Long hset(String key, String field, String value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hset(key, field, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 返回哈希表 key 中给定域 field 的值
     * @param key
     * @param field
     * @return 给定域的值 当给定域不存在或是给定 key 不存在时，返回 nil
     */
    public String hget(String key, String field) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hget(key, field);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在
     * 若域 field 已经存在，该操作无效
     * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令
     * @param key
     * @param field
     * @param value
     * @return 设置成功，返回 1
     * 如果给定域已经存在且没有操作被执行，返回 0
     */
    public Long hsetnx(String key, String field, String value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hsetnx(key, field, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中
     * 此命令会覆盖哈希表中已存在的域
     * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作
     * @param key
     * @param hash
     * @return 如果命令执行成功，返回 OK
     * 当 key 不是哈希表(hash)类型时，返回一个错误
     */
    public String hmset(String key, Map<String, String> hash) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hmset(key, hash);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 返回哈希表 key 中，一个或多个给定域的值
     * 如果给定的域不存在于哈希表，那么返回一个 nil 值
     * 因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表
     * @param key
     * @param fields
     * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样
     */
    public List<String> hmget(String key, String... fields) {
        List<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hmget(key, fields);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 为哈希表 key 中的域 field 的值加上增量 increment
     * 增量也可以为负数，相当于对给定域进行减法操作
     * 如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令
     * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0
     * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误
     * 本操作的值被限制在 64 位(bit)有符号数字表示之内
     * @param key
     * @param field
     * @param value
     * @return 执行 HINCRBY 命令之后，哈希表 key 中域 field 的值
     */
    public Long hincrBy(String key, String field, long value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hincrBy(key, field, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 查看哈希表 key 中，给定域 field 是否存在
     * @param key
     * @param field
     * @return 如果哈希表含有给定域，返回 1 如果哈希表不含有给定域，或 key 不存在，返回 0
     */
    public Boolean hexists(String key, String field) {
        Boolean result = false;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hexists(key, field);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 删除给定的一个或多个 key
     * 不存在的 key 会被忽略
     * @param key
     * @return 被删除 key 的数量
     */
    public Long del(String key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.del(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略
     * @param key
     * @param field
     * @return 被成功移除的域的数量，不包括被忽略的域
     */
    public Long hdel(String key, String field) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hdel(key, field);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 返回哈希表 key 中域的数量
     * @param key
     * @return 哈希表中域的数量 当 key 不存在时，返回 0
     */
    public Long hlen(String key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hlen(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 返回哈希表 key 中的所有域
     * 一个包含哈希表中所有域的表
     * 当 key 不存在时，返回一个空表
     * @param key
     * @return
     */
    public Set<String> hkeys(String key) {
        Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hkeys(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 返回哈希表 key 中所有域的值
     * @param key
     * @return 一个包含哈希表中所有值的表 当 key 不存在时，返回一个空表
     */
    public List<String> hvals(String key) {
        List<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hvals(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 返回哈希表 key 中，所有的域和值
     * 在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍
     * @param key
     * @return 以列表形式返回哈希表的域和域的值 若 key 不存在，返回空列表
     */
    public Map<String, String> hgetAll(String key) {
        Map<String, String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hgetAll(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    // ================list ====== l表示list或 left, r表示right====================

    /**
     * 将一个或多个值 value 插入到列表 key 的表尾(最右边)
     * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表尾：比如对一个空列表 mylist 执行 RPUSH mylist a b c ，得出的结果列表为 a b c ，等同于执行命令 RPUSH mylist a 、 RPUSH mylist b 、 RPUSH mylist c
     * 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作
     * 当 key 存在但不是列表类型时，返回一个错误
     * @param key
     * @param string
     * @return 执行 RPUSH 操作后，表的长度
     */
    public Long rpush(String key, String string) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.rpush(key, string);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 将一个或多个值 value 插入到列表 key 的表头
     * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令
     * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作
     * 当 key 存在但不是列表类型时，返回一个错误
     * @param key
     * @param string
     * @return 执行 LPUSH 命令后，列表的长度
     */
    public Long lpush(String key, String string) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lpush(key, string);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 返回列表 key 的长度
     * 如果 key 不存在，则 key 被解释为一个空列表，返回 0
     * 如果 key 不是列表类型，返回一个错误
     * @param key
     * @return
     */
    public Long llen(String key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.llen(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
     * 注意LRANGE命令和编程语言区间函数的区别
     * 假如你有一个包含一百个元素的列表，对该列表执行 LRANGE list 0 10 ，结果是一个包含11个元素的列表，
     * 这表明 stop 下标也在 LRANGE 命令的取值范围之内(闭区间)，这和某些语言的区间函数可能不一致，比如Ruby的 Range.new 、 Array#slice 和Python的 range() 函数
     * 超出范围的下标
     * 超出范围的下标值不会引起错误
     * 如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，那么 LRANGE 返回一个空列表
     * 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end
     * @param key
     * @param start
     * @param end
     * @return 一个列表，包含指定区间内的元素
     */
    public List<String> lrange(String key, long start, long end) {
        List<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lrange(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除
     * 举个例子，执行命令 LTRIM list 0 2 ，表示只保留列表 list 的前三个元素，其余元素全部删除
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
     * 当 key 不是列表类型时，返回一个错误
     * 超出范围的下标
     * 超出范围的下标值不会引起错误
     * 如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，或者 start > stop ， LTRIM 返回一个空列表(因为 LTRIM 已经将整个列表清空)
     * 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end
     * @param key
     * @param start
     * @param end
     * @return 命令执行成功时，返回 ok
     */
    public String ltrim(String key, long start, long end) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.ltrim(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 返回列表 key 中，下标为 index 的元素
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
     * 如果 key 不是列表类型，返回一个错误
     * @param key
     * @param index
     * @return
     */
    public String lindex(String key, long index) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lindex(key, index);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 将列表 key 下标为 index 的元素的值设置为 value
     * 当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET 时，返回一个错误
     * 关于列表下标的更多信息，请参考 LINDEX 命令
     * @param key
     * @param index
     * @param value
     * @return 操作成功返回 ok ，否则返回错误信息
     */
    public String lset(String key, long index, String value) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lset(key, index, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 根据参数 count 的值，移除列表中与参数 value 相等的元素
     * count 的值可以是以下几种：
     * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count
     * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值
     * count = 0 : 移除表中所有与 value 相等的值
     * @param key
     * @param count
     * @param value
     * @return 被移除元素的数量
     * 因为不存在的 key 被视作空表(empty list)，所以当 key 不存在时， LREM 命令总是返回 0
     */
    public Long lrem(String key, long count, String value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lrem(key, count, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 移除并返回列表 key 的头元素
     * @param key
     * @return 列表的头元素
     * 当 key 不存在时，返回 nil
     */
    public String lpop(String key) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lpop(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 移除并返回列表 key 的尾元素
     * @param key
     * @return 列表的尾元素
     * 当 key 不存在时，返回 nil
     */
    public String rpop(String key) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.rpop(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    //return 1 add a not exist value ,
    //return 0 add a exist value

    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略
     * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合
     * 当 key 不是集合类型时，返回一个错误
     * @param key
     * @param member 可以是一个String 也可以是一个String数组
     * @return 被添加到集合中的新元素的数量，不包括被忽略的元素
     */
    public Long sadd(String key, String...member) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.sadd(key, member);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 返回集合 key 中的所有成员
     * 不存在的 key 被视为空集合
     * @param key
     * @return 集合中的所有成员
     */
    public Set<String> smembers(String key) {
        Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.smembers(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略
     * 当 key 不是集合类型，返回一个错误
     * @param key
     * @param members
     * @return 被成功移除的元素的数量，不包括被忽略的元素
     */
    public Long srem(String key, String... members) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();

        Long result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.srem(key, members);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 移除并返回集合中的一个随机元素
     * @param key
     * @return 被移除的随机元素
     * 当 key 不存在或 key 是空集时，返回 nil
     */
    public String spop(String key) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        String result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.spop(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 返回集合 key 的基数(集合中元素的数量)
     * @param key
     * @return 集合的基数 当 key 不存在时，返回 0
     */
    public Long scard(String key) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        Long result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.scard(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 判断 member 元素是否集合 key 的成员
     * @param key
     * @param member
     * @return 如果 member 元素是集合的成员，返回 1
     * 如果 member 元素不是集合的成员，或 key 不存在，返回 0
     */
    public Boolean sismember(String key, String member) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        Boolean result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.sismember(key, member);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 如果命令执行时，只提供了 key 参数，那么返回集合中的一个随机元素
     * @param key
     * @return 只提供 key 参数时，返回一个元素；如果集合为空，返回 nil
     * 如果提供了 count 参数，那么返回一个数组；如果集合为空，返回空数组
     */
    public String srandmember(String key) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        String result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.srandmember(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中
     * 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，并通过重新插入这个 member 元素，来保证该 member 在正确的位置上
     * score 值可以是整数值或双精度浮点数
     * 如果 key 不存在，则创建一个空的有序集并执行 ZADD 操作
     * 当 key 存在但不是有序集类型时，返回一个错误
     * 对有序集的更多介绍请参见 sorted set
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Long zadd(String key, double score, String member) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zadd(key, score, member);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrange(String key, int start, int end) {
        Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrange(key, start, end);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param members
     * @return
     */
    public Long zrem(String key, String... members) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrem(key, members);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Double zincrby(String key, double score, String member) {
        Double result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zincrby(key, score, member);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrank(String key, String member) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrank(key, member);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrevrank(String key, String member) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrank(key, member);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrevrange(String key, int start, int end) {
        Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrange(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Tuple> zrangeWithScores(String key, int start, int end) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeWithScores(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeWithScores(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @return
     */
    public Long zcard(String key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zcard(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param member
     * @return
     */
    public Double zscore(String key, String member) {
        Double result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zscore(key, member);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @return
     */
    public List<String> sort(String key) {
        List<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.sort(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param sortingParameters
     * @return
     */
    public List<String> sort(String key, SortingParams sortingParameters) {
        List<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.sort(key, sortingParameters);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zcount(String key, double min, double max) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zcount(key, min, max);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<String> zrangeByScore(String key, double min, double max) {
        Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeByScore(key, min, max);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeByScore(key, max, min);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeByScore(key, min, max, offset, count);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param max
     * @param min
     * @param offset
     * @param count
     * @return
     */
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeByScoreWithScores(key, min, max);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeByScoreWithScores(key, max, min);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param max
     * @param min
     * @param offset
     * @param count
     * @return
     */
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeByScoreWithScores(key, max, min, offset, count);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByRank(String key, int start, int end) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zremrangeByRank(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByScore(String key, double start, double end) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zremrangeByScore(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param where
     * @param pivot
     * @param value
     * @return
     */
    public Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.linsert(key, where, pivot, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public String set(byte[] key, byte[] value) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.set(key, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.get(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @return
     */
    public Boolean exists(byte[] key) {
        Boolean result = false;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.exists(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @return
     */
    public String type(byte[] key) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.type(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param seconds
     * @return
     */
    public Long expire(byte[] key, int seconds) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.expire(key, seconds);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param unixTime
     * @return
     */
    public Long expireAt(byte[] key, long unixTime) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.expireAt(key, unixTime);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @return
     */
    public Long ttl(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.ttl(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public byte[] getSet(byte[] key, byte[] value) {
        byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.getSet(key, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public Long setnx(byte[] key, byte[] value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.setnx(key, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String setex(byte[] key, int seconds, byte[] value) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.setex(key, seconds, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long decrBy(byte[] key, long integer) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.decrBy(key, integer);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long decr(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.decr(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long incrBy(byte[] key, long integer) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.incrBy(key, integer);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long incr(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.incr(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long append(byte[] key, byte[] value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.append(key, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public byte[] substr(byte[] key, int start, int end) {
        byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.substr(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 将哈希表 key 中的域 field 的值设为 value
     * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作
     * 如果域 field 已经存在于哈希表中，旧值将被覆盖
     * @param key
     * @param field
     * @param value
     * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1
     * 如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0
     */
    public Long hset(byte[] key, byte[] field, byte[] value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hset(key, field, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public byte[] hget(byte[] key, byte[] field) {
        byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hget(key, field);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hsetnx(key, field, value);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hmset(key, hash);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        List<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hmget(key, fields);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long hincrBy(byte[] key, byte[] field, long value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hincrBy(key, field, value);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Boolean hexists(byte[] key, byte[] field) {
        Boolean result = false;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hexists(key, field);
        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long hdel(byte[] key, byte[]... fields) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hdel(key, fields);
        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long hlen(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hlen(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> hkeys(byte[] key) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hkeys(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Collection<byte[]> hvals(byte[] key) {
        Collection<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hvals(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Map<byte[], byte[]> hgetAll(byte[] key) {
        Map<byte[], byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hgetAll(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long rpush(byte[] key, byte[]... strings) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.rpush(key, strings);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long lpush(byte[] key, byte[]... strings) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.lpush(key, strings);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long llen(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.llen(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<byte[]> lrange(byte[] key, int start, int end) {
        List<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.lrange(key, start, end);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String ltrim(byte[] key, int start, int end) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.ltrim(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public byte[] lindex(byte[] key, int index) {
        byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.lindex(key, index);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String lset(byte[] key, int index, byte[] value) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.lset(key, index, value);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long lrem(byte[] key, int count, byte[] value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.lrem(key, count, value);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public byte[] lpop(byte[] key) {
        byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.lpop(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public byte[] rpop(byte[] key) {
        byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.rpop(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long sadd(byte[] key, byte[]... members) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.sadd(key, members);
        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> smembers(byte[] key) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.smembers(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long srem(byte[] key, byte[]... members) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.srem(key, members);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public byte[] spop(byte[] key) {
        byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.spop(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long scard(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.scard(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Boolean sismember(byte[] key, byte[] member) {
        Boolean result = false;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.sismember(key, member);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public byte[] srandmember(byte[] key) {
        byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.srandmember(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zadd(byte[] key, double score, byte[] member) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zadd(key, score, member);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> zrange(byte[] key, int start, int end) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrange(key, start, end);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zrem(byte[] key, byte[]... members) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrem(key, members);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Double zincrby(byte[] key, double score, byte[] member) {
        Double result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zincrby(key, score, member);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zrank(byte[] key, byte[] member) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrank(key, member);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zrevrank(byte[] key, byte[] member) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrank(key, member);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> zrevrange(byte[] key, int start, int end) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrange(key, start, end);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeWithScores(key, start, end);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeWithScores(key, start, end);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zcard(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zcard(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Double zscore(byte[] key, byte[] member) {
        Double result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zscore(key, member);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<byte[]> sort(byte[] key) {
        List<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.sort(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
        List<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.sort(key, sortingParameters);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zcount(byte[] key, double min, double max) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zcount(key, min, max);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeByScore(key, min, max);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeByScore(key, min, max, offset, count);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeByScoreWithScores(key, min, max);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeByScore(key, max, min);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param max
     * @param min
     * @param offset
     * @param count
     * @return
     */
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeByScoreWithScores(key, max, min);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param max
     * @param min
     * @param offset
     * @param count
     * @return
     */
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeByScoreWithScores(key, max, min, offset, count);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByRank(byte[] key, int start, int end) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zremrangeByRank(key, start, end);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByScore(byte[] key, double start, double end) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zremrangeByScore(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param where
     * @param pivot
     * @param value
     * @return
     */
    public Long linsert(byte[] key, BinaryClient.LIST_POSITION where, byte[] pivot, byte[] value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.linsert(key, where, pivot, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @return
     */
    public Jedis getShard(byte[] key) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        Jedis result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getShard(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @return
     */
    public Jedis getShard(String key) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        Jedis result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getShard(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @return
     */
    public JedisShardInfo getShardInfo(byte[] key) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        JedisShardInfo result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getShardInfo(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @return
     */
    public JedisShardInfo getShardInfo(String key) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        JedisShardInfo result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getShardInfo(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param key
     * @return
     */
    public String getKeyTag(String key) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        String result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getKeyTag(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @return
     */
    public Collection<JedisShardInfo> getAllShardInfo() {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        Collection<JedisShardInfo> result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getAllShardInfo();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @return
     */
    public Collection<Jedis> getAllShards() {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        Collection<Jedis> result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getAllShards();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }


}
