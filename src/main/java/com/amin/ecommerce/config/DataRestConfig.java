package com.amin.ecommerce.config;

import com.amin.ecommerce.entity.Product;
import com.amin.ecommerce.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Type;
import java.util.Set;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {
    private final EntityManager entityManager;

    @Autowired
    public DataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        disableHttpUpdateMethods(config);
        exposeIds(config);
    }

    private void disableHttpUpdateMethods(RepositoryRestConfiguration config) {
        HttpMethod[] unsupportedHttpMethods = {HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE};

        config.getExposureConfiguration().forDomainType(Product.class)
                .withItemExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedHttpMethods))
                .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedHttpMethods));

        config.getExposureConfiguration().forDomainType(ProductCategory.class)
                .withItemExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedHttpMethods))
                .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedHttpMethods));
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        Set<EntityType<?>> entityTypes = entityManager.getMetamodel().getEntities();
        Class<?>[] domainClasses = entityTypes.stream().map(Type::getJavaType).toArray(Class[]::new);
        config.exposeIdsFor(domainClasses);
    }
}
