/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDPrimaNotaRelTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRPrimaNota;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoRelazionePrimaNota;

/**
 * The Class MovimentoEPAmbitoConverter.
 *
 * @author Domenico
 */
@Component
public class PrimeNoteCollegateConverter extends ExtendedDozerConverter<PrimaNota, SiacTPrimaNota > {
	
	public PrimeNoteCollegateConverter() {
		super(PrimaNota.class, SiacTPrimaNota.class);
	}

	@Override
	public PrimaNota convertFrom(SiacTPrimaNota src, PrimaNota dest) {
		
		List<PrimaNota> listaPrimaNotaPadre = new ArrayList<PrimaNota>();
		List<PrimaNota> listaPrimaNotaFiglia = new ArrayList<PrimaNota>();
		
		if(src.getSiacRPrimaNotaFiglio() != null){
			for(SiacRPrimaNota r : src.getSiacRPrimaNotaFiglio()){
				if(r.getDataCancellazione() == null){
					PrimaNota pn = map(r.getSiacTPrimaNotaPadre(), PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota_Base);
					pn.setNoteCollegamento(r.getNote());
					TipoRelazionePrimaNota tipoRelazione = mapNotNull(r.getSiacDPrimaNotaRelTipo(), TipoRelazionePrimaNota.class, GenMapId.SiacDPrimaNotaRelTipo_TipoRelazionePrimaNota);
					pn.setTipoRelazionePrimaNota(tipoRelazione);
					listaPrimaNotaPadre.add(pn);
				}
			}
		}
		
		if(src.getSiacRPrimaNotaPadre() != null){
			for(SiacRPrimaNota r : src.getSiacRPrimaNotaPadre()){
				if(r.getDataCancellazione() == null){
					PrimaNota pn = map(r.getSiacTPrimaNotaFiglio(), PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota_Base);
					pn.setNoteCollegamento(r.getNote());
					TipoRelazionePrimaNota tipoRelazione = mapNotNull(r.getSiacDPrimaNotaRelTipo(), TipoRelazionePrimaNota.class, GenMapId.SiacDPrimaNotaRelTipo_TipoRelazionePrimaNota);
					pn.setTipoRelazionePrimaNota(tipoRelazione);
					listaPrimaNotaFiglia.add(pn);
				}
			}
		}
		
		dest.setListaPrimaNotaPadre(listaPrimaNotaPadre);
		dest.setListaPrimaNotaFiglia(listaPrimaNotaFiglia);
		return dest;
		
	}

	@Override
	public SiacTPrimaNota convertTo(PrimaNota src, SiacTPrimaNota dest) {
		List<SiacRPrimaNota> siacRPrimaNotaPadri = new ArrayList<SiacRPrimaNota>();
		List<SiacRPrimaNota> siacRPrimaNotaFigli = new ArrayList<SiacRPrimaNota>();
		
		for(PrimaNota pNota : src.getListaPrimaNotaFiglia()){
			
			SiacTPrimaNota siacTPrimaNotaFiglio = new SiacTPrimaNota();
			siacTPrimaNotaFiglio.setUid(pNota.getUid());
			SiacRPrimaNota siacRPrimaNotaPadre = new SiacRPrimaNota();
			
			siacRPrimaNotaPadre.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			siacRPrimaNotaPadre.setSiacTPrimaNotaFiglio(siacTPrimaNotaFiglio);
			siacRPrimaNotaPadre.setSiacTPrimaNotaPadre(dest);
			siacRPrimaNotaPadre.setNote(pNota.getNoteCollegamento());
			siacRPrimaNotaPadre.setLoginOperazione(dest.getLoginOperazione());
			
			if(pNota.getTipoRelazionePrimaNota() != null && pNota.getTipoRelazionePrimaNota().getUid() != 0){
				SiacDPrimaNotaRelTipo siacDPrimaNotaRelTipo = map(pNota.getTipoRelazionePrimaNota(), SiacDPrimaNotaRelTipo.class, GenMapId.SiacDPrimaNotaRelTipo_TipoRelazionePrimaNota);
				siacRPrimaNotaPadre.setSiacDPrimaNotaRelTipo(siacDPrimaNotaRelTipo);
			}
			
			siacRPrimaNotaPadri.add(siacRPrimaNotaPadre);
		}
		
		for(PrimaNota pNota : src.getListaPrimaNotaPadre()){
			
			SiacDPrimaNotaRelTipo siacDPrimaNotaRelTipo = map(pNota.getTipoRelazionePrimaNota(), SiacDPrimaNotaRelTipo.class, GenMapId.SiacDPrimaNotaRelTipo_TipoRelazionePrimaNota);
			SiacTPrimaNota siacTPrimaNotaPadre = new SiacTPrimaNota();
			siacTPrimaNotaPadre.setUid(pNota.getUid());
			
			SiacRPrimaNota siacRPrimaNotaFiglio = new SiacRPrimaNota();
			siacRPrimaNotaFiglio.setSiacDPrimaNotaRelTipo(siacDPrimaNotaRelTipo);
			siacRPrimaNotaFiglio.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			siacRPrimaNotaFiglio.setSiacTPrimaNotaFiglio(dest);
			siacRPrimaNotaFiglio.setSiacTPrimaNotaPadre(siacTPrimaNotaPadre);
			siacRPrimaNotaFiglio.setNote(pNota.getNoteCollegamento());
			siacRPrimaNotaFiglio.setLoginOperazione(dest.getLoginOperazione());
			
			siacRPrimaNotaFigli.add(siacRPrimaNotaFiglio);
		}
		
		dest.setSiacRPrimaNotaPadre(siacRPrimaNotaPadri);
		dest.setSiacRPrimaNotaFiglio(siacRPrimaNotaFigli);
		return dest;
	}



	

}
