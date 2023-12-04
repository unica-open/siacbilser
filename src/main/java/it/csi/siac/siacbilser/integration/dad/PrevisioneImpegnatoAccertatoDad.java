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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacRBilElemPrevisioneImpAccRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemPrevisioneImpacc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.PrevisioneImpegnatoAccertato;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class CapitoloUscitaGestioneDad.
 *
 * @author 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class PrevisioneImpegnatoAccertatoDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private SiacRBilElemPrevisioneImpAccRepository siacRBilElemPrevisioneImpAccRepository;
	
	public PrevisioneImpegnatoAccertato create(PrevisioneImpegnatoAccertato previsioneImpegnatoAccertatoSuCapitolo) {
		previsioneImpegnatoAccertatoSuCapitolo.setEnte(ente);
		SiacRBilElemPrevisioneImpacc siacRBilElemPrevisioneImpacc = buildSiacRBilElemPrevisioneImpacc(previsioneImpegnatoAccertatoSuCapitolo);
		SiacRBilElemPrevisioneImpacc saved = siacRBilElemPrevisioneImpAccRepository.save(siacRBilElemPrevisioneImpacc);
		previsioneImpegnatoAccertatoSuCapitolo.setUid(saved.getUid());
		return previsioneImpegnatoAccertatoSuCapitolo;
	}

	private SiacRBilElemPrevisioneImpacc buildSiacRBilElemPrevisioneImpacc(PrevisioneImpegnatoAccertato previsioneImpegnatoAccertatoSuCapitolo) {
		 SiacRBilElemPrevisioneImpacc built =  map(previsioneImpegnatoAccertatoSuCapitolo,SiacRBilElemPrevisioneImpacc.class , BilMapId.SiacRBilElemPrevisioneImpacc_PrevisioneImpegnatoAccertato);
		 built.setDataModificaInserimento(new Date());
		 built.setLoginOperazione(loginOperazione);
		 return built;
	}
	
	public PrevisioneImpegnatoAccertato caricaPrevisioneImpegnatoAccertatoByIdCapitolo(Integer uidCapitolo) {
		List<SiacRBilElemPrevisioneImpacc> founds = siacRBilElemPrevisioneImpAccRepository.findByCapitolo(uidCapitolo, ente.getUid());
		if(founds == null || founds.isEmpty() || founds.get(0) == null) {
			return null;
		}
		SiacRBilElemPrevisioneImpacc found = founds.get(0);
		if(founds.size() > 1) {
			String capitolo = found.getSiacTBilElem().getElemCode() ; 
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("trovati piu' record corrispondenti al capitolo " + capitolo + " indicato."));
		}
		return map(found, PrevisioneImpegnatoAccertato.class, BilMapId.SiacRBilElemPrevisioneImpacc_PrevisioneImpegnatoAccertato);
	}
	

	public PrevisioneImpegnatoAccertato caricaPrevisioneImpegnatoAccertatoById(Integer uidPrevisioneImpegnatoAccertato) {
		SiacRBilElemPrevisioneImpacc found = siacRBilElemPrevisioneImpAccRepository.findOne(uidPrevisioneImpegnatoAccertato);
		if(found == null) {
			return null;
		}
		return map(found, PrevisioneImpegnatoAccertato.class, BilMapId.SiacRBilElemPrevisioneImpacc_PrevisioneImpegnatoAccertato);
	}

	public PrevisioneImpegnatoAccertato updateImporti(Integer uid,PrevisioneImpegnatoAccertato previsioneImpegnatoAccertatoSuCapitolo) {
		SiacRBilElemPrevisioneImpacc found = siacRBilElemPrevisioneImpAccRepository.findOne(uid);
		if(found == null) {
			return null;
		}
		found.setImportoPrevAnno1(previsioneImpegnatoAccertatoSuCapitolo.getImportoPrevAnno1());
		found.setImportoPrevAnno2(previsioneImpegnatoAccertatoSuCapitolo.getImportoPrevAnno2());
		found.setImportoPrevAnno3(previsioneImpegnatoAccertatoSuCapitolo.getImportoPrevAnno3());
		found.setImportoPrevNote(previsioneImpegnatoAccertatoSuCapitolo.getNote());
		found.setDataModificaAggiornamento(new Date());
		found.setLoginOperazione(loginOperazione);
		siacRBilElemPrevisioneImpAccRepository.saveAndFlush(found);
		return map(found,PrevisioneImpegnatoAccertato.class, BilMapId.SiacRBilElemPrevisioneImpacc_PrevisioneImpegnatoAccertato);
	}

	public boolean hasRecordConStessoAnnoBilancio(String annoBilancio) {
		List<SiacRBilElemPrevisioneImpacc> founds = siacRBilElemPrevisioneImpAccRepository.findByAnno(annoBilancio, ente.getUid());
		return founds != null && !founds.isEmpty();
	}

	
}
