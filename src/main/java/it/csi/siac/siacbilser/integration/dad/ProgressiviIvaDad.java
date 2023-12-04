/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.ProgressiviIvaDao;
import it.csi.siac.siacbilser.integration.dao.SiacTIvaRegistroTotaleRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTPeriodoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaRegistroTotale;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.ProgressiviIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

/**
 * Data access delegate di un ProgressiviIva.
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ProgressiviIvaDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private ProgressiviIvaDao progressiviIvaDao;
	
	@Autowired
	private SiacTIvaRegistroTotaleRepository siacTIvaRegistroTotaleRepository;
	
	@Autowired
	private SiacTPeriodoRepository siacTPeriodoRepository;
	
	/**
	 * Ricerca il progressivo per uid.
	 * 
	 * @param uid l'uid da ricercare
	 * 
	 * @return il progressivo corrispondente all'uid, se presente
	 */
	public ProgressiviIva findProgressiviIvaByUid(Integer uid) {
		SiacTIvaRegistroTotale siacTIvaRegistroTotale = siacTIvaRegistroTotaleRepository.findOne(uid);
		
		return mapNotNull(siacTIvaRegistroTotale, ProgressiviIva.class, BilMapId.SiacTIvaRegistroTotale_ProgressiviIva);// TODO: mettere a posto
	}
	
	/**
	 * Ricerca il progressivo per uid.
	 * 
	 * @param registroIva il registro per cui ricercare
	 * @param aliquotaIva l'aliquota per cui ricercare
	 * @param periodo     il periodo per cui ricercare
	 * @param anno        l'anno per cui ricercare
	 * @param ente        l'ente per cui ricercare
	 * 
	 * @return il progressivo corrispondente ai parametri di input, se presente
	 */
	public ProgressiviIva findProgressiviIvaByRegistroAndAliquotaIvaAndPeriodoAndAnnoAndEnte(RegistroIva registroIva, AliquotaIva aliquotaIva,
			Periodo periodo, Integer anno) {
		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndPeriodoTipoAndEnteProprietario(anno.toString(), periodo.getCodice(), ente.getUid());
		// Se non c'è un periodo corrispondente, restituisco null
		if(siacTPeriodo == null) {
			throw new IllegalStateException("Nessun periodo di tipo "+periodo.getCodice()+" trovato per l'anno "+anno.toString() +" per l'ente"+ente.getUid());
		}
		
		SiacTIvaRegistroTotale siacTIvaRegistroTotale = siacTIvaRegistroTotaleRepository.findByRegistroAndAliquotaIvaAndPeriodoAndEnte(
				registroIva.getUid(), aliquotaIva.getUid(), siacTPeriodo.getUid(), ente.getUid());
		
		return mapNotNull(siacTIvaRegistroTotale, ProgressiviIva.class, BilMapId.SiacTIvaRegistroTotale_ProgressiviIva);
	}
	
	/**
	 * Inserisce il ProgressiviIva.
	 * 
	 * @param progressiviIva il progressivo da inserire
	 */
	public void inserisciProgressiviIva(ProgressiviIva progressiviIva) {
		SiacTIvaRegistroTotale siacTIvaRegistroTotale = buildSiacTIvaRegistroTotale(progressiviIva);
		progressiviIvaDao.create(siacTIvaRegistroTotale);
		progressiviIva.setUid(siacTIvaRegistroTotale.getUid());
	}
	
	/**
	 * Aggiorna il ProgressiviIva.
	 * 
	 * @param progressiviIva il progressivo da aggiornare
	 */
	public void aggiornaProgressiviIva(ProgressiviIva progressiviIva) {
		SiacTIvaRegistroTotale siacTIvaRegistroTotale = buildSiacTIvaRegistroTotale(progressiviIva);
		progressiviIvaDao.update(siacTIvaRegistroTotale);
		progressiviIva.setUid(siacTIvaRegistroTotale.getUid());
	}

	/**
	 * Costruisce un siacTIvaResitroTotale a partire dal ProgressiviIva.
	 * 
	 * @param progressiviIva il progressivo da convertire
	 * 
	 * @return l'entity corrispondente all'input
	 */
	private SiacTIvaRegistroTotale buildSiacTIvaRegistroTotale(ProgressiviIva progressiviIva) {
		SiacTIvaRegistroTotale siacTIvaRegistroTotale = new SiacTIvaRegistroTotale();
		siacTIvaRegistroTotale.setLoginOperazione(loginOperazione);
		progressiviIva.setLoginOperazione(loginOperazione);
		map(progressiviIva, siacTIvaRegistroTotale, BilMapId.SiacTIvaRegistroTotale_ProgressiviIva);
		return siacTIvaRegistroTotale;
	}
	
	
//	public List<BigDecimal> calcolaTotaleImponibileEImpostaAliquotaByGruppoAttivitaIvaAndTipoRegistroIva(Ente ente, GruppoAttivitaIva gruppoAttivitaIva, TipoRegistroIva tipoRegistroIva, AliquotaIva aliquotaIva) {
//		final String methodName = "calcolaTotaleImponibileEImpostaAliquotaByGruppoAttivitaIvaAndTipoRegistroIva";
//		log.debug(methodName, "ente: "+ente.getUid()+ " gruppoAttivitaIva: "+ gruppoAttivitaIva.getUid() + " tipoRegistroIva: "+ tipoRegistroIva.getCodice() + " aliquotaIva: "+ aliquotaIva.getUid());
//		
//		Object[] n = siacTIvaRegistroTotaleRepository.calcolaTotaliByIvaGruppoAndIvaTipoAndIvaAliquota(ente.getUid(), gruppoAttivitaIva.getUid(), tipoRegistroIva.getCodice(),aliquotaIva.getUid());		
//				
//		try {
//			log.debug(methodName, "Importi ottenuti: impDef: " + n[0] + " ivaDef: " + n[1] + " impProv: " + n[2] + " ivaProv: " + n[3]);
//		} catch (RuntimeException re) {
//			log.info(methodName, "Importi ottenuti: null");
//		}
//		
//		return Arrays.asList((BigDecimal)n[0], (BigDecimal)n[1], (BigDecimal)n[2], (BigDecimal)n[3]);
//
//		
//	}
	
	public List<BigDecimal> calcolaTotaleImponibileEImpostaAliquotaByGruppoAttivitaIvaAndTipoRegistroIvaAndAnno(Ente ente, GruppoAttivitaIva gruppoAttivitaIva, TipoRegistroIva tipoRegistroIva, AliquotaIva aliquotaIva, String anno) {
		final String methodName = "calcolaTotaleImponibileEImpostaAliquotaByGruppoAttivitaIvaAndTipoRegistroIvaAndAnno";
		log.debug(methodName, "ente: "+ente.getUid()+ " gruppoAttivitaIva: "+ gruppoAttivitaIva.getUid() + " tipoRegistroIva: "+ tipoRegistroIva.getCodice() + " aliquotaIva: "+ aliquotaIva.getUid()+ " anno : "+ anno);
		
		List<Object[]> page = siacTIvaRegistroTotaleRepository.calcolaTotaliByIvaGruppoAndIvaTipoAndIvaAliquotaAndAnno(ente.getUid(), gruppoAttivitaIva.getUid(), tipoRegistroIva.getCodice(),aliquotaIva.getUid(),anno, new PageRequest(0,1));

		//SIAC-3501
		if(page == null || page.isEmpty()){
			log.debug(methodName, "Non sono stati ottenuti importi per l'ente: " +ente.getUid()+ " gruppoAttivitaIva: "+ gruppoAttivitaIva.getUid() + " tipoRegistroIva: "+ tipoRegistroIva.getCodice() + " aliquotaIva: "+ aliquotaIva.getUid()+ " anno : "+ anno );
			return Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO , BigDecimal.ZERO, BigDecimal.ZERO);
		}
		
		Object[] n = page.get(0);
		log.debug(methodName, "Importi ottenuti: impDef: " + n[0] + " ivaDef: " + n[1] + " impProv: " + n[2] + " ivaProv: " + n[3]);		
		
		return Arrays.asList((BigDecimal)n[0], (BigDecimal)n[1], (BigDecimal)n[2], (BigDecimal)n[3]);

	}
	
}
