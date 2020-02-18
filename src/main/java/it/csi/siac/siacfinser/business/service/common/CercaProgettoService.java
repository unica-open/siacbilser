/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgettoResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoCercaProgrammaDto;




@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CercaProgettoService extends AbstractBaseService<EsistenzaProgetto, EsistenzaProgettoResponse>{

	@Autowired
	CommonDad commonDad;
	
	@Override
	protected void init() {
		
		
	}
	
//	@Override
//	@Transactional(readOnly=true)
//	public EsistenzaProgettoResponse executeService(EsistenzaProgetto serviceRequest) {
//		return super.executeService(serviceRequest);
//	}
	
	@Override
	public void execute() {
		String methodName = "CercaProgettoService - execute()";
		Ente ente = req.getRichiedente().getAccount().getEnte();
		
		/*
		 *  servizio che restituisce un bool a seconda dell'esistenza o meno di un progetto (es: prima pagina di inserimento impegni)
		 */
		
		EsitoCercaProgrammaDto esitoCercaProgrammaDto = commonDad.cercaProgramma(
				req.getCodiceProgetto(), 
				req.getCodiceTipoProgetto(), 
				req.getBilancio().getUid(), 
				req.getRichiedente());
		
		
		if(!esitoCercaProgrammaDto.isEsitenzaProgramma()){
			
			List<Errore> listaErrori = new ArrayList<Errore>(); 
			listaErrori.add(ErroreCore.NESSUN_DATO_REPERITO.getErrore("Progetto"));
			res.setErrori(listaErrori);
//			res.sete
			res.setEsito(Esito.FALLIMENTO);
		}
		
		// esistenza progetto
		res.setEsisteProgetto(esitoCercaProgrammaDto.isEsitenzaProgramma());
		// flag FPV
		res.setFlagEsistenzaFPV(esitoCercaProgrammaDto.isFlagRilevabileFPV());
		//Valore complessivo:
		res.setValoreComplessivo(esitoCercaProgrammaDto.getValoreComplessivo());
	}
}