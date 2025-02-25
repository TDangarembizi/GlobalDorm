/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Tino
 */
@Path("applications")
public class ApplicationResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public ApplicationResource() {
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{username}/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateApplicationStatus(
            @PathParam("username") String username,
            @PathParam("roomId") int roomId,
            String newStatus) {
        try {
            ApplicationOps app = new ApplicationOps();
            boolean update = app.updateApplicationStatus(username, roomId, "Cancelled");

            if (update) {
                return Response.ok("{\"message\":\"Application status updated successfully.\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Application not found or status update failed.\"}")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"An error occurred while updating the application.\"}")
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response applyForRoom(@FormParam("username") String username,
            @FormParam("roomId") int roomId) {
        try {
            String status = "Pending"; // Default status for new applications 
            ApplicationOps app = new ApplicationOps();
            app.app(username, roomId, status);
            return Response.ok("{\"message\":\"Application submitted successfully.\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"An error occurred while submitting the application.\"}")
                    .build();
        }
    }

}
