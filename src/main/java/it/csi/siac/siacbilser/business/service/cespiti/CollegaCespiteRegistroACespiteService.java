/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteRegistroACespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CollegaCespiteRegistroACespiteService extends CheckedAccountBaseService<CollegaCespiteRegistroACespite, CollegaCespiteRegistroACespiteResponse> {

	//DAD
	@Autowired
	CespiteDad cespiteDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkCondition(req.getListaCespiti()!=null && !req.getListaCespiti().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(" lista cespiti "));
		checkEntita(req.getMovimentoDettaglio(), "movimento dettaglio");
		for (Cespite cespite : req.getListaCespiti()) {
			checkEntita(cespite, "cespite non valido");
		}
	}
	
	@Override
	protected void init() {
		super.init();
		cespiteDad.setEnte(ente);
		cespiteDad.setLoginOperazione(loginOperazione);
	}
	
	@Transactional
	@Override
	public CollegaCespiteRegistroACespiteResponse executeService(CollegaCespiteRegistroACespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {	
		List<Cespite> listaScartati = new ArrayList<Cespite>();
		List<String> codiceCespiteCollegato = new ArrayList<String>();
		
		for(Cespite ces :req.getListaCespiti()){
			Long numeroCollegamentiDelCespiteAPrimeNote = cespiteDad.contaCespiticollegatiAPrimeNoteCogeInvDaAccettare(ces.getUid());			
			if(numeroCollegamentiDelCespiteAPrimeNote != null && numeroCollegamentiDelCespiteAPrimeNote.longValue() > 0L){
				String identificativoCespite = ces.getCodice() != null? ("codice " + ces.getCodice() ): " uid: " + ces.getUid();
				codiceCespiteCollegato.add(identificativoCespite);
				continue;
			}		
			ces = cespiteDad.findCespiteById(ces, new CespiteModelDetail[] {});
			boolean collegamentoAvvenutoConSuccesso = cespiteDad.collegaCespitiAPrimaNota(ces, req.getMovimentoDettaglio(), req.getInserimentoContestuale());
			if(!collegamentoAvvenutoConSuccesso) {
				listaScartati.add(ces);
			}
		}
		if(!codiceCespiteCollegato.isEmpty()) {
			res.addMessaggio(new Messaggio("CESPITI_NON_COLLEGATI", " I seguenti cespiti non sono stati collegati in quanto risultano essere gi&agrave; associati ad altra prima nota. Identificativi dei cespiti non collegati: " + StringUtils.join(codiceCespiteCollegato, ",")));
		}
		
		res.setListaScartati(listaScartati);
	}

}