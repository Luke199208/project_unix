package com.luke.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

//@Aspect
@Component
public class ExceptionLog {
    @Before("within(com.luke.*.*.*)")
    public void before(JoinPoint joinPoint){
       String className = joinPoint.getSignature().getDeclaringTypeName();
       String  methodName = joinPoint.getSignature().getName();
       System.out.println("--类："+className+",方法"+methodName+"将要执行");
    }

    @Around("within(com.luke.*.*.*)")
    public void  around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String className = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String  methodName = proceedingJoinPoint.getSignature().getName();
        System.out.println("--类："+className+",方法"+methodName+"将要执行");
        proceedingJoinPoint.proceed();
        System.out.println("--类："+className+",方法"+methodName+"执行结束");
    }
}
