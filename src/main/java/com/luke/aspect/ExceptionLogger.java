package com.luke.aspect;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.luke.admin.bean.Admin;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;



/**
 *	记录异常日志
 *一 AOP的基本概念
 *(2)JointPoint(连接点):程序执行过程中明确的点，一般是方法的调用
 *(4)Pointcut(切入点):就是带有通知的连接点，在程序中主要体现为书写切入点表达式
 *(3)Advice(通知):AOP在特定的切入点上执行的增强处理，
 *  有before,after,afterReturning,afterThrowing,around
  (1)Aspect(切面):通常是一个类，里面可以定义切入点和通知
  (5)AOP代理：AOP框架创建的对象，代理就是目标对象的加强。Spring中的AOP代理可以使JDK动态代理，也可以是CGLIB代理，前者基于接口，后者基于子类
 *
 *
 * 2.通知类型介绍
 *
 * (1)Before:在目标方法被调用之前做增强处理,@Before只需要指定切入点表达式即可
 * (2)AfterReturning:在目标方法正常完成后做增强,@AfterReturning除了指定切入点表达式后，
 *    还可以指定一个返回值形参名returning,代表目标方法的返回值
 * (3)AfterThrowing:主要用来处理程序中未处理的异常,@AfterThrowing除了指定切入点表达式后，
 *    还可以指定一个throwing的返回值形参名,可以通过该形参名来访问目标方法中所抛出的异常对象
 * (4)After:在目标方法完成之后做增强，无论目标方法时候成功完成。@After可以指定一个切入点表达式
 * (5)Around:环绕通知,在目标方法完成前后做增强处理,环绕通知是最重要的通知类型,像事务,日志等都是环绕通知,
 *    注意编程中核心是一个ProceedingJoinPoint
 */
@Component
@Aspect
public class ExceptionLogger {

    @Resource
    private HttpServletRequest request;

    @Before("within(com.luke.*.*.*)")
    public void before(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] obj = joinPoint.getArgs();
        System.out.println("测试@Before方法：类" +className+ "方法"+methodName+"将要执行");
//        if (obj.length>0){
//            System.out.println("方法参数----"+obj[0].toString());
//        }
    }

    @After("within(com.luke.*.*.*)")
    public void after(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        System.out.println("测试@After方法：类" +className+ "方法"+methodName+"已经最终执行");
    }

    @AfterThrowing(throwing="ex",pointcut="within(com.luke.*.*.*)")
    public  void afterThrowing(JoinPoint joinPoint,Throwable ex){
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        System.out.println("@AfterThrowing 方法执行测试：" +className+ methodName+"抛出异常");

        System.out.println("目标方法中抛出的异常:" + ex.toString());

    }


    @AfterReturning("within(com.luke.*.*.*)")
    public void  afterReturning(JoinPoint joinPoint){

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        System.out.println("@afterReturning 方法执行测试：" +className+ methodName+"正常执行结束");
    }

    //AOP的AfterThrowing处理虽然可以对目标方法的异常进行处理，但这种处理与直接使用catch捕捉不同，
    //catch捕捉意味着完全处理该异常，如果catch块中没有重新抛出新的异常，则该方法可能正常结束；
    //而AfterThrowing处理虽然处理了该异常，但它不能完全处理异常，该异常依然会传播到上一级调用者，即JVM。


    @Around("within(com.luke.*.*.*)")
    public Object log(ProceedingJoinPoint point) throws Exception {

        Object obj = null;
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("method start time: " + startTime);
            System.out.println("方法开始执行之前");
            obj = point.proceed();
            System.out.println("方法开始执行之后");
            long endTime = System.currentTimeMillis();
            System.out.println("method end time: " + endTime);
            System.out.println("历时: " + (endTime - startTime));
        } catch (Throwable e) {
            System.out.println("e.printStackTrace()----WRONG MESSAGE");
            e.printStackTrace();
            // 记录错误日志
            Logger logger = Logger.getLogger(this.getClass());
            BasicConfigurator.configure();
            Admin admin = (Admin) request.getSession().getAttribute("admin");
            if(admin != null) {
                String className = point.getTarget().getClass().getName();
                String method = point.getSignature().getName();
                String date = new SimpleDateFormat(
                        "yyyy-MM-dd hh:mm:ss").format(new Date());

                StringBuffer sb = new StringBuffer();
                sb.append("用户[").append(admin.getAdmin_code()).append("], ");
                sb.append("IP[").append(request.getRemoteHost()).append("], ");
                sb.append("在[").append(date).append("], 执行[");
                sb.append(className).append(".").append(method);
                sb.append("]时，发生如下异常：");
                logger.error(sb.toString());
            }

            StackTraceElement[] elems = e.getStackTrace();
            for(StackTraceElement elem : elems) {
                logger.error("\t" + elem.toString());
            }
            // 抛出异常
            throw new Exception(e);
        }
        return obj;
    }

}
