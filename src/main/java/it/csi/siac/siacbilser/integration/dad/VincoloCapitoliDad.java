/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacRVincoloBilElemRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTVincoloRepository;
import it.csi.siac.siacbilser.integration.dao.VincoloCapitoliDao;
import it.csi.siac.siacbilser.integration.entity.SiacDVincoloStato;
import it.csi.siac.siacbilser.integration.entity.SiacRVincoloBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacRVincoloStato;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTVincolo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVincoloStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVincoloTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.StatoOperativo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class VincoloCapitoliDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class VincoloCapitoliDad extends ExtendedBaseDadImpl {
	
	/** The siac t vincolo repository. */
	@Autowired
	private SiacTVincoloRepository siacTVincoloRepository;
	
	/** The siac r vincolo bil elem repository. */
	@Autowired
	private SiacRVincoloBilElemRepository siacRVincoloBilElemRepository;
	
	/** The vincolo capitoli dao. */
	@Autowired
	private VincoloCapitoliDao vincoloCapitoliDao;
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Find vincolo by id.
	 *
	 * @param uid the uid
	 * @return the vincolo
	 */
	public Vincolo findVincoloById(Integer uid) {
		final String methodName = "findOne";		
		log.debug(methodName, "uid: "+ uid);
		SiacTVincolo siacTVincolo = siacTVincoloRepository.findOne(uid);
		if(siacTVincolo == null) {
			throw new IllegalArgumentException("Impossibile trovare il vincolo con id: " + uid);
		}
		return  map(siacTVincolo, Vincolo.class, BilMapId.SiacTVincolo_Vincolo);
	}

	/**
	 * Find vincolo capitoli by id.
	 *
	 * @param uid the uid
	 * @return the vincolo capitoli
	 */
	public VincoloCapitoli findVincoloCapitoliById(Integer uid, Bilancio bilancio) {
		final String methodName = "findOne";		
		log.debug(methodName, "uid: "+ uid);
		SiacTVincolo siacTVincolo = siacTVincoloRepository.findOne(uid);
		VincoloCapitoli vc = new VincoloCapitoli();
		vc.setBilancio(bilancio);
		map(siacTVincolo, vc, BilMapId.SiacTVincolo_VincoloCapitoli);
		return vc;
	}	
	
	/**
	 * Inserisci vincolo.
	 *
	 * @param vincolo the vincolo
	 */
	public void inserisciVincolo(Vincolo vincolo) {		
		SiacTVincolo siacTVincolo = buildSiacTVincolo(vincolo);	
//		siacTVincolo.setSiacTPeriodo(getSiacTPeriodoAssociatoABilancio());
		vincoloCapitoliDao.create(siacTVincolo);
		vincolo.setUid(siacTVincolo.getUid());
	}	
	
	/**
	 * Aggiorna vincolo.
	 *
	 * @param vincolo the vincolo
	 */
	public void aggiornaVincolo(Vincolo vincolo) {		
		SiacTVincolo siacTVincolo = buildSiacTVincolo(vincolo);	
		vincoloCapitoliDao.update(siacTVincolo);
		vincolo.setUid(siacTVincolo.getUid());
	}	

	/**
	 * Builds the siac t vincolo.
	 *
	 * @param vincolo the vincolo
	 * @return the siac t vincolo
	 */
	private SiacTVincolo buildSiacTVincolo(Vincolo vincolo) {
		SiacTVincolo siacTVincolo = new SiacTVincolo();		
		siacTVincolo.setLoginOperazione(loginOperazione);
		vincolo.setLoginOperazione(loginOperazione);
		map(vincolo, siacTVincolo, BilMapId.SiacTVincolo_Vincolo);
		return siacTVincolo;
	}

//	/**
//	 * Gets the siac t periodo associato a bilancio.
//	 *
//	 * @return the siac t periodo associato a bilancio
//	 */
//	private SiacTPeriodo getSiacTPeriodoAssociatoABilancio() {		
//		SiacTBil siacTBil = siacTBilRepository.findOne(bilancio.getUid());
//		log.debug("getSiacTPeriodoAssociatoABilancio", "returning "+siacTBil.getSiacTPeriodo().getUid());
//		return siacTBil.getSiacTPeriodo();
//	}
	
	/**
	 * Associa un capitolo ad un vincolo.
	 *
	 * @param vincolo the vincolo
	 * @param capitolo the capitolo
	 */
	public void associaCapitoloAlVincolo(Vincolo vincolo, Capitolo<?, ?> capitolo) {
		
		SiacRVincoloBilElem siacRVincoloBilElem = new SiacRVincoloBilElem();
		
		SiacTVincolo siacTVincolo = new SiacTVincolo();
		siacTVincolo.setVincoloId(vincolo.getUid());
		siacRVincoloBilElem.setSiacTVincolo(siacTVincolo);
		
		SiacTBilElem siacTBilElem = new SiacTBilElem();
		siacTBilElem.setElemId(capitolo.getUid());
		siacRVincoloBilElem.setSiacTBilElem(siacTBilElem);
		
		
		siacRVincoloBilElem.setDataModificaInserimento(new Date());
		siacRVincoloBilElem.setLoginOperazione(loginOperazione);
		siacRVincoloBilElem.setSiacTEnteProprietario(siacTEnteProprietario);
		siacRVincoloBilElemRepository.saveAndFlush(siacRVincoloBilElem);
		
	}
	
	
	/**
	 * Elimina l'asscociazione di un capitolo ad un vincolo .
	 *
	 * @param vincolo the vincolo
	 * @param capitolo the capitolo
	 */
	public void scollegaCapitoloAlVincolo(Vincolo vincolo, Capitolo<?, ?> capitolo) {
		siacRVincoloBilElemRepository.scollegaCapitoloAlVincolo(vincolo.getUid(), capitolo.getUid());
	}
	
	/**
	 * Find vincolo by capitolo associato.
	 *
	 * @param capitolo the capitolo
	 */
	public void findVincoloByCapitoloAssociato(Capitolo<?, ?> capitolo) {
		//siacTVincoloRepository.findVincoloValidoByCapitoloAssociatoValido(capitolo);
		
	}
	
	/**
	 * Aggiorna lo stato del vincolo con quello passato a parametro.
	 *
	 * @param vincolo the vincolo
	 * @param statoOperativo the stato operativo
	 */
	public void aggiornaStatoOperativoVincolo(Vincolo vincolo, StatoOperativo statoOperativo) {
		SiacTVincolo siacTVincolo = siacTVincoloRepository.findOne(vincolo.getUid());
		List<SiacRVincoloStato> siacRVincoloStatos = siacTVincolo.getSiacRVincoloStatos();
		Date dataCancellazione = new Date();
		for (SiacRVincoloStato siacRVincoloStato : siacRVincoloStatos) {
			if(siacRVincoloStato.getDataCancellazione()==null){
				siacRVincoloStato.setDataCancellazione(dataCancellazione);
				siacRVincoloStato.setDataFineValidita(dataCancellazione);
			}
		}
		SiacRVincoloStato siacRVincoloStato = new SiacRVincoloStato();
		//Integer enteId = ente.getUid();
		Integer enteId = siacTVincolo.getSiacTEnteProprietario().getUid();
		siacRVincoloStato.setSiacDVincoloStato(eef.getEntity(SiacDVincoloStatoEnum.byStatoOperativo(statoOperativo),  enteId, SiacDVincoloStato.class));
		siacRVincoloStato.setDataModificaInserimento(new Date());
		siacRVincoloStato.setSiacTEnteProprietario(siacTVincolo.getSiacTEnteProprietario() /*siacTEnteProprietario*/);
		siacRVincoloStato.setSiacTVincolo(siacTVincolo);
		siacRVincoloStato.setLoginOperazione(loginOperazione);
		siacRVincoloStatos.add(siacRVincoloStato);
		
	}

	
	/**
	 * Ricerca sintetica vincolo capitoli.
	 *
	 * @param vincolo the vincolo
	 * @param capitolo the capitolo
	 * @param tipiCapitolo the tipi capitolo
	 * @param bilancio the bilancio
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<VincoloCapitoli> ricercaSinteticaVincoloCapitoli(Vincolo vincolo, Capitolo<?, ?> capitolo, List<TipoCapitolo> tipiCapitolo,Bilancio bilancio, ParametriPaginazione parametriPaginazione) {
		
		Page<SiacTVincolo> lista = vincoloCapitoliDao.ricercaSinteticaVincoloCapitoli(ente.getUid(),
				vincolo.getCodice(),
				SiacDVincoloTipoEnum.byTipoVincoloCapitoliEvenNull(vincolo.getTipoVincoloCapitoli()),
				vincolo.getDescrizione(),
				vincolo.getFlagTrasferimentiVincolati(),
				mapToUidIfNotZero(capitolo),
				capitolo != null ? SiacDBilElemTipoEnum.byTipoCapitoloEvenNull(capitolo.getTipoCapitolo()) : null,
				SiacDBilElemTipoEnum.toList(tipiCapitolo),
				capitolo != null ? mapToString(capitolo.getAnnoCapitolo()) : null, //equivalente a BilancioAnno
				capitolo != null ? mapToString(capitolo.getBilancioAnno()) : null, //equivalente ad annoCapitolo
				capitolo != null ? mapToString(capitolo.getNumeroCapitolo()) : null,
				capitolo != null ? mapToString(capitolo.getNumeroArticolo()) : null,
				capitolo != null ? mapToString(capitolo.getNumeroUEB()) : null,
				mapToUidIfNotZero(vincolo.getGenereVincolo()),
				bilancio != null && bilancio.getAnno() != 0?mapToString(bilancio.getAnno()) : null,
				toPageable(parametriPaginazione));
		
		return toListaPaginata(lista,VincoloCapitoli.class, BilMapId.SiacTVincolo_VincoloCapitoli);
	}

}
