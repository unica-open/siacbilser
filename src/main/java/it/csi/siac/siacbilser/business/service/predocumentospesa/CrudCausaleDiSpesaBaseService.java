/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentospesa;

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
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

// TODO: Auto-generated Javadoc
/**
 * Base service per il crud della causale di spesa.
 *
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class CrudCausaleDiSpesaBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ,RES> {
	
	
	/** The causale dad. */
	@Autowired
	protected CausaleDad causaleDad;
	
	/** The soggetto dad. */
	@Autowired
	protected SoggettoDad soggettoDad;
	
	/** The causale. */
	protected CausaleSpesa causale;
	protected TipoCausale tipoCausale;
	
	
	/**
	 * Check soggetto.
	 */
	protected void checkSoggetto() {
		if(causale.getSoggetto()==null){
			return;
		}
		if( causale.getSoggetto().getUid()==0){
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
		
		List<CausaleSpesa> causali = causaleDad.ricercaCausaliSpesaByCodiceETipo(causale.getCodice(), tipoCausale);		
		if(!causali.isEmpty()) {			
			throw new BusinessException(ErroreFin.INSERIMENTO_CAUSALE_NON_POSSIBILE.getErrore(), Esito.FALLIMENTO);
		}
	}
	
	/**
	 * Check codice causale aggiornamento.
	 */
	protected void checkCodiceCausaleAggiornamento() {		
		List<CausaleSpesa> causali = causaleDad.ricercaCausaliSpesaByCodiceETipo(causale.getCodice(), tipoCausale);		
		
		if (causali.size()==1) {			
			CausaleSpesa c = causali.get(0);
			if(c.getUid()!=causale.getUid()){
				throw new BusinessException(ErroreFin.INSERIMENTO_CAUSALE_NON_POSSIBILE.getErrore(), Esito.FALLIMENTO);
			}			
		}
	}
	
	
	/**
	 * Check congruenza soggetto pagamento.
	 */
	protected void checkCongruenzaSoggettoPagamento() {
    	//il controllo viene effettuato solo se sono presenti sia soggetto sia accertamento
		final String methodName = "checkCongruenzaSoggettoPagamento";
		Soggetto soggetto = causale.getSoggetto();
		Impegno impegno = causale.getImpegno();
		SubImpegno subImpegno = causale.getSubImpegno();
		
		if (soggetto==null || soggetto.getUid() == 0|| impegno==null || impegno.getUid() == 0){
			log.debug(methodName, "esco");
			return;
		}
		
		if( subImpegno != null  && subImpegno.getUid() != 0){
			Soggetto soggettoImpegno= soggettoDad.findSoggettoByIdSubMovimentoGestione(causale.getSubImpegno().getUid());
			log.debug(methodName, "subimpegno uid: " + subImpegno.getUid() );
			if(soggettoImpegno != null && soggettoImpegno.getUid() != 0 && !soggettoImpegno.getCodiceSoggetto().equals(soggetto.getCodiceSoggetto())){
				log.debug(methodName, "uid soggetto subimpegno: " + soggettoImpegno.getUid() );
				log.debug(methodName, "uid soggetto causale: " + soggetto.getUid() );
				throw new BusinessException(ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto di pagamento", " il subimpegno", "per la causale di pagamento", 
						"subimpegno con soggetto specifico : il soggetto di pagamento deve essere lo stesso del subimpegno "));
			}
		} else{
			Soggetto soggettoImpegno= soggettoDad.findSoggettoByIdMovimentoGestione(causale.getImpegno().getUid());
			if(soggettoImpegno != null && soggettoImpegno.getUid() != 0 && !soggettoImpegno.getCodiceSoggetto().equals(soggetto.getCodiceSoggetto())){
				log.debug(methodName, "uid soggetto impegno: " + soggettoImpegno.getUid() );
				log.debug(methodName, "uid soggetto causale: " + soggetto.getUid() );
				throw new BusinessException(ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto di pagamento", " l'impegno", "per la causale di pagamento", 
						"impegno con soggetto specifico : il soggetto di pagamento deve essere lo stesso dell'impegno "));
			}
		}
		
	}
	

	/**
	 * Check impegno.
	 */
	protected void checkImpegno() {
		if (causale.getImpegno()!=null 
				&& causale.getSubImpegno()==null 
				&& causale.getImpegno().getElencoSubImpegni()!=null){
			
			for(SubImpegno subImpegno: causale.getImpegno().getElencoSubImpegni()){
				if(!subImpegno.getStatoOperativoMovimentoGestioneSpesa().equals(StatoOperativoMovimentoGestione.ANNULLATO.name())){
					throw new BusinessException(ErroreFin.IMPEGNO_CON_SUBIMPEGNI_VALIDI.getErrore(), Esito.FALLIMENTO);
				}
			}
			
		}
		
	}
	

}
