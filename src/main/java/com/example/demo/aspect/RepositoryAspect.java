package com.example.demo.aspect;

import com.example.demo.exception.ObjectNotFoundException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RepositoryAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Object avoidReturnNull(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Object ret = pjp.proceed(args);
        if (ret == null) throw new ObjectNotFoundException();
        return ret;
    }

    @Around("execution(* com.example.demo.repository.UserRepository.findByUsername(..))")
    public Object aroundRepository(ProceedingJoinPoint pjp) throws Throwable {
        return avoidReturnNull(pjp);
    }

    @Around("execution(* com.example.demo.repository.*.findOne(..))")
    public Object aroundOtherRepository(ProceedingJoinPoint pjp) throws Throwable {
        return avoidReturnNull(pjp);
    }
}
