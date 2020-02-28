package com.htwz.condition;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class AquariusCondition implements Condition {
    private String key;
    private String value;

    public AquariusCondition(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String beanName = context.getEnvironment().getProperty(key);

        return StringUtils.equals(beanName, value);
    }
}