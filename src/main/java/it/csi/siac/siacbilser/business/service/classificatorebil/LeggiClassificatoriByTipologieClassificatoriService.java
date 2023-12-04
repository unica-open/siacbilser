/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificatorebil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatori;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatoriResponse;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * 
 * Ricerca l'elenco dei classificatori  di un certo tipo
 * 
 * @author Elisa Chiari
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiClassificatoriByTipologieClassificatoriService extends CheckedAccountBaseService<LeggiClassificatoriByTipologieClassificatori, LeggiClassificatoriByTipologieClassificatoriResponse> {

	
	/** The classificatori dad. */
	@Autowired
	private ClassificatoriDad classificatoriDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getListaTipologieClassificatori(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipologia"), false);
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"), false);
		checkCondition(req.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public LeggiClassificatoriByTipologieClassificatoriResponse executeService(LeggiClassificatoriByTipologieClassificatori serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		List<Codifica> codifiche = classificatoriDad.findListByEnteTipoClassificatoreAndAnno(req.getEnte(), req.getListaTipologieClassificatori(), req.getBilancio().getAnno());
		if(codifiche == null || codifiche.isEmpty()){
			
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Classificatori",  "classificatori per anno: " + req.getBilancio().getAnno() + "e tipologia " + componiStringaTipologieRichieste(req.getListaTipologieClassificatori())));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		res.setCodifiche(codifiche);
	}

	private String componiStringaTipologieRichieste(List<TipologiaClassificatore> listaTipologieClassificatori) {
		StringBuilder sb = new StringBuilder();
		for (TipologiaClassificatore tc : listaTipologieClassificatori) {
			sb.append(tc.name()).append(", ");
		}
		return sb.toString();
	}

}
