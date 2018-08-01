package com.luke.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 连接点：
 * 切点：
 * 增强：@Before  @After()  @AfterReturning()  @AfterThrowing()  @Around()
 *
 *
 * 切面：
 */
@Component
//@Aspect
public class MethodLog {

    @Before("within(com.luke.*.*.*)")
    public  void  beforeMethod(JoinPoint joinPoint){
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String  methodName = joinPoint.getSignature().getName();
        System.out.println("类"+className+"方法"+methodName+"将要执行");
    }


}
