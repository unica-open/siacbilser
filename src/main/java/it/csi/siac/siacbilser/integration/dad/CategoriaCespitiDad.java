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

import it.csi.siac.siacbilser.integration.dao.cespiti.CategoriaCespitiDao;
import it.csi.siac.siacbilser.integration.dao.cespiti.SiacDCespitiCategoriaRepository;
import it.csi.siac.siacbilser.integration.dao.cespiti.SiacRCespitiCategoriaAliquotaCalcoloTipoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacRCespitiCategoriaAliquotaCalcoloTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.CategoriaCespitiModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe di DAD per il Tipo Onere.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CategoriaCespitiDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private CategoriaCespitiDao categoriaCespitiDao;

	@Autowired
	private SiacDCespitiCategoriaRepository siacDCespitiCategoriaRepository;
	
	@Autowired
	private SiacRCespitiCategoriaAliquotaCalcoloTipoRepository siacRCespitiCategoriaAliquotaCalcoloTipoRepository;
	
	/**
	 * 
	 * @param categoriaCespiti
	 * @return
	 */
	public CategoriaCespiti inserisciCategoriaCespiti(CategoriaCespiti categoriaCespiti){		
		SiacDCespitiCategoria siacDCespitiCategoria = buildSiacDCespitiCategoria(categoriaCespiti);		
		categoriaCespitiDao.create(siacDCespitiCategoria);
		categoriaCespiti.setUid(siacDCespitiCategoria.getUid());
		return categoriaCespiti;
	}

	private SiacDCespitiCategoria buildSiacDCespitiCategoria(CategoriaCespiti categoriaCespiti) {
		SiacDCespitiCategoria siacDCespitiCategoria = new SiacDCespitiCategoria();		
		siacDCespitiCategoria.setLoginOperazione(loginOperazione);		
		categoriaCespiti.setLoginOperazione(loginOperazione);		
		categoriaCespiti.setEnte(ente);
		return mapNotNull(categoriaCespiti , SiacDCespitiCategoria.class , CespMapId.SiacDCespitiCategoria_CategoriaCespiti);	
	}


	public void aggiornaCategoriaCespiti(CategoriaCespiti categoriaCespiti){		
		SiacDCespitiCategoria siacDCespitiCategoria = buildSiacDCespitiCategoria(categoriaCespiti);		
		categoriaCespitiDao.update(siacDCespitiCategoria);
	}

	public CategoriaCespiti eliminaCategoriaCespiti(CategoriaCespiti categoriaCespiti){		
		SiacDCespitiCategoria attuale = categoriaCespitiDao.delete(categoriaCespiti.getUid(), loginOperazione);
		return mapNotNull(attuale , CategoriaCespiti.class , CespMapId.SiacDCespitiCategoria_CategoriaCespiti_ModelDetail);			
	}
	
	
	/**
	 * Ricerca sintetica categoria cespiti.
	 *
	 * @param categoriaCespiti the categoria cespiti
	 * @param escludiAnnullati 
	 * @param parametriPaginazione the parametri paginazione
	 * @param categoriaCespitiModelDetails 
	 * @return the lista paginata
	 */
	public ListaPaginata<CategoriaCespiti> ricercaSinteticaCategoriaCespiti(CategoriaCespiti categoriaCespiti,  Boolean escludiAnnullati, ParametriPaginazione parametriPaginazione, CategoriaCespitiModelDetail... categoriaCespitiModelDetails) {
		Ambito ambito = categoriaCespiti != null && categoriaCespiti.getAmbito() != null? categoriaCespiti.getAmbito() : Ambito.AMBITO_FIN;
		
		Page<SiacDCespitiCategoria> lista = categoriaCespitiDao.ricercaSinteticaCategoriaCespiti(
				ente.getUid(),
				categoriaCespiti != null ? categoriaCespiti.getCodice() : null,
				categoriaCespiti != null ? categoriaCespiti.getDescrizione()  : null,
				categoriaCespiti != null ? categoriaCespiti.getAliquotaAnnua()  : null,
				categoriaCespiti != null && categoriaCespiti.getCategoriaCalcoloTipoCespite() != null && categoriaCespiti.getCategoriaCalcoloTipoCespite().getUid() != 0? categoriaCespiti.getCategoriaCalcoloTipoCespite().getUid() : null,
				escludiAnnullati,
				SiacDAmbitoEnum.byAmbito(ambito).getCodice(),
				categoriaCespiti != null ? categoriaCespiti.getDataInizioValiditaFiltro()  : null,
				toPageable(parametriPaginazione));
		
		CategoriaCespiti c = new CategoriaCespiti();
		c.setDataInizioValiditaFiltro(categoriaCespiti.getDataInizioValiditaFiltro());
		if(categoriaCespitiModelDetails == null) {
			return toListaPaginata(lista, c, CespMapId.SiacDCespitiCategoria_CategoriaCespiti);
		}
		return toListaPaginata(lista,c, CespMapId.SiacDCespitiCategoria_CategoriaCespiti_ModelDetail, categoriaCespitiModelDetails);
		
	}
	
	/**
	 * Find categoria cespiti by codice.
	 *
	 * @param codice the codice
	 * @return the categoria cespiti
	 */
	public CategoriaCespiti findCategoriaCespitiByCodice(String codice) {
		SiacDCespitiCategoria siacDCespitiCategoria = siacDCespitiCategoriaRepository.findCategoriaCespitiByCodiceEEnte(codice, ente.getUid());
		return mapNotNull(siacDCespitiCategoria, CategoriaCespiti.class, CespMapId.SiacDCespitiCategoria_CategoriaCespiti_ModelDetail);
	}
	
	/**
	 * Find categoria cespiti by id.
	 *
	 * @param uid the uid
	 * @param inizioAnno the inizio anno
	 * @param modelDetails the model details
	 * @return the categoria cespiti
	 */
	public CategoriaCespiti findCategoriaCespitiById(Integer uid, Date inizioAnno, CategoriaCespitiModelDetail... modelDetails) {
		final String methodName = "findCategoriaCespitiById";		
		log.debug(methodName, "uid: "+ uid);
		SiacDCespitiCategoria siacDCespitiCategoria = categoriaCespitiDao.findById(uid);
		if(siacDCespitiCategoria == null) {
			log.warn(methodName, "Impossibile trovare il categoria cespiti con id: " + uid);
		}
		
		CategoriaCespiti c = new CategoriaCespiti();
		c.setDataInizioValiditaFiltro(inizioAnno);
		
		if(modelDetails == null) {
			mapNotNull(siacDCespitiCategoria, c, CespMapId.SiacDCespitiCategoria_CategoriaCespiti);
			return c;
		}
		return  mapNotNull(siacDCespitiCategoria, c, CespMapId.SiacDCespitiCategoria_CategoriaCespiti_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	
	public void annullaCategoriaCespiti(CategoriaCespiti categoriaCespiti, Date validitaFine) {
		Date now = new Date();
		
		SiacDCespitiCategoria siacDCespitiCategoria = siacDCespitiCategoriaRepository.findOne(categoriaCespiti.getUid());
		siacDCespitiCategoria.setDataFineValidita(validitaFine);
		siacDCespitiCategoria.setLoginOperazione(loginOperazione);
		siacDCespitiCategoria.setDataModifica(now);
		siacDCespitiCategoriaRepository.saveAndFlush(siacDCespitiCategoria);
		
		List<SiacRCespitiCategoriaAliquotaCalcoloTipo> siacRCespitiCategoriaAliquotaCalcoloTipos = siacRCespitiCategoriaAliquotaCalcoloTipoRepository.findRelazioneValidaByIdCategoriaEData(categoriaCespiti.getUid(), validitaFine);
		if(siacRCespitiCategoriaAliquotaCalcoloTipos == null || siacRCespitiCategoriaAliquotaCalcoloTipos.isEmpty() ) {
			return;
		}
		if(siacRCespitiCategoriaAliquotaCalcoloTipos.size() != 1) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("sono presenti piu' legami validi con aliquota e tipo calcolo."));
		}
		SiacRCespitiCategoriaAliquotaCalcoloTipo siacRCespitiCategoriaAliquotaCalcoloTipo = siacRCespitiCategoriaAliquotaCalcoloTipos.get(0);
		siacRCespitiCategoriaAliquotaCalcoloTipo.setDataFineValidita(validitaFine);
		siacRCespitiCategoriaAliquotaCalcoloTipo.setLoginOperazione(loginOperazione);
		siacRCespitiCategoriaAliquotaCalcoloTipo.setDataModifica(now);
		
		siacRCespitiCategoriaAliquotaCalcoloTipoRepository.saveAndFlush(siacRCespitiCategoriaAliquotaCalcoloTipo);
	}

	public Long contaTipoBeneByCategoria(CategoriaCespiti categoriaCespiti) {
		return siacDCespitiCategoriaRepository.countTipoBeneByCategoriaCespite(categoriaCespiti.getUid(), categoriaCespiti.getDataInizioValiditaFiltro());
	}

}
