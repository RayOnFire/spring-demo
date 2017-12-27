package com.example.demo.aspect;

import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.User;
import com.example.demo.model.UserOrder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by Ray on 2017/7/12.
 */
@Component
@Aspect
public class ControllerRoleAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());
    /*
    @Around("execution(* com.example.demo.controller.OrderController.*(..)) && @annotation(com.example.demo.annotation.SellerOnly)")
    public Object orderSellerAccess(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("access seller only endpoint");
        Object[] args = pjp.getArgs();
        for (Object obj : args) {
            if (obj instanceof UserOrder) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Object principal = authentication.getPrincipal();
                if (principal instanceof CustomUserDetails) {
                    User user = ((CustomUserDetails) principal).getUser();
                    if (((UserOrder) obj))
                }
            }
        }
        Object ret = pjp.proceed(args);
        return ret;
    }
    */
    // TODO: 增加权限控制
}
