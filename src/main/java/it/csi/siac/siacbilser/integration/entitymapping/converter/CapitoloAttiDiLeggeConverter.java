/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAttoLegge;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoLegge;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entitymapping.AttMapId;
import it.csi.siac.siacbilser.model.AttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;


/**
 * The Class CapitoloEntrataGestioneCategoriaConverter
 */
@Component
public class CapitoloAttiDiLeggeConverter extends ExtendedDozerConverter<Capitolo<?, ?>, SiacTBilElem > {

	/**
	 * Instantiates a CapitoloEntrataPrevisioneCategoriaConverter.
	 */
	@SuppressWarnings("unchecked")
	public CapitoloAttiDiLeggeConverter() {
		super((Class<Capitolo<?, ?>>)(Class<?>)Capitolo.class, SiacTBilElem.class);
	}

	@Override
	public Capitolo<?, ?> convertFrom(SiacTBilElem src, Capitolo<?, ?> dest) {
		
		List<AttoDiLeggeCapitolo> listaAttiDiLeggeCapitolo = new ArrayList<AttoDiLeggeCapitolo>();
		if(src.getSiacRBilElemAttoLegges()!= null){
			for(SiacRBilElemAttoLegge r : src.getSiacRBilElemAttoLegges()){								
				AttoDiLeggeCapitolo relazione = map(r, AttoDiLeggeCapitolo.class, AttMapId.SiacRBilElemAttoLegge_AttoDiLeggeCapitolo);
				if (r.getDataCancellazione() != null){
					relazione.setStato(StatoEntita.CANCELLATO);
				}else{
					relazione.setStato(StatoEntita.VALIDO);
				}
				relazione.setAttoDiLegge(mapToAttoDiLegge(r.getSiacTAttoLegge()));
				
				listaAttiDiLeggeCapitolo.add(relazione);
				
			}
			dest.setListaAttiDiLeggeCapitolo(listaAttiDiLeggeCapitolo);
		}
		return dest;
	}


	@Override
	public SiacTBilElem convertTo(Capitolo<?, ?> src, SiacTBilElem dest) {	
		return dest;	
	}
	
	/**
	 * Map to atto di legge.
	 *
	 * @param atto the atto
	 * @return the atto di legge
	 */
	private AttoDiLegge mapToAttoDiLegge(SiacTAttoLegge atto) {
		
		AttoDiLegge attoMappato =  mapNotNull(atto, AttoDiLegge.class, AttMapId.SiacTAttoLegge);
		
		if (atto.getDataCancellazione() != null){
			attoMappato.setStato(StatoEntita.CANCELLATO);
		}else{
			attoMappato.setStato(StatoEntita.VALIDO);
		}
		TipoAtto tipoAtto = new TipoAtto();
		tipoAtto.setUid(atto.getSiacDAttoLeggeTipo().getUid());
		tipoAtto.setCodice(atto.getSiacDAttoLeggeTipo().getAttoleggeTipoCode());
		tipoAtto.setDescrizione(atto.getSiacDAttoLeggeTipo().getAttoleggeTipoDesc());
		
		attoMappato.setTipoAtto(tipoAtto);
		
		return attoMappato;
	}
	
}
