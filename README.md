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
WLocation.Callback因为会使用引用存储，所以可能会导致无法回调的问题，所以应该将回调对象在方法外强引用，不用使用匿名对象

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

