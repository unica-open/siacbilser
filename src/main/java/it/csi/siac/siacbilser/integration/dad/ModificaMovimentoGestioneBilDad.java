/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacTModificaBilRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsDetTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;


/**
 * The Class ModificaMovimentoGestioneBilDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ModificaMovimentoGestioneBilDad extends ExtendedBaseDadImpl  {
	
	/** The siac t movgest t repository. */
	@Autowired
	protected SiacTModificaBilRepository siacTModificaRepository;
	
	
	
	/**
	 * Estrai soggetto della modifica.
	 *
	 * @param modifica the modifica
	 * @return the soggetto
	 */
	public Soggetto estraiSoggettoDellaModifica(ModificaMovimentoGestione modifica) {
		final String methodName="estraiSoggettoDellaModifica";
		if(modifica == null) {
			log.info(methodName, "impossibile estrarre il soggetto da una modifica null");
			return null;
		}
		//modifica di soggetto: prendo il soggetto (N.B: non c'è un tipo di modifica che mi dica se e' importo o soggetto, quindi provo prima l'una e poi l'altra)
		List<SiacTSoggetto> soggettoModificaSoggettos = siacTModificaRepository.findSiacTSoggettoBySiacRMovgestTsSogMod(modifica.getUid(), ente.getUid());
		if(soggettoModificaSoggettos != null && !soggettoModificaSoggettos.isEmpty()) {
			Soggetto soggettoModificaSoggetto = mapNotNull(soggettoModificaSoggettos.get(0), Soggetto.class, BilMapId.SiacTSoggetto_Soggetto);
			log.info(methodName, "trovato soggetto [uid: " + (soggettoModificaSoggetto != null? soggettoModificaSoggetto.getUid()  : "null")+ " ] per la modifica [uid: " + modifica.getUid() + " ] .");
			return soggettoModificaSoggetto;
		}
		//modifica di importo
		List<SiacTSoggetto> soggettoModificaImportos = siacTModificaRepository.findSiacTSoggettoBySiacRMovgestTsSog(modifica.getUid(), ente.getUid());
		if(soggettoModificaImportos != null && !soggettoModificaImportos.isEmpty()) {
			Soggetto soggettoModificaImporto = mapNotNull(soggettoModificaImportos.get(0), Soggetto.class, BilMapId.SiacTSoggetto_Soggetto);
			log.info(methodName, "trovato soggetto [uid: " + (soggettoModificaImporto != null? soggettoModificaImporto.getUid()  : "null")+ " ] per la modifica [uid: " + modifica.getUid() + " ] .");
			return soggettoModificaImporto;
		}
		log.info(methodName, "Impossibile reperire il soggetto della modifica [uid: " + modifica.getUid() + "]. Potrebbe essere una modifica di classe soggetto oppure potrebero esserci dati sporchi sul db.");
		return null;
	}
	
	public BigDecimal estraiImportoAttualeModifica(ModificaMovimentoGestione modifica) {
		return siacTModificaRepository.findMovgestTsDetImportoModByModIdAndMovgestTsDetTipoCode(modifica.getUid(), SiacDMovgestTsDetTipoEnum.Attuale.getCodice());
	}

}
