# HappyAndroid
Happy Android, Happy Learning.

# 目标

本仓库主要包含以下几个模块： `基础` 、 `UI` 、 `架构` 、 `新特性` 及 ` 其他` 五个模块。整个项目以**组件化**方式实现。各模块可以独立运行，也可以集成运行。

其他的话，慢慢添加。


# 基础

## 1. 跳转

### 1.1 常规操作

一般情况，我们使用以下方式进行activity跳转：

```java
Intent intent = new Intent(context, MainActivity.class);
startActivity(intent);
```
这种方式叫显式intent跳转。还有一种叫隐式跳转：
首先在AndroidManifest.xml中配置 action：

```xml
<activity android:name=".MainBasicActivity">
    <intent-filter>
        <action android:name="route.basic.main" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```

执行跳转：

```java
Intent intent = new Intent("route.basic.main");
startActivity(intent);
```

### 1.2 进阶

将跳转操作放到 router-api 中，统一管理。

思路：以 path 为 key，与 class 为 value，将所有 activity，统一放到 map中，然后根据 key 取出目标 activity ，然后去执行跳转。

#### 1.2.1 思考

如果项目中有非常多的 activity，那是不是就得在 init() 方法中不断的注册？显然这样很啰嗦，一旦忘记添加，就无法跳转。为了解决这个问题，我们可以做一个规范，让每个 module 在自己内部注册，这样，我们就不用关心是否添加配置了。

#### 1.2.2 注解处理器

如果 module 很多，还是需要写模版代码。我们深入思考下，有没有更优雅的实现方案？

想必，很多搞 Android 开发的，都用过 ButterKnife，这个框架通过编译期生成代码的方式，提高了运行速度，得到了很多开发者的支持，我们也可以借鉴这种思路来打造我们的路由框架。

# UI

# 架构

# 新特性

# Other