/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.gruppoattivitaiva;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentoiva.RicercaAttivitaIvaLegateAGruppoAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.registroiva.RicercaRegistroIvaService;
import it.csi.siac.siacbilser.integration.dad.GruppoAttivitaIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaLegateAGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaLegateAGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * The Class EliminaGruppoAttivitaIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaGruppoAttivitaIvaService extends CheckedAccountBaseService<EliminaGruppoAttivitaIva, EliminaGruppoAttivitaIvaResponse> {
	
	/** The gruppo. */
	private GruppoAttivitaIva gruppo;
	
	/** The gruppo attivita iva dad. */
	@Autowired
	private GruppoAttivitaIvaDad gruppoAttivitaIvaDad;
	
	/** The ricerca attivita iva legate a gruppo attivita iva service. */
	@Autowired
	private RicercaAttivitaIvaLegateAGruppoAttivitaIvaService ricercaAttivitaIvaLegateAGruppoAttivitaIvaService;
	
	/** The ricerca registro iva service. */
	@Autowired
	private RicercaRegistroIvaService ricercaRegistroIvaService;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		gruppo= req.getGruppoAttivitaIva();
		
		checkNotNull(gruppo, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("gruppo"));
		checkCondition(gruppo.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid gruppo"));
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		gruppoAttivitaIvaDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public EliminaGruppoAttivitaIvaResponse executeService(EliminaGruppoAttivitaIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		if(!verificaEliminabilitaGruppo() ) {
			res.setEsito(Esito.FALLIMENTO);
			res.addErrore(ErroreFin.CANCELLAZIONE_GRUPPO_ATTIVITA_IVA_IMPOSSIBILE.getErrore());
			return;
		}
		gruppoAttivitaIvaDad.eliminaGruppoAttivitaIva(gruppo);
		res.setGruppoAttivitaIva(gruppo);
	}

	/**
	 * Verifica eliminabilita gruppo.
	 *
	 * @return true, if successful
	 */
	private boolean verificaEliminabilitaGruppo() {
		
		List<AttivitaIva> listaAttivita = ricercaAttivitaIvaLegate();
		if(!listaAttivita.isEmpty()){
			return false;
		}
		
		List<RegistroIva> listaRegistriIva = ricercaRegistriIvaLegate();
		return listaRegistriIva.isEmpty();
	}
	

	/**
	 * Ricerca attivita iva legate.
	 *
	 * @return the list
	 */
	private List<AttivitaIva> ricercaAttivitaIvaLegate() {
		RicercaAttivitaIvaLegateAGruppoAttivitaIva reqRAI = new RicercaAttivitaIvaLegateAGruppoAttivitaIva();
		reqRAI.setRichiedente(req.getRichiedente());
		reqRAI.setGruppoAttivitaIva(gruppo);
		RicercaAttivitaIvaLegateAGruppoAttivitaIvaResponse resRAI = executeExternalService(ricercaAttivitaIvaLegateAGruppoAttivitaIvaService, reqRAI);
		return resRAI.getListaAttivitaIva();
	}
	
	/**
	 * Ricerca registri iva legate.
	 *
	 * @return the list
	 */
	private List<RegistroIva> ricercaRegistriIvaLegate() {
		RicercaRegistroIva reqRRI = new RicercaRegistroIva();
		RegistroIva registroIva = new RegistroIva();
		registroIva.setEnte(gruppo.getEnte());
		registroIva.setGruppoAttivitaIva(gruppo);
		reqRRI.setRegistroIva(registroIva);
		reqRRI.setRichiedente(req.getRichiedente());
		RicercaRegistroIvaResponse resRRI =  executeExternalService(ricercaRegistroIvaService, reqRRI);
		return resRRI.getListaRegistroIva();
	}
	
}
