package com.gis.geoctap;

import com.gis.geoctap.entity.User;
import com.gis.geoctap.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@SpringBootApplication
public class GeoctapApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeoctapApplication.class, args);
    }

}
