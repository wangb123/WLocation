#WLocation文档
####使用方法：

```groovy
	implementation 'org.wbing:location:0.0.1' 
```

```
	<!--百度申请的AK-->
	<meta-data
		android:name="com.baidu.lbsapi.API_KEY"
		android:value="i8WVXNzGpAqFHYbUZVBfb8GFVPAuqj84" />
```

##功能说明
>基于百度地图封装的定位框架


###功能一：定位
```java
 		//初始化
        WLocation.init(this);
        //获取地理位置
        WLocation.get((error, location) -> {
            if (error != null) {
                return;
            }
            Log.e("location", location.toString());
        });
```

