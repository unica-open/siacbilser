/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCronoprogrammaResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CronoprogrammaDad;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * Ricerca di un cronoprogramma.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaCronoprogrammaService extends CheckedAccountBaseService<RicercaCronoprogramma, RicercaCronoprogrammaResponse> {

	/** The cronoprogramma. */
	private Cronoprogramma cronoprogramma;
	
	/** The valorizzato codice e stato progetto. */
	private boolean valorizzatoCodiceEStatoProgetto;
	
	/** The valorizzato uid progetto. */
	private boolean valorizzatoUidProgetto;
	
	
	/** The cronoprogramma dad. */
	@Autowired
	private CronoprogrammaDad cronoprogrammaDad;
	@Autowired private BilancioDad bilancioDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		//parametri del cronoprogramma
		cronoprogramma  = req.getCronoprogramma();
		checkNotNull(cronoprogramma, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("cronoprogramma"));
		
		checkEntita(cronoprogramma.getBilancio(), "bilancio cronoprogramma", false);
		checkEntita(cronoprogramma.getEnte(), "ente cronoprogramma", false);
		
		checkNotNull(cronoprogramma.getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice cronoprogramma"), false);
		checkNotNull(cronoprogramma.getStatoOperativoCronoprogramma(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo cronoprogramma"), false);
						
		
		//Check valorizzazione Progetto: o si valorizza l'uid oppure codice e stato.		
		checkNotNull(cronoprogramma.getProgetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("progetto"));		
		
		valorizzatoCodiceEStatoProgetto = (cronoprogramma.getProgetto().getCodice() !=null 
													&&  cronoprogramma.getProgetto().getStatoOperativoProgetto() != null);		
		valorizzatoUidProgetto = cronoprogramma.getProgetto().getUid()!=0;		
		
		checkCondition(valorizzatoUidProgetto || valorizzatoCodiceEStatoProgetto, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid progetto oppure codice e stato progetto"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		cronoprogrammaDad.setEnte(cronoprogramma.getEnte());
		cronoprogrammaDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		List<Cronoprogramma> cronos;
		
		caricaBilancio();
				
		if(valorizzatoUidProgetto) { 
			cronos = cronoprogrammaDad.findCronoprogrammiByCodiceAndStatoOperativoAndUidProgetto(cronoprogramma);
		} else {			
			cronos = cronoprogrammaDad.findCronoprogrammiByCodiceAndStatoOperativoAndCodiceEStatoProgetto(cronoprogramma);		
		}
		
		if(cronos == null){
			String msg = valorizzatoUidProgetto? " associato a uid progetto: "+cronoprogramma.getProgetto().getUid() 
					: " associato al progetto: "+cronoprogramma.getProgetto().getCodice() + "/" + cronoprogramma.getProgetto().getStatoOperativoProgetto();
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("cronoprogramma", cronoprogramma.getCodice() + "/"+ cronoprogramma.getStatoOperativoCronoprogramma() + msg));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		
		
		for (Cronoprogramma crono : cronos) {
			
			TipoProgetto tipoProgettoAssociato = cronoprogrammaDad.getTipoProgettoByCronoprogramma(crono);
			if(tipoProgettoAssociato == null) {
				throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("progetto associato al cronoprogramma", "tipo progetto"));
			}
			List<DettaglioEntrataCronoprogramma> dettagliEntrata = cronoprogrammaDad.findDettagliEntrataCronoprogramma(crono, tipoProgettoAssociato.getTipoCapitoloEntrata());
			crono.setCapitoliEntrata(dettagliEntrata);
			
			List<DettaglioUscitaCronoprogramma> dettagliUscita = cronoprogrammaDad.findDettagliUscitaCronoprogramma(crono, tipoProgettoAssociato.getTipoCapitoloSpesa());
			crono.setCapitoliUscita(dettagliUscita);
		}
		
		res.setCronoprogramma(cronos);

	}

	private void caricaBilancio() {
		Bilancio bilancio = bilancioDad.getBilancioByUid(cronoprogramma.getBilancio().getUid());
		if(bilancio == null ) {
			//non dovrebbe mai succedere, ma in ogni caso mi arrabbio.
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("bilancio"));
		}
		cronoprogramma.setBilancio(bilancio);
	}

}
