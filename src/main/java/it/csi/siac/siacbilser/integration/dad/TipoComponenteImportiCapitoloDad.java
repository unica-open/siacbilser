/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacDBilElemDetCompTipoRepository;
import it.csi.siac.siacbilser.integration.dao.TipoComponenteImportiCapitoloDao;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetCompMacroTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.DecodificaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ImpegnabileComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.PropostaDefaultComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.SottotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.StatoTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
//import it.csi.siac.siacbilser.model.TipoGestioneComponenteImportiCapitolo;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.DecodificaEnum;
import it.csi.siac.siacfin2ser.model.TipoComponenteImportiCapitoloModelDetail;


@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class TipoComponenteImportiCapitoloDad extends ExtendedBaseDadImpl{

	@Autowired
	private SiacDBilElemDetCompTipoRepository siacDBilElemDetCompTipoRepository;

	@Autowired
	private TipoComponenteImportiCapitoloDao tipoComponenteImportiCapitoloDao;

	public TipoComponenteImportiCapitolo inserisceTipoComponenteImportiCapitolo(TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo) {
		
		SiacDBilElemDetCompTipo siacDBilElemDetCompTipo = new SiacDBilElemDetCompTipo();
		siacDBilElemDetCompTipo.setSiacTEnteProprietario(siacTEnteProprietario);

		tipoComponenteImportiCapitolo.setStatoTipoComponenteImportiCapitolo(StatoTipoComponenteImportiCapitolo.VALIDO);
		map(tipoComponenteImportiCapitolo, siacDBilElemDetCompTipo, BilMapId.SiacDBilElemDetCompTipo_TipoComponenteImportiCapitolo);

		siacDBilElemDetCompTipo.setLoginOperazione(loginOperazione);
		
		SiacDBilElemDetCompTipo siacDBilElemDetCompTipoIns = tipoComponenteImportiCapitoloDao.create(siacDBilElemDetCompTipo);
		
		tipoComponenteImportiCapitolo.setUid(siacDBilElemDetCompTipoIns.getUid());
		
		return tipoComponenteImportiCapitolo;
	}

	public List<TipoComponenteImportiCapitolo> findAll(TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo, TipoComponenteImportiCapitoloModelDetail... modelDetails) {
		
		List<SiacDBilElemDetCompTipo> siacDBilElemDetCompTipoList = siacDBilElemDetCompTipoRepository.findAllByEnteProprietarioId(siacTEnteProprietario.getEnteProprietarioId());
		
		return convertiLista(siacDBilElemDetCompTipoList, TipoComponenteImportiCapitolo.class, BilMapId.SiacDBilElemDetCompTipo_TipoComponenteImportiCapitolo_ModelDetail, modelDetails);
	}
	
	public List<TipoComponenteImportiCapitolo> ricercaTipoComponenteImportiCapitolo(
			TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo,
			MacrotipoComponenteImportiCapitolo[] macrotipoComponenteImportiCapitoloDaEscludere,
			SottotipoComponenteImportiCapitolo[] sottotipoComponenteImportiCapitoloDaEscludere,
			PropostaDefaultComponenteImportiCapitolo[] propostaDefaultComponenteImportiCapitoloDaEscludere,
			//SIAC-7349
			ImpegnabileComponenteImportiCapitolo[] impegnabileComponenteImportiCapitoloDaEscludere,
			Integer annoBilancio,
			boolean soloValidiPerBilancio,
			TipoComponenteImportiCapitoloModelDetail... modelDetails) {
		
		List<SiacDBilElemDetCompTipo> siacDBilElemDetCompTipoList = tipoComponenteImportiCapitoloDao.ricercaTipoComponenteImportiCapitolo(
				siacTEnteProprietario.getEnteProprietarioId(),
				//SIAC-7349
				//tipoComponenteImportiCapitolo.getTipoGestioneComponenteImportiCapitolo() != null ? Boolean.valueOf(TipoGestioneComponenteImportiCapitolo.SOLO_AUTOMATICA.equals(tipoComponenteImportiCapitolo.getTipoGestioneComponenteImportiCapitolo())) : null,
				tipoComponenteImportiCapitolo.getDescrizione(),
				getCodice(tipoComponenteImportiCapitolo.getMacrotipoComponenteImportiCapitolo()),
				getCodice(tipoComponenteImportiCapitolo.getSottotipoComponenteImportiCapitolo()),
				getCodice(tipoComponenteImportiCapitolo.getAmbitoComponenteImportiCapitolo()),
				getCodice(tipoComponenteImportiCapitolo.getFonteFinanziariaComponenteImportiCapitolo()),
				getCodice(tipoComponenteImportiCapitolo.getMomentoComponenteImportiCapitolo()),
				getCodice(tipoComponenteImportiCapitolo.getPropostaDefaultComponenteImportiCapitolo()),
				//SIAC-7349
				getCodice(tipoComponenteImportiCapitolo.getImpegnabileComponenteImportiCapitolo()),
				getCodice(tipoComponenteImportiCapitolo.getStatoTipoComponenteImportiCapitolo()),
				tipoComponenteImportiCapitolo.getAnno(),
				annoBilancio,
				Boolean.valueOf(soloValidiPerBilancio),
				projectToCode(macrotipoComponenteImportiCapitoloDaEscludere),
				projectToCode(sottotipoComponenteImportiCapitoloDaEscludere),
				projectToCode(propostaDefaultComponenteImportiCapitoloDaEscludere),
				//SIAC-7349
				projectToCode(impegnabileComponenteImportiCapitoloDaEscludere));
		
		return convertiLista(siacDBilElemDetCompTipoList, TipoComponenteImportiCapitolo.class, BilMapId.SiacDBilElemDetCompTipo_TipoComponenteImportiCapitolo_ModelDetail, modelDetails);
	}
	
	public ListaPaginata<TipoComponenteImportiCapitolo> ricercaSinteticaTipoComponenteImportiCapitolo(
			TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo,
			MacrotipoComponenteImportiCapitolo[] macrotipoComponenteImportiCapitoloDaEscludere,
			SottotipoComponenteImportiCapitolo[] sottotipoComponenteImportiCapitoloDaEscludere,
			PropostaDefaultComponenteImportiCapitolo[] propostaDefaultComponenteImportiCapitoloDaEscludere,
			//SIAC-7349
			ImpegnabileComponenteImportiCapitolo[] impegnabileComponenteImportiCapitoloDaEscludere,			
			//SIAC-7873
			boolean saltaControlloSuDateValidita,
			Integer annoBilancio,
			boolean soloValidiPerBilancio,
			ParametriPaginazione parametriPaginazione,
			TipoComponenteImportiCapitoloModelDetail... modelDetails) {
		
		Page<SiacDBilElemDetCompTipo> siacDBilElemDetCompTipoPagedList = tipoComponenteImportiCapitoloDao.ricercaPaginataTipoComponenteImportiCapitolo(
				siacTEnteProprietario.getEnteProprietarioId(), 
				//SIAC-7349
				//tipoComponenteImportiCapitolo.getTipoGestioneComponenteImportiCapitolo() != null ? Boolean.valueOf(TipoGestioneComponenteImportiCapitolo.SOLO_AUTOMATICA.equals(tipoComponenteImportiCapitolo.getTipoGestioneComponenteImportiCapitolo())) : null,
				tipoComponenteImportiCapitolo.getDescrizione(), 
				getCodice(tipoComponenteImportiCapitolo.getMacrotipoComponenteImportiCapitolo()),
				getCodice(tipoComponenteImportiCapitolo.getSottotipoComponenteImportiCapitolo()),
				getCodice(tipoComponenteImportiCapitolo.getAmbitoComponenteImportiCapitolo()),
				getCodice(tipoComponenteImportiCapitolo.getFonteFinanziariaComponenteImportiCapitolo()),
				getCodice(tipoComponenteImportiCapitolo.getMomentoComponenteImportiCapitolo()),
				getCodice(tipoComponenteImportiCapitolo.getPropostaDefaultComponenteImportiCapitolo()),
				//SIAC-7349
				getCodice(tipoComponenteImportiCapitolo.getImpegnabileComponenteImportiCapitolo()),
				getCodice(tipoComponenteImportiCapitolo.getStatoTipoComponenteImportiCapitolo()),
				//SIAC-7873
				saltaControlloSuDateValidita,
				tipoComponenteImportiCapitolo.getAnno(),
				annoBilancio,
				Boolean.valueOf(soloValidiPerBilancio),
				projectToCode(macrotipoComponenteImportiCapitoloDaEscludere),
				projectToCode(sottotipoComponenteImportiCapitoloDaEscludere),
				projectToCode(propostaDefaultComponenteImportiCapitoloDaEscludere),
				//SIAC-7349
				projectToCode(impegnabileComponenteImportiCapitoloDaEscludere),
				toPageable(parametriPaginazione));
		
		return toListaPaginata(siacDBilElemDetCompTipoPagedList, TipoComponenteImportiCapitolo.class, BilMapId.SiacDBilElemDetCompTipo_TipoComponenteImportiCapitolo_ModelDetail, modelDetails);
	}
	
	public TipoComponenteImportiCapitolo ricercaDettaglioTipoComponenteImportiCapitolo(TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo) {
		SiacDBilElemDetCompTipo siacDBilElemDetCompTipoList = findTipoComponenteImportiCapitolo(tipoComponenteImportiCapitolo);
		
		return map(siacDBilElemDetCompTipoList, TipoComponenteImportiCapitolo.class, BilMapId.SiacDBilElemDetCompTipo_TipoComponenteImportiCapitolo);
	}

	private SiacDBilElemDetCompTipo findTipoComponenteImportiCapitolo(TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo) {
		return siacDBilElemDetCompTipoRepository.findOne(tipoComponenteImportiCapitolo.getUid());
	}

	public TipoComponenteImportiCapitolo aggiornaTipoComponenteImportiCapitolo(TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo) {
		
		SiacDBilElemDetCompTipo siacDBilElemDetCompTipo = new SiacDBilElemDetCompTipo();
		
		siacDBilElemDetCompTipo.setSiacTEnteProprietario(siacTEnteProprietario);

		map(tipoComponenteImportiCapitolo, siacDBilElemDetCompTipo, BilMapId.SiacDBilElemDetCompTipo_TipoComponenteImportiCapitolo);
		
		siacDBilElemDetCompTipo.setLoginOperazione(loginOperazione);
		
		tipoComponenteImportiCapitoloDao.update(siacDBilElemDetCompTipo);

		return tipoComponenteImportiCapitolo;
	}

	private String getCodice(DecodificaTipoComponenteImportiCapitolo decodificaTipoComponenteImportiCapitolo) {
		return decodificaTipoComponenteImportiCapitolo == null ? null : decodificaTipoComponenteImportiCapitolo.getCodice();
	}
	
	/**
	 * Count tipo componente with macrotipo diverso da.
	 *
	 * @param uidTipoComponenti the uid tipo componenti
	 * @param macrotipo the macrotipo
	 * @return the long
	 */
	public Long countTipoComponenteWithMacrotipoDiversoDa(List<Integer> uidTipoComponenti, MacrotipoComponenteImportiCapitolo macrotipo) {
		SiacDBilElemDetCompMacroTipoEnum macroEnum = SiacDBilElemDetCompMacroTipoEnum.byMacrotipoComponenteImportiCapitolo(macrotipo);
		if(macroEnum == null || uidTipoComponenti == null || uidTipoComponenti.isEmpty()) {
			return Long.valueOf(0);
		}
		return siacDBilElemDetCompTipoRepository.countTipoComponenteWithMacrotipoDiversoDa(uidTipoComponenti, macroEnum.getCodice());
		
	}
	
	private List<String> projectToCode(PropostaDefaultComponenteImportiCapitolo... decodifiche) {
		if(decodifiche == null) {
			return new ArrayList<String>();
		}
		List<String> res = new ArrayList<String>();
		if(decodifiche != null) {
			for(PropostaDefaultComponenteImportiCapitolo de : decodifiche) {
				res.add(de.getCodice());
			}
		}
		return res;
	}
	
	//SIAC-7349
	public List<TipoComponenteImportiCapitolo> findAllByOnlyEnteProprietarioId(TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo, TipoComponenteImportiCapitoloModelDetail... modelDetails) {
		
		List<SiacDBilElemDetCompTipo> siacDBilElemDetCompTipoList = 
				siacDBilElemDetCompTipoRepository.findAllByOnlyEnteProprietarioId(siacTEnteProprietario.getEnteProprietarioId());
		
		return convertiLista(siacDBilElemDetCompTipoList, TipoComponenteImportiCapitolo.class, BilMapId.SiacDBilElemDetCompTipo_TipoComponenteImportiCapitolo_ModelDetail, modelDetails);
	}
	
	//SIAC-7349
	public List<TipoComponenteImportiCapitolo> findAllByEnteProprietarioIdImpegnabili(TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo, TipoComponenteImportiCapitoloModelDetail... modelDetails) {
		
		List<SiacDBilElemDetCompTipo> siacDBilElemDetCompTipoList = 
				siacDBilElemDetCompTipoRepository.findAllByEnteProprietarioIdImpegnabili(siacTEnteProprietario.getEnteProprietarioId());
		
		return convertiLista(siacDBilElemDetCompTipoList, TipoComponenteImportiCapitolo.class, BilMapId.SiacDBilElemDetCompTipo_TipoComponenteImportiCapitolo_ModelDetail, modelDetails);
	}
	
}
