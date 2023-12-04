/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovEp;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEpDet;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class MovimentoEPMovimentoDettaglioConverter extends ExtendedDozerConverter<MovimentoEP, SiacTMovEp> {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public MovimentoEPMovimentoDettaglioConverter() {
		super(MovimentoEP.class, SiacTMovEp.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public MovimentoEP convertFrom(SiacTMovEp src, MovimentoEP dest) {
		
		if(src.getSiacTMovEpDets() == null){
			return dest;
		}
		
		List<MovimentoDettaglio> listaMovimentoDettaglio = new ArrayList<MovimentoDettaglio>();
		for(SiacTMovEpDet siacTMovEpDet : src.getSiacTMovEpDets()){
			if(siacTMovEpDet.getDataCancellazione() == null){
				MovimentoDettaglio movimentoDettaglio = map(siacTMovEpDet, MovimentoDettaglio.class, GenMapId.SiacTMovEpDet_MovimentoDettaglio);
				listaMovimentoDettaglio.add(movimentoDettaglio);
			}
		}
		dest.setListaMovimentoDettaglio(listaMovimentoDettaglio);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTMovEp convertTo(MovimentoEP src, SiacTMovEp dest) {
		
		if(src.getListaMovimentoDettaglio() == null){
			return dest;
		}
		List<SiacTMovEpDet> siacTMovEpDets = new ArrayList<SiacTMovEpDet>();
		for(MovimentoDettaglio movimentoDettaglio : src.getListaMovimentoDettaglio()){
			movimentoDettaglio.setLoginOperazione(src.getLoginOperazione());
			movimentoDettaglio.setEnte(src.getEnte());
			movimentoDettaglio.setAmbito(src.getAmbito());
			SiacTMovEpDet siacTMovEpDet = map(movimentoDettaglio, SiacTMovEpDet.class, GenMapId.SiacTMovEpDet_MovimentoDettaglio);
			siacTMovEpDet.setSiacTMovEp(dest);
			siacTMovEpDets.add(siacTMovEpDet);
			
			
		}
		dest.setSiacTMovEpDets(siacTMovEpDets);
		return dest;
	}



	

}
