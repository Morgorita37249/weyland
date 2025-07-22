package com.weyland.bishop.controller;
import com.weyland.bishop.audit.AuditMode;
import com.weyland.bishop.audit.WeylandWatchingYou;
import com.weyland.bishop.model.Command;
import com.weyland.bishop.model.Priority;
import com.weyland.bishop.service.CommandQueueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commands")
@RequiredArgsConstructor
public class CommandController {
    private final CommandQueueService queueService;

    @PostMapping
    @WeylandWatchingYou(mode = AuditMode.KAFKA)
    public ResponseEntity<String> addCommand(@Valid @RequestBody Command command){
        if (command.getPriority() == Priority.CRITICAL){
            return ResponseEntity.ok("CRITICAL: " + command.getDescription());
        } else{
            queueService.addCommand(command);
            return ResponseEntity.ok("COMMON: " + command.getDescription());
        }
    }
}
