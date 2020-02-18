/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.classificatorefin;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiTreeContoEconomico;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiTreeContoEconomicoResponse;
import it.csi.siac.siacfinser.integration.dad.ClassificatoreFinDad;
import it.csi.siac.siacfinser.model.ElementoContoEconomico;



@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiTreeContoEconomicoService 
extends AbstractBaseService<LeggiTreeContoEconomico, LeggiTreeContoEconomicoResponse>{
	
	@Autowired
	private ClassificatoreFinDad classificatoreFinDad;
	
	@Autowired
	private Mapper dozerBeanMapper;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition(req.getAnno() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"),false);
		checkCondition(req.getIdEnteProprietario() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("enteProprietarioId"),false);
	}
	
//	@Override
//	@Transactional(readOnly=true)
//	public LeggiTreeContoEconomicoResponse executeService(LeggiTreeContoEconomico serviceRequest) {
//		return super.executeService(serviceRequest);
//	}
	
	
	@Override //(propagation=Propagation.REQUIRED, readOnly=true)
	public void execute() {
		String methodName = "LeggiTreeContoEconomicoService - execute()";
			
		// servizio utilizzato per il caricamento dell'albero
		// del conto economico
		List<ElementoContoEconomico> treeContoEconomico = new ArrayList<ElementoContoEconomico>();
		
//		CR-2023 ho commentato perchè non serve piu leggere il conto economico e nella SiacDClassFamFinEnum ho commentato l'enum ContoEconomico
//				classificatoreFinDad.findPianoContoEconomico(
//						req.getAnno(), req.getIdEnteProprietario(), SiacDClassFamFinEnum.ContoEconomico.getCodice(), req.getIdCodificaPadre());
	
		if (treeContoEconomico == null || treeContoEconomico.isEmpty()) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("piano conto economico "));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		res.setCardinalitaComplessiva(treeContoEconomico.size());
		res.setTreeElementoContoEconomico(treeContoEconomico);
		
	}	
	
	

}
