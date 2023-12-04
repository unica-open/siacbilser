/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CausaleDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

// TODO: Auto-generated Javadoc
/**
 * Base service per il crud delle causali di entrata.
 *
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class CrudCausaleEntrataBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ,RES> {
	
	
	/** The causale dad. */
	@Autowired
	protected CausaleDad causaleDad;
	
	/** The soggetto dad. */
	@Autowired
	protected SoggettoDad soggettoDad;
	
	
	/** The causale. */
	protected CausaleEntrata causale;
	protected TipoCausale tipoCausale;

	/**
	 * Check accertamento.
	 */
	protected void checkAccertamento() {
		if (causale.getAccertamento()!=null 
				&& causale.getSubAccertamento()==null 
				&& causale.getAccertamento().getElencoSubAccertamenti()!=null){
			
			for(SubAccertamento subAccertamento: causale.getAccertamento().getElencoSubAccertamenti()){
				if(!subAccertamento.getStatoOperativoMovimentoGestioneEntrata().equals(StatoOperativoMovimentoGestione.ANNULLATO.name())){
					throw new BusinessException(ErroreFin.ACCERTAMENTO_CON_SUBACCERTAMENTI.getErrore(), Esito.FALLIMENTO);
				}
			}
			
		}
		
	}

	/**
	 * Check soggetto.
	 */
	protected void checkSoggetto() {
		//il soggetto è facoltativo
		if(causale.getSoggetto()==null || causale.getSoggetto().getUid()==0){
			return;
		}
		StatoOperativoAnagrafica statoOperativoAnagrafica = soggettoDad.findStatoOperativoAnagraficaSoggetto(causale.getSoggetto());	
		
		if(!StatoOperativoAnagrafica.VALIDO.equals(statoOperativoAnagrafica)){
			throw new BusinessException(it.csi.siac.siacfinser.model.errore.ErroreFin.SOGGETTO_NON_VALIDO.getErrore(), Esito.FALLIMENTO);
		}
		if( StatoOperativoAnagrafica.BLOCCATO.equals(statoOperativoAnagrafica)){
			throw new BusinessException(ErroreFin.SOGGETTO_BLOCCATO.getErrore(), Esito.FALLIMENTO);
		}
	}
	
	/**
	 * Check codice causale inserimento.
	 */
	protected void checkCodiceCausaleInserimento() {
		List<CausaleEntrata> causali = causaleDad.ricercaCausaliEntrataByCodiceETipo(causale.getCodice(), tipoCausale);
		
		if (!causali.isEmpty()){
			throw new BusinessException(ErroreFin.INSERIMENTO_CAUSALE_NON_POSSIBILE.getErrore(), Esito.FALLIMENTO);
		}
	}
	
	
	
	/**
	 * Check codice causale aggiornamento.
	 */
	protected void checkCodiceCausaleAggiornamento() {
		List<CausaleEntrata> causali = causaleDad.ricercaCausaliEntrataByCodiceETipo(causale.getCodice(), tipoCausale);		
		
		if (causali.size()==1) {			
			CausaleEntrata c = causali.get(0);
			if(c.getUid()!=causale.getUid()){
				throw new BusinessException(ErroreFin.INSERIMENTO_CAUSALE_NON_POSSIBILE.getErrore(), Esito.FALLIMENTO);
			}			
		}
	}
	
	
	
	
	
	/**
	 * Check congruenza soggetto incasso.
	 */
	protected void checkCongruenzaSoggettoIncasso() {
		final String methodName = "checkCongruenzaSoggettoIncasso";
    	//il controllo viene effettuato solo se sono presenti sia soggetto sia accertamento
		Soggetto soggetto = causale.getSoggetto();
		Accertamento accertamento = causale.getAccertamento();
		SubAccertamento subAccertamento = causale.getSubAccertamento();
		
		if (soggetto==null || soggetto.getUid() == 0 || accertamento==null || accertamento.getUid()==0 ){
			return;
		}
		
		if( subAccertamento != null  && subAccertamento.getUid() != 0){
			Soggetto soggettoSubAccertamento= soggettoDad.findSoggettoByIdSubMovimentoGestione(causale.getSubAccertamento().getUid());
			if(soggettoSubAccertamento != null && soggettoSubAccertamento.getUid() != 0
					&& !soggettoSubAccertamento.getCodiceSoggetto().equals(soggetto.getCodiceSoggetto())){
				log.debug(methodName, "uid soggetto subimpegno: " + soggettoSubAccertamento.getUid() );
				log.debug(methodName, "uid soggetto causale: " + soggetto.getUid() );
				throw new BusinessException(ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto di incasso", " il subaccertamento", "per la causale di incasso", 
						"subaccertamento con soggetto specifico : il soggetto di pagamento deve essere lo stesso del subaccertamento "));
			}
		} else{
			Soggetto soggettoAccertamento= soggettoDad.findSoggettoByIdMovimentoGestione(causale.getAccertamento().getUid());
			if(soggettoAccertamento != null && soggettoAccertamento.getUid() != 0 && !soggettoAccertamento.getCodiceSoggetto().equals(soggetto.getCodiceSoggetto())){
				log.debug(methodName, "uid soggetto impegno: " + soggettoAccertamento.getUid() );
				log.debug(methodName, "uid soggetto causale: " + soggetto.getUid() );
				throw new BusinessException(ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto di incasso", " l'accertamento", "per la causale di incasso", 
						"accertamento con soggetto specifico : il soggetto di incasso deve essere lo stesso dell'accertamento "));
			}
		}
	}



}
