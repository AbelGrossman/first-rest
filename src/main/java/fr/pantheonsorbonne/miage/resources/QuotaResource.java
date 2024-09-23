package fr.pantheonsorbonne.miage.resources;

import fr.pantheonsorbonne.miage.dao.QuotaDAO;
import fr.pantheonsorbonne.miage.dto.BookingRequest;
import fr.pantheonsorbonne.miage.dto.Quota;
import fr.pantheonsorbonne.miage.service.InsufficientQuotaException;
import fr.pantheonsorbonne.miage.service.NoSuchQuotaException;
import fr.pantheonsorbonne.miage.service.QuotaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/vendor/{vendorId}/quota")
public class QuotaResource {

    @Inject
    QuotaService quotaService;

    @Inject
    QuotaDAO dao;

    @GET
    @Path("/{concertId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Quota getQuotaForVendorAndConcert(
            @PathParam("vendorId") int vendorId,
            @PathParam("concertId") int concertId) {
        return dao.getQuota(vendorId, concertId);

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Quota> getQuotaForVendor(
            @PathParam("vendorId") int vendorID) {
        return dao.getQuotas(vendorID);
    }

    @PUT
    @Path("/{concertId}")
    public Response bookConcert(
            @PathParam("vendorId") int vendorId,
            @PathParam("concertId") int concertId,
            BookingRequest bookingRequest)  {
        try {
            quotaService.bookTickets(vendorId,
                    concertId,
                    bookingRequest.seated(),
                    bookingRequest.standing());
            return Response.status(404).entity("toto").build();


        } catch (InsufficientQuotaException e) {
            throw new WebApplicationException(Response.status(400).entity("not enough quota").build());
        } catch (NoSuchQuotaException e) {
            throw new WebApplicationException("NoSuch Quota ",404);
        }
    }
}
