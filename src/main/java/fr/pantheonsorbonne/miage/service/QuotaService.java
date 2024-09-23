package fr.pantheonsorbonne.miage.service;

import fr.pantheonsorbonne.miage.dao.QuotaDAO;
import fr.pantheonsorbonne.miage.dto.Quota;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Objects;
@ApplicationScoped
public class QuotaService {

    @Inject
    QuotaDAO quotaDAO;

    public void bookTickets(int vendorId,int concertId, int seated, int standing) throws InsufficientQuotaException, NoSuchQuotaException {


        Quota quota = quotaDAO.getQuota(vendorId,concertId);
        if(null==quota){
            throw new NoSuchQuotaException();
        }
        var reaminingSeated = quota.seated()-seated;
        var reaminingStanding = quota.standing()-standing;

        if(reaminingSeated>=0 && reaminingStanding>=0){
            quotaDAO.saveQuota(
                    new Quota(vendorId,concertId,reaminingSeated,reaminingStanding));

        }
        else{
            throw new InsufficientQuotaException();
        }




    }



}
