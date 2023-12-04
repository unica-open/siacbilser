/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacTModpagRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTAbi;
import it.csi.siac.siacbilser.integration.entity.SiacTCab;
import it.csi.siac.siacbilser.integration.entity.SiacTModpag;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.ContoCorrente;

// TODO: Auto-generated Javadoc
/**
 * The Class ContoCorrenteDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ContoCorrenteDad extends BaseDadImpl {
	
	/** The siac t modpag repository. */
	@Autowired
	private SiacTModpagRepository siacTModpagRepository;
	
	private Ente ente;
	
	/**
	 * Ricerca conti correnti by ente.
	 *
	 * @param ente the ente
	 * @return the list
	 */
	public List<ContoCorrente> ricercaContiCorrentiByEnte(Ente ente) {
		Integer enteId = (ente == null) ? null : ente.getUid(); 
		
		List<SiacTModpag> elencoSiacTModpag = siacTModpagRepository.findByEnteProprietario(enteId);
		if(elencoSiacTModpag == null) {
			return new ArrayList<ContoCorrente>();
		}
				
		List<ContoCorrente> elencoContiCorrenti = convertiLista(elencoSiacTModpag, ContoCorrente.class, BilMapId.SiacTModpag_ContoCorrente);
				
		return elencoContiCorrenti;
	}
	
	
	public boolean verificaValiditaAbi(String abiCode) {
		if(abiCode == null || StringUtils.isBlank(abiCode)){
			return false;
		}
		List<SiacTAbi> siacTAbis = siacTModpagRepository.findAbiByCodice(abiCode, ente.getUid());
		return siacTAbis != null && !siacTAbis.isEmpty();
	}
	
	public boolean verificaValiditaCab(String cabAbi, String cabCode) {
		if(cabCode == null || StringUtils.isBlank(cabCode)){
			return false;
		}
		List<SiacTCab> siacTCabs = siacTModpagRepository.findCabByCodice(cabAbi, cabCode, ente.getUid());
		return siacTCabs != null && !siacTCabs.isEmpty();
	}


	/**
	 * @return the ente
	 */
	public Ente getEnte() {
		return ente;
	}


	/**
	 * @param ente the ente to set
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
	}
	
	
	
}
