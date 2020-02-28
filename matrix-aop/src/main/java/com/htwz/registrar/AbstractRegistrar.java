package com.htwz.registrar;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public abstract class AbstractRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;
    private ClassLoader classLoader;
    private Environment environment;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        registerAnnotations(metadata, registry);
    }

    private void registerAnnotations(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);

        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(getAnnotationClass());
        scanner.addIncludeFilter(annotationTypeFilter);
        Set<String> basePackages = getBasePackages(metadata);

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    // verify annotated class is an interface
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();

                    Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(getAnnotationClass().getCanonicalName());
                    registerAnnotation(registry, annotationMetadata, attributes);
                }
            }
        }
    }

    private void registerAnnotation(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();

        log.info("Found annotation [{}] in {} ", getAnnotationClass().getSimpleName(), className);

        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(getBeanClass());
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();

        customize(registry, annotationMetadata, attributes, definition);

        try {
            definition.addPropertyValue("interface", Class.forName(className));
        } catch (ClassNotFoundException e) {
            log.error("Get interface for name error", e);
        }
        definition.addPropertyValue("interceptor", getInterceptor(beanDefinition.getPropertyValues()));

        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className, new String[]{className});
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().getInterfaceNames().length == 1 && Annotation.class.getName().equals(beanDefinition.getMetadata().getInterfaceNames()[0])) {
                        try {
                            Class<?> target = ClassUtils.forName(beanDefinition.getMetadata().getClassName(), AbstractRegistrar.this.classLoader);

                            return !target.isAnnotation();
                        } catch (Exception ex) {
                            log.error("Could not load target class: " + beanDefinition.getMetadata().getClassName(), ex);
                        }
                    }

                    return true;
                }

                return false;
            }
        };
    }

    private Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(getEnableAnnotationClass().getCanonicalName());
        if(CollUtil.isEmpty(attributes)){
            return Collections.emptySet();
        }
        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class[]) attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }

        return basePackages;
    }

    private void customize(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, Map<String, Object> attributes, BeanDefinitionBuilder definition) {
        for (Map.Entry<String, Object> attribute : attributes.entrySet()) {
            definition.addPropertyValue(attribute.getKey(), attribute.getValue());
        }
    }

    public abstract Class<? extends Annotation> getEnableAnnotationClass();

    protected abstract Class<? extends Annotation> getAnnotationClass();

    protected abstract Class<?> getBeanClass();

    protected abstract MethodInterceptor getInterceptor(MutablePropertyValues annotationValues);
}