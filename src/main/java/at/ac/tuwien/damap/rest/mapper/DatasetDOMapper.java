package at.ac.tuwien.damap.rest.mapper;

import at.ac.tuwien.damap.domain.Dataset;
import at.ac.tuwien.damap.enums.EDataAccessType;
import at.ac.tuwien.damap.rest.domain.*;

public class DatasetDOMapper {

    public static void mapEntityToDO(Dataset dataset, DatasetDO datasetDO) {
        datasetDO.setId(dataset.id);
        datasetDO.setTitle(dataset.getTitle());
        datasetDO.setType(dataset.getType());
        datasetDO.setSize(dataset.getSize());
        datasetDO.setComment(dataset.getComment());
        datasetDO.setPersonalData(dataset.getPersonalData());
        datasetDO.setSensitiveData(dataset.getSensitiveData());
        datasetDO.setLegalRestrictions(dataset.getLegalRestrictions());
        datasetDO.setLicense(dataset.getLicense());
        datasetDO.setStartDate(dataset.getStart());
        datasetDO.setReferenceHash(dataset.getReferenceHash());
        if (dataset.getDataAccess() != null)
            datasetDO.setDataAccess(dataset.getDataAccess().getValue());
    }

    public static void mapDOtoEntity(DatasetDO datasetDO, Dataset dataset){
        if (datasetDO.getId() != null)
            dataset.id = datasetDO.getId();
        dataset.setTitle(datasetDO.getTitle());
        dataset.setType(datasetDO.getType());
        dataset.setSize(datasetDO.getSize());
        dataset.setComment(datasetDO.getComment());
        dataset.setPersonalData(datasetDO.getPersonalData());
        dataset.setSensitiveData(datasetDO.getSensitiveData());
        dataset.setLegalRestrictions(datasetDO.getLegalRestrictions());
        dataset.setLicense(datasetDO.getLicense());
        dataset.setStart(datasetDO.getStartDate());
        dataset.setReferenceHash(datasetDO.getReferenceHash());
        dataset.setDataAccess(EDataAccessType.getByValue(datasetDO.getDataAccess()));
    }}
