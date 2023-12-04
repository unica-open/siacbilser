/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.tipodocumentofel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.integration.dad.TipoDocumentoFELDad;
import it.csi.siac.siacbilser.model.TipoDocFEL;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

 

/**
 * The Class RicercaPuntualeCapitoloEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaPuntualeTipoDocumentoFELService
	extends CheckedAccountBaseService<RicercaTipoDocumentoFEL, RicercaTipoDocumentoFELResponse> {
	
 
	@Autowired
	protected  TipoDocumentoFELDad tipoDocumentoFELDad;

	@Override
	protected void init() {
		tipoDocumentoFELDad.setEnte(ente);
		tipoDocumentoFELDad.setLoginOperazione(loginOperazione);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {	 	
		TipoDocFEL criteriRicerca = req.getTipoDocFEL();
		checkNotNull(criteriRicerca, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		checkNotNull(criteriRicerca.getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Codice"), false);
		
	}
	
 

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly=true)
	public RicercaTipoDocumentoFELResponse executeService(RicercaTipoDocumentoFEL serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		TipoDocFEL capitoloEntrataPrevisione = tipoDocumentoFELDad.ricercaPuntualeTipoDocumentoFEL(req.getTipoDocFEL());
	
		if(capitoloEntrataPrevisione==null){
			TipoDocFEL criteriRicerca = req.getTipoDocFEL();
			
			
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Tipo Documento",String.format("Codice: %s/%s/%s/%s/%s/%s", criteriRicerca.getCodice())));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
//		Bilancio bilancio = capitoloEntrataPrevisioneDad.getBilancioAssociatoACapitolo(capitoloEntrataPrevisione);
//		capitoloEntrataPrevisione.setBilancio(bilancio);
//		//res.setBilancio(bilancio);//TODO inutile e ridondante. eliminarlo dal Model		
//		
//		ImportiCapitoloEP importiCapitolo = importiCapitoloDad.findImportiCapitolo(capitoloEntrataPrevisione, req.getRicercaPuntualeCapitoloEPrev().getAnnoEsercizio(), ImportiCapitoloEP.class, req.getImportiDerivatiRichiesti());
//		capitoloEntrataPrevisione.setImportiCapitoloEP(importiCapitolo);
//		//res.setImportiCapitoloEP(importiCapitoloUP); //TODO inutile e ridondante. eliminarlo dal Model
//		
//		capitoloEntrataPrevisioneDad.populateFlags(capitoloEntrataPrevisione);
//		
//		StrutturaAmministrativoContabile struttAmmCont = capitoloEntrataPrevisioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(capitoloEntrataPrevisione);
//		capitoloEntrataPrevisione.setStrutturaAmministrativoContabile(struttAmmCont);
//		//res.setStrutturaAmministrativoContabile(struttAmmCont);//TODO inutile e ridondante.  eliminarlo dal Model
//		
//		ElementoPianoDeiConti elementoPianoDeiConti = capitoloEntrataPrevisioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(capitoloEntrataPrevisione);
//		capitoloEntrataPrevisione.setElementoPianoDeiConti(elementoPianoDeiConti);
//		//res.setElementoPianoDeiConti(elementoPianoDeiConti);	//TODO inutile e ridondante.  eliminarlo dal Model
//		
//		
//		List<TipoDocFEL> ff = new RicercaPuntualeTipoDocumentoFELService()
//		
//		res.setTipoDocumentiFEL(capitoloEntrataPrevisione);
		res.setEsito(Esito.SUCCESSO);
	}
	
	

}
