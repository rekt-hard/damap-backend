package at.ac.tuwien.damap.rest.base.service;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public interface ServiceRead<Entity> {
    Entity read(String id, MultivaluedMap<String, String> queryParams);
    default Entity read(String id) {
        return read(id, new MultivaluedHashMap<String,String>());
    }
}
