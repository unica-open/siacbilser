/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.CodicePCCDao;
import it.csi.siac.siacbilser.integration.dao.CodiceUfficioDestinatarioPCCDao;
import it.csi.siac.siacbilser.integration.entity.SiacDPccCodice;
import it.csi.siac.siacbilser.integration.entity.SiacDPccUfficio;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.CodicePCC;
import it.csi.siac.siacfin2ser.model.CodiceUfficioDestinatarioPCC;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CodifichePCCDad extends BaseDadImpl {
	
	private Ente ente;
	
	@Autowired
	private CodicePCCDao codicePCCDao;
	@Autowired
	private CodiceUfficioDestinatarioPCCDao ufficioPCCDao;
	
	/**
	 * @param ente the ente to set
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	public List<CodicePCC> findCodiciPCCByEnteAndStrutturaAmministrativoContabile(Iterable<StrutturaAmministrativoContabile> struttureAmministrativoContabili, CodiceUfficioDestinatarioPCC codiceUfficioDestinatarioPCC) {
		final String methodName = "findCodiciPCCByEnteAndStrutturaAmministrativoContabile";
		Set<Integer> classifIds = new HashSet<Integer>();
		for(StrutturaAmministrativoContabile sac : struttureAmministrativoContabili) {
			classifIds.add(sac.getUid());
		}
		log.debug(methodName, "Numero di strutture amministrative per cui filtrare: " + classifIds.size() + " => " + classifIds);
		
		List<SiacDPccCodice> siacDPccCodices = codicePCCDao.findByEnteProprietarioAndStruttureAmministrativoContabiliAndCodiceUfficioPCC(ente.getUid(), classifIds, codiceUfficioDestinatarioPCC != null? codiceUfficioDestinatarioPCC.getUid() : null);
		
		return convertiLista(siacDPccCodices, CodicePCC.class, BilMapId.SiacDPccCodice_CodicePCC);
	}
	
	public List<CodiceUfficioDestinatarioPCC> findCodiciUfficiDestinatariPCCByEnteAndStrutturaAmministrativoContabile(Iterable<StrutturaAmministrativoContabile> struttureAmministrativoContabili) {
		final String methodName = "findCodiciUfficiDestinatariPCCByEnteAndStrutturaAmministrativoContabile";
		Set<Integer> classifIds = new HashSet<Integer>();
		for(StrutturaAmministrativoContabile sac : struttureAmministrativoContabili) {
			classifIds.add(sac.getUid());
		}
		log.debug(methodName, "Numero di strutture amministrative per cui filtrare: " + classifIds.size() + " => " + classifIds);
		
		List<SiacDPccUfficio> siacDPccUfficios = ufficioPCCDao.findByEnteProprietarioAndStruttureAmministrativoContabili(ente.getUid(), classifIds);
		
		return convertiLista(siacDPccUfficios, CodiceUfficioDestinatarioPCC.class, BilMapId.SiacDPccUfficio_CodiceUfficioDestinatarioPCC);
	}

}
