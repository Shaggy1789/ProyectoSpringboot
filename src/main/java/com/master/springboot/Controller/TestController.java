package com.master.springboot.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/testdb")
    public Map<String,String> testConnection(){
        Map<String,String> response = new HashMap<>();

        try(Connection conn = dataSource.getConnection()){
            response.put("status","SUCCESS");
            response.put("database",conn.getMetaData().getDatabaseProductName());
            response.put("version",conn.getMetaData().getDatabaseProductVersion());
            response.put("url",conn.getMetaData().getURL());
            response.put("user",conn.getMetaData().getUserName());
        } catch (SQLException e) {
            response.put("status","Error");
            response.put("Message",e.getMessage());
        }
        return response;
    }

}
