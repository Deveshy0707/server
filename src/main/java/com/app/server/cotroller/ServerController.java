package com.app.server.cotroller;

import com.app.server.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
@RestController
public class ServerController {

    @Autowired
    private ServerService service;

    @PostMapping("/server/evaluate")
    public int evalauateProject(@RequestBody byte[] fileContent){
        return service.evaluate(fileContent);
    }

}
