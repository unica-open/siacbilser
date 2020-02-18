/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacDEventoRepository;
import it.csi.siac.siacbilser.integration.dao.cespiti.SiacDCespitiBeneTipoRepository;
import it.csi.siac.siacbilser.integration.dao.cespiti.TipoBeneCespiteDao;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiBeneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;

// TODO: Auto-generated Javadoc
/**
 * Classe di DAD per il Tipo Bene.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class TipoBeneCespiteDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private TipoBeneCespiteDao tipoBeneCespiteDao;

	@Autowired
	private SiacDCespitiBeneTipoRepository siacDCespitiBeneTipoRepository;
	@Autowired
	private SiacDEventoRepository siacDEventoRepository;
	
	/**
	 * Inserisci tipo bene cespite.
	 *
	 * @param tipoBeneCespite the tipo bene cespite
	 * @return the tipo bene cespite
	 */
	public TipoBeneCespite inserisciTipoBeneCespite(TipoBeneCespite tipoBeneCespite){
		tipoBeneCespite.setEnte(ente);
		tipoBeneCespite.setLoginOperazione(loginOperazione);	
		SiacDCespitiBeneTipo siacDCespitiBeneTipo = buildSiacDCespitiBeneTipo(tipoBeneCespite);		
		tipoBeneCespiteDao.create(siacDCespitiBeneTipo);
		tipoBeneCespite.setUid(siacDCespitiBeneTipo.getUid());
		return tipoBeneCespite;
	}
	
	/**
	 * Aggiorna tipo bene cespite.
	 *
	 * @param tipoBeneCespite the tipo bene cespite
	 * @return the tipo bene cespite
	 */
	public TipoBeneCespite aggiornaTipoBeneCespite(TipoBeneCespite tipoBeneCespite){
		tipoBeneCespite.setEnte(ente);
		tipoBeneCespite.setLoginOperazione(loginOperazione);	
		SiacDCespitiBeneTipo siacDCespitiBeneTipo = buildSiacDCespitiBeneTipo(tipoBeneCespite);		
		tipoBeneCespiteDao.update(siacDCespitiBeneTipo);
		return tipoBeneCespite;
	}

	
	/**
	 * Annulla tipo bene cespite.
	 *
	 * @param tipoBeneCespite the tipo bene cespite
	 * @param dataAnnullamento the data annullamento
	 * @return the tipo bene cespite
	 */
	public TipoBeneCespite annullaTipoBeneCespite(TipoBeneCespite tipoBeneCespite ,Date dataAnnullamento){
		Date now = new Date();		
		tipoBeneCespite.setEnte(ente);
		tipoBeneCespite.setLoginOperazione(loginOperazione);	
		SiacDCespitiBeneTipo siacDCespitiBeneTipo = siacDCespitiBeneTipoRepository.findOne(tipoBeneCespite.getUid());
		siacDCespitiBeneTipo.setLoginOperazione(loginOperazione);	
		siacDCespitiBeneTipo.setDataModifica(now);			
		siacDCespitiBeneTipo.setDataFineValidita(dataAnnullamento);
		siacDCespitiBeneTipoRepository.saveAndFlush(siacDCespitiBeneTipo);
		return tipoBeneCespite;
	}
	
	
	/**
	 * Ricerca sintetica tipo bene cespite.
	 *
	 * @param cespitiBeneTipo the cespiti bene tipo
	 * @param contoPatrimoniale the conto patrimoniale
	 * @param categoriaCespiti the categoria cespiti
	 * @param listaTipoBeneCespiteModelDetail the lista tipo bene cespite model detail
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<TipoBeneCespite> ricercaSinteticaTipoBeneCespite(TipoBeneCespite cespitiBeneTipo, Conto contoPatrimoniale,CategoriaCespiti categoriaCespiti,TipoBeneCespiteModelDetail[] listaTipoBeneCespiteModelDetail,ParametriPaginazione parametriPaginazione ){

		
		Page<SiacDCespitiBeneTipo> listSiacDCespitiBeneTipo = tipoBeneCespiteDao.ricercaSinteticaTipoBeneCespite(
				ente.getUid(),
				cespitiBeneTipo != null ? cespitiBeneTipo.getCodice() : null,
				cespitiBeneTipo != null ? cespitiBeneTipo.getDescrizione() : null,
				mapToUidIfNotZero(categoriaCespiti),
				categoriaCespiti != null ? categoriaCespiti.getCodice() : null,
				mapToUidIfNotZero(contoPatrimoniale),
				contoPatrimoniale != null ? contoPatrimoniale.getCodice() : null,
				cespitiBeneTipo.getDataInizioValiditaFiltro(),
				toPageable(parametriPaginazione)
		);
		
		TipoBeneCespite t = new TipoBeneCespite();
		t.setDataInizioValiditaFiltro(cespitiBeneTipo.getDataInizioValiditaFiltro());
		
		return toListaPaginata(listSiacDCespitiBeneTipo, t, CespMapId.SiacDCespitiBeneTipo_TipoBeneCespite_ModelDetail, listaTipoBeneCespiteModelDetail);
	}

	/**
	 * Find dettaglio tipo bene cespite by id.
	 *
	 * @param tipoBeneCespite the tipo bene cespite
	 * @param modelDetails the model details
	 * @return the tipo bene cespite
	 */
	public TipoBeneCespite findDettaglioTipoBeneCespiteById(TipoBeneCespite tipoBeneCespite,TipoBeneCespiteModelDetail[] modelDetails) {
		//Page<SiacDCespitiBeneTipo> listSiacDCespitiBeneTipo = tipoBeneCespiteDao.findDettaglioTipoBeneCespiteById(tipoBeneCespite.getUid() );
		final String methodName = "findAllegatoAttoById";	
		int uid = tipoBeneCespite.getUid();
		log.debug(methodName, "uid: "+ uid);
		SiacDCespitiBeneTipo siacDCespitiBeneTipo = tipoBeneCespiteDao.findById(tipoBeneCespite.getUid());
		
		if(siacDCespitiBeneTipo == null) {
			log.warn(methodName, "Impossibile trovare il Tipo Bene cesite con id: " + uid);
		}
		
		TipoBeneCespite t = new TipoBeneCespite();
		t.setDataInizioValiditaFiltro(tipoBeneCespite.getDataInizioValiditaFiltro());
	
		if(modelDetails == null) {
			mapNotNull(siacDCespitiBeneTipo, t, CespMapId.SiacDCespitiBeneTipo_TipoBeneCespite);
			return t;
		}
		return  mapNotNull(siacDCespitiBeneTipo, t, CespMapId.SiacDCespitiBeneTipo_TipoBeneCespite_ModelDetail, Converters.byModelDetails(modelDetails));

	}
	
	private SiacDCespitiBeneTipo buildSiacDCespitiBeneTipo(TipoBeneCespite tipoBeneCespite) {
		SiacDCespitiBeneTipo siacDCespitiBeneTipo = new SiacDCespitiBeneTipo();
		siacDCespitiBeneTipo.setLoginOperazione(loginOperazione);
		map(tipoBeneCespite,siacDCespitiBeneTipo,CespMapId.SiacDCespitiBeneTipo_TipoBeneCespite);
		return siacDCespitiBeneTipo;	
	}
	
	public TipoBeneCespite eliminaTipoBeneCespite(TipoBeneCespite tipoBeneCespite) {
		SiacDCespitiBeneTipo tipoBeneCespiteEliminato = tipoBeneCespiteDao.delete(tipoBeneCespite.getUid(), loginOperazione);
		return mapNotNull(tipoBeneCespiteEliminato , TipoBeneCespite.class , CespMapId.SiacDCespitiBeneTipo_TipoBeneCespite_ModelDetail);			
	}
	
	public Long countTipoBeneByCategoria(CategoriaCespiti categoriaCespiti){
		Long count = siacDCespitiBeneTipoRepository.countCespitiBeneTipoByUidCategoria(categoriaCespiti.getUid(), categoriaCespiti.getDataInizioValiditaFiltro());
		return count;
	}
	
	/**
	 * Count tipo bene by categoria.
	 *
	 * @param codiceTipoBene the codice tipo bene
	 * @return the tipo bene cespite
	 */
	public TipoBeneCespite findByCodice(String codiceTipoBene){
		 SiacDCespitiBeneTipo siacDCespitiBeneTipo = siacDCespitiBeneTipoRepository.findSiacDCespitiBeneTipoByCodiceEEnte(codiceTipoBene, ente.getUid());
		 return mapNotNull(siacDCespitiBeneTipo, TipoBeneCespite.class, CespMapId.SiacDCespitiBeneTipo_TipoBeneCespite_ModelDetail);
	}
	
	public Evento findEventoByUid(Evento evento) {
		SiacDEvento siacDEvento = siacDEventoRepository.findOne(evento.getUid());
		return mapNotNull(siacDEvento, Evento.class, GenMapId.SiacDEvento_Evento);
	}


}
