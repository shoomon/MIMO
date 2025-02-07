package com.bisang.backend.s3.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    String name();
    String key();
    long waitTime() default 3;  // 락 획득 대기 시간 (초)
    long leaseTime() default 3;  // 락 자동 해제 시간 (초)
}
