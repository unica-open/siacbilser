/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.previsioneimpegnatoaccertato;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.InseriscePrevisioneImpegnatoAccertato;
import it.csi.siac.siacbilser.frontend.webservice.msg.InseriscePrevisioneImpegnatoAccertatoResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.PrevisioneImpegnatoAccertato;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class InserisceCapitoloDiEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InseriscePrevisioneImpegnatoAccertatoService 
	extends CrudPrevisioneImpegnatoAccertatoBaseService<InseriscePrevisioneImpegnatoAccertato, InseriscePrevisioneImpegnatoAccertatoResponse> {

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		initDads();
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		previsioneImpegnatoAccertato = req.getPrevisioneImpegnatoAccertatoSuCapitolo();
		checkNotNull(previsioneImpegnatoAccertato, "previsione impegnato/accertato");
		Capitolo<?,?> capitolo =  previsioneImpegnatoAccertato.getCapitolo();
		checkCondition(previsioneImpegnatoAccertato.getUid() != 0 ||  capitolo != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo o previsione impegnato"), true);
		checkCondition(capitolo.getAnnoCapitolo() != null && capitolo.getAnnoCapitolo().intValue() !=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"));
		checkCondition(capitolo.getNumeroCapitolo() != null && capitolo.getNumeroCapitolo().intValue() !=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"));
		checkCondition(capitolo.getNumeroArticolo() != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"));
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.service.capitolo.CrudCapitoloBaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public InseriscePrevisioneImpegnatoAccertatoResponse executeService(InseriscePrevisioneImpegnatoAccertato serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		caricaIdCapitoloByChiaveLogica();	
		
		checkStatoCapitolo(req.getPrevisioneImpegnatoAccertatoSuCapitolo().getCapitolo().getUid());
		
		PrevisioneImpegnatoAccertato previsioneImpegnatoDaDb = caricaPrevisioneImpegnatoDaDb();		
		if(previsioneImpegnatoDaDb != null && previsioneImpegnatoDaDb.getUid() != 0) {
			res.setPrevisioneImpegnatoAccertatoSuCapitolo(previsioneImpegnatoDaDb);
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore(
					"impostazione del capitolo " + getChiaveLogicaCapitolo()  + " per la previsione",
					"relazione"));
		}
			
		PrevisioneImpegnatoAccertato created = previsioneImpegnatoAccertatoDad.create(req.getPrevisioneImpegnatoAccertatoSuCapitolo());
		
		res.setPrevisioneImpegnatoAccertatoSuCapitolo(created);
		res.addMessaggio(ErroreCore.OPERAZIONE_EFFETTUATA.getErrore("elaborazione capitolo " + getChiaveLogicaCapitolo(), "correttamente."));
	}

}
