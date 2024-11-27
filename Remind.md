# 10月2号
传送文件的方法：

后端：

```java
    @PostMapping("changerHeader")
    public Result<String> changeHeader(MultipartFile file,String userId) throws Exception {
        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
        String url = AliOssUtil.upLoadFile(fileName,file.getInputStream());
        Integer id = Integer.parseInt(userId);
        userService.changeUserHead(url,id);
        System.out.println(id);
        return Result.success(url);
    }
```

前端：

```javascript
const response = await request.post('http://localhost:8080/user/changerHeader', formData, {
    headers: {
        'Content-Type': 'multipart/form-data'
    },
});
```

# 10月3号
## 商品页面思路
分为两个端 一个商家段 还有一个是用户端

# 10月13号
## 代码记录
获取当前时间时间：
```java
LocalDateTime currentTime = LocalDateTime.now();
```
雪花算法生成的id
```java
Long saleAttributeId = snowflake.nextId();
```

# 10月20号
## redis相关学习
查的操作：
1.命中缓存直接返回 
2.未命中缓存则查数据库，并写入缓存，设定删除时间

写的操作：
1.先写数据库，然后再删除缓存。
2.删除重试机制保持结果一致性。


