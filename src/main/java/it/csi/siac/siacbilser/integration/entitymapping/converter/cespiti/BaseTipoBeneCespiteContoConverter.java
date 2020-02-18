/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import java.util.Date;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiBeneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siacgenser.model.Conto;

/**
 * The Class TipoBeneCespitiCategoriaCespitiConverter.
 *
 * @author Anto
 */
public abstract class BaseTipoBeneCespiteContoConverter extends ExtendedDozerConverter<TipoBeneCespite, SiacDCespitiBeneTipo > {
	
	/**
	 * Instantiates a new base tipo bene cespite conto patrimoniale converter.
	 */
	public BaseTipoBeneCespiteContoConverter() {
		super(TipoBeneCespite.class, SiacDCespitiBeneTipo.class);
	}

	@Override
	public TipoBeneCespite convertFrom(SiacDCespitiBeneTipo src, TipoBeneCespite dest) {
		Date dataInizioValiditaFiltro = dest.getDataInizioValiditaFiltro();
		if(dataInizioValiditaFiltro == null) {
			dataInizioValiditaFiltro = Utility.ultimoGiornoDellAnno(Utility.BTL.get().getAnno());
		}
		
		SiacTPdceConto siacTPdceConto = getSiacTPdceConto(src,dataInizioValiditaFiltro);
		if(siacTPdceConto == null) {
			//BOH
			return dest;
		}
		impostaConto(dest, siacTPdceConto);
		return dest;
		
	}

	@Override
	public SiacDCespitiBeneTipo convertTo(TipoBeneCespite src, SiacDCespitiBeneTipo dest) {
		Conto conto = getConto(src);
		if(conto == null || conto.getUid() == 0) {
			//BOH? i pare che non sia obbligatorio
			return dest;
		}
		SiacTPdceConto siacTPdceConto = new SiacTPdceConto();
		
		siacTPdceConto.setUid(conto.getUid());
		siacTPdceConto.setPdceContoCode(conto.getCodice());
		siacTPdceConto.setPdceContoDesc(conto.getDescrizione());
		
		impostaSiacTPdceConto(dest, conto);
		
		return dest;
	}

	
	protected abstract void impostaSiacTPdceConto(SiacDCespitiBeneTipo dest, Conto conto);

	protected abstract Conto getConto(TipoBeneCespite tipoBene);
	
	protected abstract SiacTPdceConto getSiacTPdceConto(SiacDCespitiBeneTipo siacDCespitiBeneTipo, Date date);
	
	protected abstract void impostaConto(TipoBeneCespite dest, SiacTPdceConto siacTPdceConto);

}
