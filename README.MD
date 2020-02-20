# Các bước tích hợp sentry
## 1. Chọn logging framework 
Sentry hỗ trợ một số logging framework như java.util.logging/Log4j 2.x/Logback/...
Xem chi tiết tại đây: https://docs.sentry.io/clients/java/integrations/#logback 

Project này đang sử dụng logback, các bước config bao gồm:
- Thêm sentry-logback dependency vào build.gradle
    ```shell script
    compile 'io.sentry:sentry-logback:1.7.30'
    ``` 
- Tạo file logback.xml trong thư mục resources
    ```xml
    <configuration>
        <appender name="Console" class="ch.qos.logback.core.ConsoleAppender">
            <encoder>
                <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
            </encoder>
        </appender>
    
        <appender name="Sentry" class="io.sentry.logback.SentryAppender">
            <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
                <level>WARN</level>
            </filter>
            <encoder>
                <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
            </encoder>
        </appender>
    
        <root level="INFO">
            <appender-ref ref="Console"/>
            <appender-ref ref="Sentry"/>
        </root>
    </configuration>
    ```

## 2. Config sentry
Có 3 cách để config các thuộc tính của Sentry:
#### Cách 1: Qua file `sentry.properties` 
```
stacktrace.app.packages=io.x10.projectexample
dsn=https://06bb609eb55b49aebbce0497613b5082:6588b52febb1418f9d28b4f07090ff9b@sentry.eway.vn/77
environment=dev
```

#### Cách 2: Qua Java Environment Variables
Truyền giá trị các biến mà Sentry hỗ trợ với prefix sentry.
```shell script
java -Dsentry.environment=production -Drelease=1.0 -jar build/libs/project-example-0.0.1-SNAPSHOT.jar
```

#### Cách 3: Qua biến môi trường
Biến môi trường mà Sentry có thể parse được chính là tên biến viết hoa, với các dấu `.` thay bằng `_`. Ví dụ `sentry.sample.rate` sẽ là `SENTRY_SAMPLE_RATE`
```shell script
SENTRY_DSN=https://06bb609eb55b49aebbce0497613b5082:6588b52febb1418f9d28b4f07090ff9b@sentry.eway.vn/77 \ 
java -jar build/libs/project-example-0.0.1-SNAPSHOT.jar 
```

#Các biến thường sử dụng
- `dns`: unique URI cho mỗi project - lấy giá trị này từ mục cài đặt trong sentry project
- `stacktrace.app.packages`: Biến này giúp sentry highlight package của project này trên giao diện
- `release`: định danh version hiện tại nhằm mục đích filter hoặc report lỗi theo bản release
- `environment`: định danh môi trường, có thể dùng để filter alert - chỉ email khi environment = production
- `server_name`: tên server - có thể dùng để định danh node khi chạy nhiều container giúp tìm ra node gây lỗi (VD: server_name=adflex-pro-306)
- `tags`: các cặp key-value tuỳ chỉnh, ví dụ như commit hash. 
Truyền vào nhiều tag với pattern: `tags=tag1:value1,tag2:value2`

Xem thêm tại đây: https://docs.sentry.io/clients/java/config/#configuration-via-the-dsn

# Chạy thử
#### Tạo project sentry tại sentry.io
#### Build project
```shell script
gradle build
```
#### Chạy project
```shell script
java -Dsentry.dns=${YOUR_DNS} sentry.enviroment=production -jar build/libs/project-example-0.0.1-SNAPSHOT.jar
```

Hoặc sử dụng System Environment Variable
```shell script
SENTRY_DNS=${YOUR_DNS} SENTRY_ENVIRONMENT=production java -jar build/libs/project-example-0.0.1-SNAPSHOT.jar
```