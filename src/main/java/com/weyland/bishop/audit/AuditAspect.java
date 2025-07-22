package com.weyland.bishop.audit;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final KafkaTemplate<String,String> kafkaTemplate;
    @Around("@annotation(weylandAudit)")
    public Object auditMethod(ProceedingJoinPoint joinPoint, WeylandWatchingYou weylandAudit) throws Throwable{
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        Object result =  joinPoint.proceed();

        String message = String.format(
                "Method: %s | Args: %s | Result: %s",
                methodName, Arrays.toString(args),result);

        if (weylandAudit.mode()== AuditMode.KAFKA){
            kafkaTemplate.send("weyland-audit-topic",message);
        }else{
            System.out.println("[AUDIT]" + message);
        }
        return result;
    }
}
