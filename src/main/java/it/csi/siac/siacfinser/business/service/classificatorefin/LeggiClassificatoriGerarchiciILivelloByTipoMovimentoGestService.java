/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.classificatorefin;

import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGerarchiciILivelloByTipoMovimentoGest;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGerarchiciILivelloByTipoMovimentoGestResponse;
import it.csi.siac.siacfinser.integration.dad.ClassificatoreFinDad;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiClassificatoriGerarchiciILivelloByTipoMovimentoGestService
		extends
		AbstractBaseService<LeggiClassificatoriGerarchiciILivelloByTipoMovimentoGest, LeggiClassificatoriGerarchiciILivelloByTipoMovimentoGestResponse> {

	@Autowired
	private ClassificatoreFinDad classificatoreFinDad;

	@Autowired
	protected Mapper dozerBeanMapper;

	@Override
	protected void init() {
	}

	
//	@Override
//	@Transactional(readOnly=true)
//	public LeggiClassificatoriGerarchiciILivelloByTipoMovimentoGestResponse executeService(LeggiClassificatoriGerarchiciILivelloByTipoMovimentoGest serviceRequest) {
//		return super.executeService(serviceRequest);
//	}
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition(req.getAnno() > 0,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"), false);
		checkCondition(req.getIdEnteProprietario() > 0,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO
						.getErrore("enteProprietarioId"), false);
	}

	@Override
	public void execute() {
		String methodName = "LeggiClassificatoriGerarchiciILivelloByTipoMovimentoGestService - execute()";
				
		List<ClassificatoreGerarchico> listaClassificatoriGerarchiciILivello = classificatoreFinDad
				.findClassificatoriGerarchiciILivelloByTipoMovimentoGestione(
						req.getAnno(), req.getIdEnteProprietario(),
						req.getCodiceTipoMovimentoGestione());
		
		if (listaClassificatoriGerarchiciILivello == null || listaClassificatoriGerarchiciILivello.isEmpty()) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("classificatori", "anno: "+req.getAnno()+" idPadre: "+req.getCodiceTipoMovimentoGestione() ));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		for (ClassificatoreGerarchico c : listaClassificatoriGerarchiciILivello) {

			if (c != null && c.getTipoClassificatore() != null) {

				TipologiaClassificatore codice = TipologiaClassificatore
						.fromCodice(c.getTipoClassificatore().getCodice());

				if (codice == null) {
					continue;
				}

				switch (codice) {
				case MISSIONE:
					res.getClassificatoriMissione().add(
							dozerBeanMapper.map(c, Missione.class));
					break;
				// aggiungere gli altri....
				default:
					break;
				}
			}
		}
		
	}
}
