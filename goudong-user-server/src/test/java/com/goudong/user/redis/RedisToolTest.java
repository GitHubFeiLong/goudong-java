package com.goudong.user.redis;

import com.google.common.collect.Lists;
import com.goudong.commons.frame.redis.AbstractRedisKey;
import com.goudong.commons.frame.redis.GenerateRedisKeyUtil;
import com.goudong.commons.frame.redis.RedisTool;
import com.goudong.commons.frame.redis.SimpleRedisKey;
import lombok.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
class RedisToolTest {

}