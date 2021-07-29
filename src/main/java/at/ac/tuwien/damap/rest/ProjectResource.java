package at.ac.tuwien.damap.rest;

import at.ac.tuwien.damap.rest.domain.ProjectDO;
import at.ac.tuwien.damap.rest.domain.ProjectMemberDO;
import at.ac.tuwien.rest.projectdatabase.service.ProjectDatabaseService;
import lombok.extern.jbosslog.JBossLog;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/pdb")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@JBossLog
public class ProjectResource {

    @Inject
    ProjectDatabaseService projectDatabaseService;

    @GET
    @Path("/suggest-projects/{personId}")
    public List<ProjectDO> getProjectSuggestionsForPerson(@PathParam("personId") String personId) {
        log.info(String.format("Get project suggestions for person with id: %s", personId));
        return projectDatabaseService.getProjectSuggestionsForPerson(personId);
    }


    @GET
    @Path("/project-staff/{id}")
    public List<ProjectMemberDO> getProjectMembers(@PathParam("id") String projectId) {
        log.info(String.format("Get Project Staff for Project ID=%s", projectId));
        return projectDatabaseService.getProjectStaff(projectId);
    }
}
