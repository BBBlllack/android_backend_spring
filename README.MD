# 这个项目需要配合我的另外一个仓库android_backend_flask才可以使用
# 前端在android_project中开源

**GET**
```http request
/category/get

param:
  type: all | parent | children => 获取 全部 | 父级 | 子分类
  pid: 父分类id(当type=children时需要传)
```
**eg:**
```http request
GET /category/get?type=all 获取全部分类
```
```json
{
  "code": 200,
  "data": [
    {
      "categoryId": 2,
      "canCreateThread": 0,
      "description": "计算机视觉|自然语言处理|人工智能|机器学习|机器人|语音",
      "icon": "",
      "name": "人工智能",
      "parentid": 0,
      "pid": 2,
      "property": 0,
      "sort": 2,
      "threadCount": 5553
    },
    {"...": "..."}
  ],
  "msg": "...",
  "others": "other data..."
}
```

```http request
GET /category/get?type=children&pid=2 获取父级分类
```
```json
{
  "code": 200,
  "data": [
    {
      "categoryId": 19,
      "canCreateThread": 0,
      "description": "",
      "icon": "",
      "name": "计算机视觉",
      "parentid": 2,
      "pid": 19,
      "property": 0,
      "sort": 1,
      "threadCount": 837
    },
    {"...":"..."}
  ],
  "msg": "返回2对应的所有子级分类",
  "others": null
}
```

