package com.atm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PostConstructImpl {

    @Autowired
    private Environment environment;

    @Autowired
    AccountServiceImpl accountService;

    @PostConstruct
    public void runAfterObjectCreated() {
        accountService.getAccountFromFile(environment.getProperty("spring.datasource.file"));
    }
}
