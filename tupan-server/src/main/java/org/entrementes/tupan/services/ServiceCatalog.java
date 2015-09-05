package org.entrementes.tupan.services;

import org.entrementes.tupan.configurations.ApiInformation;
import org.entrementes.tupan.expection.ServiceVersionNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class ServiceCatalog {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCatalog.class);

    private Map<String, TupanSmartGridService> serviceVersions;

    private ApiInformation apiInformation;

    @Autowired
    public ServiceCatalog(ApplicationContext context, ApiInformation apiInformation){
        this.serviceVersions = context.getBeansOfType(TupanSmartGridService.class);
        this.apiInformation = apiInformation;
    }

    public TupanSmartGridService getServiceByVersion(Optional<String> version) throws ServiceVersionNotFound {
        TupanSmartGridService result = null;
        if(version.isPresent()){
            result = this.serviceVersions.get(version.get());
            LOGGER.info("service version: {}", version.get());
        }else{
            result = this.serviceVersions.get(this.apiInformation.getVersion());
            LOGGER.info("service version: using latest {}", this.apiInformation.getVersion());
        }
        if(result == null){
            LOGGER.error("service version: could not find implementation");
            throw new ServiceVersionNotFound("could not find implementation for service version {}.".replace("{}",version.get()));
        }
        return result;
    }
}
