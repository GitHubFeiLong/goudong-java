local result = {};

result[1] = KEYS[1];
result[2] = KEYS[2];
-- result[3] = 3;
-- result[4] = 4;
-- result[2] = redis.call("get", KEYS[1]);
-- result[3] = KEYS[1];
-- result[4] = redis.call("get", KEYS[2]);

return result;

-- return 123;