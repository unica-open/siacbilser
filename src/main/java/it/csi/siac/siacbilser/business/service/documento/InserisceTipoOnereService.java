/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.integration.dad.NaturaOnereDad;
import it.csi.siac.siacbilser.integration.dad.TipoOnereDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceTipoOnereResponse;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaTipoOnereService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceTipoOnereService extends CheckedAccountBaseService<InserisceTipoOnere, InserisceTipoOnereResponse>{
	
	/** The tipo onere dad. */
	@Autowired
	private TipoOnereDad tipoOnereDad;
	
	@Autowired
	private NaturaOnereDad naturaOnereDad;
	
	private TipoOnere tipoOnere;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		
		checkNotNull(req.getTipoOnere(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo onere"));
		
		tipoOnere = req.getTipoOnere();
		tipoOnere.setEnte(ente);
		
		checkEntita(tipoOnere.getNaturaOnere(), "natura onere", false);
		checkCondition(tipoOnere.getCodice() != null && StringUtils.isNotBlank(tipoOnere.getCodice()),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo onere"), false);
		checkCondition(tipoOnere.getDescrizione() != null && StringUtils.isNotBlank(tipoOnere.getDescrizione())
				,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione tipo onere"), false);
		checkCondition(tipoOnere.getDescrizione().length()<=200,  ErroreCore.FORMATO_NON_VALIDO.getErrore("descrizione tipo onere",": lunghezza massima 200 caratteri"), false);
		checkCondition(tipoOnere.getAliquotaCaricoEnte() == null || tipoOnere.getAliquotaCaricoEnte().signum()>0, ErroreCore.FORMATO_NON_VALIDO.getErrore("aliquota a carico ente",": deve essere non negativo"), false);
		checkCondition(tipoOnere.getAliquotaCaricoSoggetto() == null || tipoOnere.getAliquotaCaricoSoggetto().signum()>0,  ErroreCore.FORMATO_NON_VALIDO.getErrore("aliquota a carico soggetto",": deve essere non negativo"), false);
		checkCondition(tipoOnere.getAliquotaCaricoEnte() == null
				|| tipoOnere.getAliquotaCaricoSoggetto() == null
				|| tipoOnere.getAliquotaCaricoSoggettoNotNull().add(tipoOnere.getAliquotaCaricoEnteNotNull()).subtract(BilUtilities.BIG_DECIMAL_ONE_HUNDRED).signum()<=0
			,  ErroreCore.FORMATO_NON_VALIDO.getErrore("aliquota a carico soggetto o aliquota a carico ente",": la somma delle aliquote deve essere minore o uguale a 100"), false);
	
	}
	
	@Override
	protected void init() {
		tipoOnereDad.setLoginOperazione(loginOperazione);
		tipoOnereDad.setEnte(ente);
		
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public InserisceTipoOnereResponse executeService(InserisceTipoOnere serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkSplitReverseSeNaturaCorrispondente();
		checkCodiceGiaEsistente();
		res.setTipoOnere(tipoOnereDad.inserisceTipoOnere(tipoOnere));
		
	}

	/**
	 * Controlla se i dati dello split/reverse sono stati popolati nel caso in cui la natura sia corrispondente
	 */
	private void checkSplitReverseSeNaturaCorrispondente() {
		NaturaOnere naturaOnere = naturaOnereDad.findNaturaOnereByUid(tipoOnere.getNaturaOnere().getUid());
		if(naturaOnere == null) {
			// Sarebbe bello NON usare l'uid
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Natura onere", "uid: " + tipoOnere.getNaturaOnere().getUid()));
		}
		
		// Se la natura ha codice ES (ESENZIONE) => il tipo deve essere Esenzione
		if("ES".equals(naturaOnere.getCodice()) && !TipoIvaSplitReverse.ESENZIONE.equals(tipoOnere.getTipoIvaSplitReverse())) {
			throw new BusinessException(ErroreCore.VALORE_NON_VALIDO.getErrore("tipo IVA split / reverse", ": se la natura onere e' ES deve essere ESENZIONE"));
		}
		if(naturaOnere.isSplitReverse() && tipoOnere.getTipoIvaSplitReverse() == null) {
			throw new BusinessException(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("tipo IVA split / reverse"));
		}
		
		tipoOnere.setNaturaOnere(naturaOnere);
	}

	private void checkCodiceGiaEsistente() {
		if(tipoOnereDad.findTipoOnereByCodice(tipoOnere.getCodice()) != null){
			throw new BusinessException(ErroreFin.INSERIMENTO_CAUSALE_NON_POSSIBILE.getErrore());
		}
	}

}
