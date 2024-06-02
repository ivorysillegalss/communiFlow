package org.chenzc.communi;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.chenzc.communi.dao")
public class CommuniApplication {


    public static void main(String[] args) {
        SpringApplication.run(CommuniApplication.class, args);
    }


}
