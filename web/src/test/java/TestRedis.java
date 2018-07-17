import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestRedis {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void set() {
        redisTemplate.opsForValue().set("test:set", "testValue1");
    }

}
