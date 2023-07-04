package at.ac.tuwien.damap.rest.invenioDamap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import at.ac.tuwien.damap.rest.madmp.dto.Dataset;

import javax.ws.rs.core.MediaType;

// TODO: we should use some sort of commong authentication (shared secret)
// TODO: use config variable
@RegisterRestClient(baseUri = "https://127.0.0.1:5000")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface InvenioRemoteService {
    @GET
    @Path("/api/invenio_damap/damap/dmp/{dmpId}/dataset")
    @ClientHeaderParam(name = "Authorization", value = "Bearer <your token>")
    Dataset createDraftForDataset(@PathParam("dmpId") long dmpId, @QueryParam("title") String title,
            @QueryParam("description") String description);

    @GET
    @Path("/api/records")
    Object getRecords();
}
