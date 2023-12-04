/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.previsioneimpegnatoaccertato;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaPrevisioneImpegnatoAccertato;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaPrevisioneImpegnatoAccertatoResponse;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassFamEnum;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ImportoDerivatoFunctionEnum;
import it.csi.siac.siacbilser.model.PrevisioneImpegnatoAccertato;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class InserisceCapitoloDiEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaPrevisioneImpegnatoAccertatoService 
	extends CrudPrevisioneImpegnatoAccertatoBaseService<AggiornaPrevisioneImpegnatoAccertato, AggiornaPrevisioneImpegnatoAccertatoResponse> {

	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;

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
		
		checkCondition(previsioneImpegnatoAccertato.getUid() != 0 ||  capitolo.getUid() != 0 || (capitolo.getAnnoCapitolo() != null && capitolo.getAnnoCapitolo().intValue() !=0), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"));
		checkCondition(previsioneImpegnatoAccertato.getUid() != 0 ||  capitolo.getUid() != 0 || (capitolo.getNumeroCapitolo() != null && capitolo.getNumeroCapitolo().intValue() !=0), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"));
		checkCondition(previsioneImpegnatoAccertato.getUid() != 0 || capitolo.getUid() != 0 || (capitolo.getNumeroArticolo() != null), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"));
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.service.capitolo.CrudCapitoloBaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public AggiornaPrevisioneImpegnatoAccertatoResponse executeService(AggiornaPrevisioneImpegnatoAccertato serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkImportiPrevisione();
		
		caricaIdCapitoloByChiaveLogica();
		
		PrevisioneImpegnatoAccertato previsioneImpegnatoAccertatoSuDb = caricaPrevisioneImpegnatoDaDb();
		
		if(previsioneImpegnatoAccertatoSuDb == null || previsioneImpegnatoAccertatoSuDb.getUid() == 0) {
			
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore(
					"il capitolo " + getChiaveLogicaCapitolo()+" non risulta essere predisposto per la gestione della previsione",
					"relazione"));
		}
		checkCapitoloUtilizzabile(previsioneImpegnatoAccertatoSuDb.getCapitolo());
		
		PrevisioneImpegnatoAccertato updated = previsioneImpegnatoAccertatoDad.updateImporti(previsioneImpegnatoAccertatoSuDb.getUid(), req.getPrevisioneImpegnatoAccertatoSuCapitolo());
		res.setPrevisioneImpegnatoAccertatoSuCapitolo(updated);
	}

	private void checkCapitoloUtilizzabile(Capitolo<?, ?> capitolo) {
		if(capitolo == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("capitolo non predisposto per la previsione [" + previsioneImpegnatoAccertato.getUid() +  "]."));
		}
		isSACCapitoloCoerenteConAccount(capitolo);
		checkDispImpegnareCapitoloSpesa(capitolo);
		checkStatoCapitolo(capitolo.getUid());
	}

	protected void checkDispImpegnareCapitoloSpesa(Capitolo<?, ?> capitolo) {
		TipoCapitolo tipoCapitolo = capitoloDad.findTipoCapitolo(capitolo.getUid());
		if(tipoCapitolo != null && TipoCapitolo.isTipoCapitoloEntrata(tipoCapitolo)) {
			return;
		}
		int annoCapitolo = capitolo.getAnnoCapitolo() != null && capitolo.getAnnoCapitolo().intValue() != 0?  capitolo.getAnnoCapitolo().intValue()
				: req.getAnnoBilancio() != null ? req.getAnnoBilancio().intValue() : 0;
		
		BigDecimal stanziamentoAnno1 = caricaSingoloStanziamento(capitolo, annoCapitolo);
		BigDecimal impegnatoAnno1 = caricaSingoloImportoDerivato(capitolo, ImportoDerivatoFunctionEnum.impegnatoEffettivoUGAnno1);
		BigDecimal differenzaStanziatoImpegnato1 = stanziamentoAnno1.subtract(impegnatoAnno1);
		BigDecimal importoPrevisioneAnno1 = previsioneImpegnatoAccertato.getImportoPrevAnno1() != null? previsioneImpegnatoAccertato.getImportoPrevAnno1() : BigDecimal.ZERO ;
		
		if(importoPrevisioneAnno1.compareTo(differenzaStanziatoImpegnato1) > 0) {
			String anno1 = annoCapitolo != 0 ? String.valueOf(annoCapitolo) : "null";
			throw new BusinessException(ErroreBil.IMPORTO_PREVISIONE.getErrore(anno1));
		}
		
		
		
		BigDecimal stanziamentoAnno2 = caricaSingoloStanziamento(capitolo, annoCapitolo +1);
		BigDecimal impegnatoAnno2 = caricaSingoloImportoDerivato(capitolo, ImportoDerivatoFunctionEnum.impegnatoEffettivoUGAnno2);
		BigDecimal differenzaStanziatoImpegnato2 = stanziamentoAnno2.subtract(impegnatoAnno2);
		BigDecimal importoPrevisioneAnno2 = previsioneImpegnatoAccertato.getImportoPrevAnno2() != null? previsioneImpegnatoAccertato.getImportoPrevAnno2() : BigDecimal.ZERO ;
		
		if(importoPrevisioneAnno2.compareTo(differenzaStanziatoImpegnato2) > 0) {
			String anno2 = annoCapitolo != 0 ? String.valueOf((annoCapitolo +1 )) : "null";
			throw new BusinessException(ErroreBil.IMPORTO_PREVISIONE.getErrore(anno2));
		}
		
		BigDecimal stanziamentoAnno3 = caricaSingoloStanziamento(capitolo, annoCapitolo +2);
		BigDecimal impegnatoAnno3 = caricaSingoloImportoDerivato(capitolo, ImportoDerivatoFunctionEnum.impegnatoEffettivoUGAnno3);
		BigDecimal differenzaStanziatoImpegnato3 = stanziamentoAnno3.subtract(impegnatoAnno3);
		BigDecimal importoPrevisioneAnno3 = previsioneImpegnatoAccertato.getImportoPrevAnno3() != null? previsioneImpegnatoAccertato.getImportoPrevAnno3() : BigDecimal.ZERO ;
		
		if(importoPrevisioneAnno3.compareTo(differenzaStanziatoImpegnato3) > 0) {
			String anno3 = annoCapitolo != 0 ? String.valueOf((annoCapitolo +2)) : "null";
			throw new BusinessException(ErroreBil.IMPORTO_PREVISIONE.getErrore(anno3));
		}
	}

	protected BigDecimal caricaSingoloImportoDerivato(Capitolo<?, ?> capitolo, ImportoDerivatoFunctionEnum importoDerivatoEnum) {
		BigDecimal importoDerivato = importiCapitoloDad.findImportoDerivato(capitolo.getUid(), importoDerivatoEnum);
		if(importoDerivato == null) {
			importoDerivato = BigDecimal.ZERO;
		}
		return importoDerivato;
	}

	protected BigDecimal caricaSingoloStanziamento(Capitolo<?, ?> capitolo, int anno) {
		BigDecimal stanziamento = importiCapitoloDad.caricaSingoloImportoCapitolo(capitolo.getUid(), anno, SiacDBilElemDetTipoEnum.Stanziamento);
		if(stanziamento == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("stanziamento per anno " + anno + " non presente in archivio."));
		}
		return stanziamento;
	}

	protected void isSACCapitoloCoerenteConAccount(Capitolo<?, ?> capitolo) {
		StrutturaAmministrativoContabile sac = capitoloDad.ricercaClassificatoreByClassFamAndUid(capitolo.getUid(), SiacDClassFamEnum.StrutturaAmministrativaContabile);
		if(sac == null) {
			//???
			return;
		}
		boolean sacCollegataAdAccount = accountDad.isSacCollegataAdAccount(req.getRichiedente().getAccount(), sac, req.getAnnoBilancio());
		if(!sacCollegataAdAccount) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("la struttura del capitolo ( " + StringUtils.defaultIfBlank(sac.getCodice(), "") + " ) non e' tra le strutture dell'account selezionato."));
		}
	}
	
	private void checkImportiPrevisione() {
		int annoBilancio = req.getAnnoBilancio()!= null? req.getAnnoBilancio().intValue() : 0;
		checkImportoCoerente(previsioneImpegnatoAccertato.getImportoPrevAnno1(), String.valueOf(annoBilancio));
		checkImportoCoerente(previsioneImpegnatoAccertato.getImportoPrevAnno2(), String.valueOf(annoBilancio + 1 ));
		checkImportoCoerente(previsioneImpegnatoAccertato.getImportoPrevAnno3(), String.valueOf(annoBilancio + 2 ));
		
	}

	
	private void checkImportoCoerente(BigDecimal importo, String nomeCampo) {
		if(importo != null && importo.signum() <0) {
			throw new BusinessException(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(nomeCampo, "non possono essere negativi."));
		}
	}

}
