package at.ac.tuwien.damap.rest;

import at.ac.tuwien.damap.rest.config.domain.ServiceConfig;
import at.ac.tuwien.damap.rest.persons.PersonService;
import io.quarkus.arc.All;
import lombok.extern.jbosslog.JBossLog;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;

import java.util.LinkedHashMap;
import java.util.List;

@JBossLog
@ApplicationScoped
public class PersonServiceBroker {

    LinkedHashMap<String, PersonService> personServices = new LinkedHashMap<>();

    @Inject
    public PersonServiceBroker(ConfigResource config, @All List<PersonService> availableServices) {
        List<ServiceConfig> configuredServices = config.personServiceConfigurations.getConfigs();

        configuredServices.forEach(serviceConfig -> {
            boolean found = false;
            String configClassName = serviceConfig.getClassName();
            for (var service : availableServices) {
                try {
                    String serviceClassName = service.getClass().getCanonicalName().split("_ClientProxy")[0];

                    if (configClassName.equals(serviceClassName)) {
                        personServices.put(serviceConfig.getQueryValue(), service);
                        found = true;
                        break;
                    }
                } catch (Exception e) {
                    log.error(String.format("Issue trying to initialize person service %s", configClassName), e);
                }
            }
            if (!found) {
                log.warn(String.format("Service '%s' configured but is not available", configClassName));
            }
        });
    }

    public PersonService getServiceFromQueryParams(MultivaluedMap<String, String> queryParams) {
        return this.getServiceForQueryParam(queryParams.getFirst("searchService"));
    }

    public PersonService getServiceForQueryParam(String searchServiceType) {
        PersonService searchService = personServices.get(searchServiceType);
        if (searchService == null && !personServices.isEmpty()) {
            searchService = personServices.entrySet().iterator().next().getValue();
        }

        return searchService;
    }
}
