package com.cydeo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyticketingOrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyticketingOrmApplication.class, args);
    }
//dependendy injected I need it to convert entity to dto-6957
//I have added modelMapper dependency so I added it is not my class, external class so @Config
//annotation eklemeden bu annotation kapsayan bu class a ekliyoruz
    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }
}
