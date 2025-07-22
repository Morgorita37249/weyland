package com.weyland.bishop.controller;
import com.weyland.bishop.audit.AuditMode;
import com.weyland.bishop.audit.WeylandWatchingYou;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test-aop")
    @WeylandWatchingYou(mode = AuditMode.CONSOLE)
    public String testAop(){
        return "AOP test is successful";
    }
}
