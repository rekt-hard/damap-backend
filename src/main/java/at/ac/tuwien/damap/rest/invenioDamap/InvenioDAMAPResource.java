package at.ac.tuwien.damap.rest.invenioDamap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import at.ac.tuwien.damap.domain.Access;
import at.ac.tuwien.damap.enums.EAccessRight;
import at.ac.tuwien.damap.enums.EDataAccessType;
import at.ac.tuwien.damap.enums.EDataKind;
import at.ac.tuwien.damap.enums.EDataSource;
import at.ac.tuwien.damap.enums.EDataType;
import at.ac.tuwien.damap.enums.EIdentifierType;
import at.ac.tuwien.damap.repo.AccessRepo;
import at.ac.tuwien.damap.rest.dmp.domain.DatasetDO;
import at.ac.tuwien.damap.rest.dmp.domain.DmpDO;
import at.ac.tuwien.damap.rest.dmp.domain.IdentifierDO;
import at.ac.tuwien.damap.rest.dmp.mapper.MapperService;
import at.ac.tuwien.damap.rest.dmp.service.DmpService;
import at.ac.tuwien.damap.rest.madmp.dto.Dataset;
import at.ac.tuwien.damap.rest.madmp.service.MaDmpService;
import at.ac.tuwien.damap.security.SecurityService;
import at.ac.tuwien.damap.validation.AccessValidator;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import lombok.extern.jbosslog.JBossLog;

@Path("/api/invenio-damap/dmps")
@Produces(MediaType.APPLICATION_JSON)
@JBossLog
public class InvenioDAMAPResource {

    @Inject
    SecurityService securityService;

    @Inject
    AccessValidator accessValidator;

    @Inject
    AccessRepo accessRepo;

    @Inject
    DmpService dmpService;

    @Inject
    MaDmpService madmpService;

    @ConfigProperty(name = "invenio.shared-secret")
    String sharedSecret;

    /*
     * Maybe make it configurable?
     * Could be more elaborate but should suffice for PoC
     * Could also go into the {@link at.ac.tuwien.damap.security.SecurityService},
     * but keeping everything together for PoC
     * 
     */
    private boolean validateAuthHeader(HttpHeaders headers) {
        return sharedSecret.equals(headers.getHeaderString("Authorization"));
    }

    @GET
    @Path("/person/{personId}")
    public List<DmpDO> getDmpListByPerson(@PathParam String personId,
            @Context HttpHeaders headers) {
        log.info("Return dmp for person id: " + personId);

        // This could allow us to authenticate users with OIDC, and allow admins to
        // access all DMPs.
        // Since this will always evaluate to false as of right now, it will check the
        // auth header
        if (!(securityService.isAdmin() || securityService.getUserId() == personId)) {
            if (!validateAuthHeader(headers)) {
                throw new UnauthorizedException();
            }
        }

        List<Access> accessList = accessRepo.getAllDmpByUniversityId(personId);

        return accessList.stream().map(access -> {
            return dmpService.getDmpById(access.getDmp().id);
        }).collect(Collectors.toList());
    }

    @POST
    @Path("/{id}/{personId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public DmpDO addDataSetToDMP(@PathParam long id, @PathParam String personId, Dataset dataset,
            @Context HttpHeaders headers) {
        log.info("Add dataset to dmp with id: " + id);

        if (!(securityService.isAdmin() || securityService.getUserId() == personId)) {
            if (!validateAuthHeader(headers)) {
                throw new UnauthorizedException();
            }
        }

        if (!accessValidator.canEditDmp(id, personId)) {
            throw new ForbiddenException("Not authorized to access dmp with id " +
                    id);
        }

        DmpDO dmpDO = dmpService.getDmpById(id);
        var datasetDO = mapMaDMPDatasetToDatasetDO(dmpDO, new DatasetDO(), dataset, null);

        dmpDO.getDatasets().add(datasetDO);
        return dmpService.update(dmpDO);
    }

    public DatasetDO mapMaDMPDatasetToDatasetDO(DmpDO dmpDO,
            DatasetDO datasetDO,
            Dataset madmpDataset, MapperService mapperService) {

        // Disclaimer: This is by no means complete. Not all fields of the
        // Dataset or DMP are set. Null value checks should also be performed.
        var datasetId = madmpDataset.getDatasetId();
        if (datasetId != null) {
            IdentifierDO newId = new IdentifierDO();
            newId.setIdentifier(datasetId.getIdentifier());
            newId.setType(EIdentifierType.valueOf(datasetId.getType().name().toUpperCase()));
            datasetDO.setDatasetId(newId);
        }

        datasetDO.setDateOfDeletion(null);
        datasetDO.setDelete(false);
        datasetDO.setDeletionPerson(null);
        datasetDO.setDescription(madmpDataset.getDescription());
        datasetDO.setLegalRestrictions(null);
        datasetDO.setLicense("");
        datasetDO.setSize(0L);
        // General TODO: some attributes have to be set from distribution
        if (madmpDataset.getDistribution() != null) {
            var distributions = madmpDataset.getDistribution();
            StringBuilder licenseBuilder = new StringBuilder();
            distributions.forEach(d -> {
                var dataAccess = EDataAccessType.OPEN;
                if (d.getDataAccess() != null) {
                    dataAccess = EDataAccessType.getByValue(d.getDataAccess().name());
                }
                datasetDO.setDataAccess(dataAccess);
                licenseBuilder.append(d.getLicense().stream()
                        .map(l -> l.getLicenseRef().toString()).collect(Collectors.joining(", ")));
                datasetDO.setSize(datasetDO.getSize() + d.getByteSize());
            });
            datasetDO.setLicense(licenseBuilder.toString());
        }

        datasetDO.setOtherProjectMembersAccess(EAccessRight.READ);
        datasetDO.setPersonalData(false);
        datasetDO.setPublicAccess(EAccessRight.READ);
        datasetDO.setReasonForDeletion("");
        // TODO: should we set this? If so, what to ?
        datasetDO.setReferenceHash(null);
        datasetDO.setRetentionPeriod(null);
        datasetDO.setSelectedProjectMembersAccess(EAccessRight.READ);
        datasetDO.setSensitiveData(false);
        // TODO: Let user decide?
        datasetDO.setSource(EDataSource.NEW);
        // This should match the dataset. If new, setDataKind. else setReusedDataKind
        dmpDO.setDataKind(EDataKind.SPECIFY);
        dmpDO.setReusedDataKind(EDataKind.SPECIFY);
        datasetDO.setStartDate(null);
        datasetDO.setTitle(madmpDataset.getTitle());
        // Setting data type
        var types = new ArrayList<EDataType>();
        var type = EDataType.OTHER;
        try {
            log.info("dataset type: " + madmpDataset.getType());
            type = EDataType.getByValue(madmpDataset.getType());
        } catch (Exception e) {

        } finally {
            types.add(type);
            datasetDO.setType(types);
        }

        return datasetDO;
    }
}
