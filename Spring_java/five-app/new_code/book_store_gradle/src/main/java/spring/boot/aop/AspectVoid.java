package spring.boot.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AspectVoid {

    @Before("@annotation(CheckLog)")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Before execution of: {}", joinPoint.getSignature().getName());
    }

    @After("@annotation(CheckLog)")
    public void logAfter(JoinPoint joinPoint) {
        log.info("After execution of: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "@annotation(CheckLog)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("After returning from: {} with result: {}", joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(pointcut = "@annotation(CheckLog)", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        log.info("After throwing from: {} with exception: ", joinPoint.getSignature().getName(), exception);
    }

    @Around("@annotation(CheckLog)")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Around method: {} is called", proceedingJoinPoint.getSignature());

        Object result = proceedingJoinPoint.proceed();

        log.info("Around method: {} is finished", proceedingJoinPoint.getSignature());

        return result;
    }
}
