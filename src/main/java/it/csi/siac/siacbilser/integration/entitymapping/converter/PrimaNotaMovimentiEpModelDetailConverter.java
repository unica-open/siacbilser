/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEp;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.MovimentoEPModelDetail;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class PrimaNotaMovimentiEpModelDetailConverter extends ExtendedDozerConverter<PrimaNota, SiacTPrimaNota> {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public PrimaNotaMovimentiEpModelDetailConverter() {
		super(PrimaNota.class, SiacTPrimaNota.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PrimaNota convertFrom(SiacTPrimaNota src, PrimaNota dest) {
		
		if(src.getSiacTMovEps() == null){
			return dest;
		}
		
		List<MovimentoEP> movimentiEp = new ArrayList<MovimentoEP>();
		for(SiacTMovEp siacTMovEp: src.getSiacTMovEps()){
			if(siacTMovEp.getDataCancellazione() == null){
				//TODO:aggiungere il model detail
				MovimentoEP movimentoEp = mapNotNull(siacTMovEp ,MovimentoEP.class, 
						GenMapId.SiacTMovEp_MovimentoEP_Base,
						Converters.byModelDetails(Utility.MDTL.byModelDetailClass(MovimentoEPModelDetail.class)));
				movimentiEp.add(movimentoEp);
			}
		}
		dest.setListaMovimentiEP(movimentiEp);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPrimaNota convertTo(PrimaNota src, SiacTPrimaNota dest) {
		if(src.getListaMovimentiEP() == null){
			return dest;
		}
		List<SiacTMovEp> siacTMovEps = new ArrayList<SiacTMovEp>();
		for(MovimentoEP movimentoEP: src.getListaMovimentiEP()){
			movimentoEP.setLoginOperazione(src.getLoginOperazione());
			movimentoEP.setEnte(src.getEnte());
			SiacTMovEp siacTMovEp = map(movimentoEP , SiacTMovEp.class, GenMapId.SiacTMovEp_MovimentoEP);
			siacTMovEp.setSiacTPrimaNota(dest);
			siacTMovEps.add(siacTMovEp);
		}
		dest.setSiacTMovEps(siacTMovEps);
		return dest;
	}



	

}
