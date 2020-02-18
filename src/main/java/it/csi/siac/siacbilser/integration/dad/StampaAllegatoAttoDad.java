/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.StampaAllegatoAttoDao;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegatoStampa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAllegatoStampaTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.AllegatoAttoStampa;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class StampaAllegatoAttoDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private StampaAllegatoAttoDao stampaAllegatoAttoDao;

	private Bilancio bilancio;
	
	
	/**
	 * Ricerca stampe allegato atto con parametri di ricerca il tipo stampa
	 * @param stampaAllegatoAtto la stampa allegato
	 * 
	 * @return la lista delle stampe allegato trovate
	 */
	public ListaPaginata<AllegatoAttoStampa> ricercaSinteticaStampaAllegatoAtto(AllegatoAttoStampa allegatoAttoStampa,ParametriPaginazione parametriPaginazione) {
		
		Page<SiacTAttoAllegatoStampa> listaSiacTAttoAllegatoStampa = stampaAllegatoAttoDao.ricercaSinteticaStampaAllegatoAtto(
			ente.getUid(),
			allegatoAttoStampa.getTipoStampa()!=null? SiacDAttoAllegatoStampaTipoEnum.byTipoStampa(allegatoAttoStampa.getTipoStampa()) : null,
			allegatoAttoStampa.getBilancio() != null? allegatoAttoStampa.getBilancio().getUid(): null,
			allegatoAttoStampa.getAllegatoAtto()!= null && allegatoAttoStampa.getAllegatoAtto().getAttoAmministrativo()!=null && allegatoAttoStampa.getAllegatoAtto().getAttoAmministrativo().getUid()!=0 ? allegatoAttoStampa.getAllegatoAtto().getAttoAmministrativo().getUid() : null,
			allegatoAttoStampa.getDataCreazione() != null? allegatoAttoStampa.getDataCreazione() : null,
			toPageable(parametriPaginazione));
		ListaPaginata<AllegatoAttoStampa> listaAllegatoAttoStampa = toListaPaginata(listaSiacTAttoAllegatoStampa, AllegatoAttoStampa.class, BilMapId.SiacTAttoAllegatoStampa_AllegatoAttoStampa);
				
		return listaAllegatoAttoStampa; 
	}
		
	public void inserisciAllegatoAttostampa(AllegatoAttoStampa allegatoAttoStampa) {
		log.logXmlTypeObject(allegatoAttoStampa, "allegato atto stampa");
		SiacTAttoAllegatoStampa siacTAttoAllegatoStampa = buildSiacTAttoAllegatoStampa(allegatoAttoStampa);
		stampaAllegatoAttoDao.create(siacTAttoAllegatoStampa);
		allegatoAttoStampa.setUid(siacTAttoAllegatoStampa.getUid());
	}
	
	private SiacTAttoAllegatoStampa buildSiacTAttoAllegatoStampa(AllegatoAttoStampa allegatoAttoStampa) {
		
		allegatoAttoStampa.setLoginOperazione(loginOperazione);
		
		SiacTAttoAllegatoStampa siacTAttoAllegatoStampa = new SiacTAttoAllegatoStampa();
		siacTAttoAllegatoStampa.setLoginOperazione(loginOperazione);
		
		map(allegatoAttoStampa, siacTAttoAllegatoStampa, BilMapId.SiacTAttoAllegatoStampa_AllegatoAttoStampa);
		return siacTAttoAllegatoStampa;
	}
	
	/**
	 * @return the bilancio
	 * */
	public Bilancio getBilancio() {
		return bilancio;
	}
	/**
	 * @param the bilancio the bilancio to set
	 * */
	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}

}
