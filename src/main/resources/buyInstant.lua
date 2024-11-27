-- skuId
local inventoryKey = KEYS[1];
-- 2数据的key
-- 3.脚本业务
if (tonumber(redis.call('get',inventoryKey)) <=0) then
	--库存不足，返回1
	return 1
else
redis.call('incrby',inventoryKey,-1)
return 0
end


