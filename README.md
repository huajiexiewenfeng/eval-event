## 对应的博客地址
* https://blog.csdn.net/xiewenfeng520/article/details/146535706

## 优化方向
* 线程池处理，优化kafka poll + 消费逻辑

## 使用方法
### 发送事件
```java
@RestController
@RequestMapping(value = {"/api/eval/event/v1"})
public class HelloController {

    @Autowired
    private EvalEventPublisher<MyEvent> evalEventPublisher;

    @Autowired
    private EvalEventPublisher<UserCreatedEvent> userEventPublisher;

    @GetMapping("/hello")
    public String hello() {
        MyEvent myEvent = new MyEvent();
        myEvent.setMessage("Hello, World!");
        // 发送事件
        evalEventPublisher.publishEvent(myEvent);
        return "ok";
    }

    @GetMapping("/user")
    public String user() {
        UserCreatedEvent userCreatedEvent = new UserCreatedEvent();
        User user = new User("xwf", 18);
        userCreatedEvent.setUser(user);
        // 发送事件
        userEventPublisher.publishEvent(userCreatedEvent);
        return "ok";
    }
}
```
### 接收事件
* 实现 EvalEventListener 方式
```java
@Component
public class MyEventEventListener implements EvalEventListener<MyEvent> {
    @Override
    public void onEvent(MyEvent event) {
        System.out.println("Received event: " + JSONObject.toJSONString(event));
    }
}
```

* @EvalEventListener 方式
```java
@Component
public class UserAnnotationEventListener  {
    @EvalEventListener
    public void onEvent(UserCreatedEvent event) {
        System.out.println("Received event: " + JSONObject.toJSONString(event));
    }
}
```