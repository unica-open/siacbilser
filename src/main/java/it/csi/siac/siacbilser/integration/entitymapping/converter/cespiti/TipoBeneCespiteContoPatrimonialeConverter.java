/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacDCespitiBeneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRCespitiBeneTipoContoPatrCat;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siacgenser.model.Conto;

/**
 * The Class TipoBeneCespitiCategoriaCespitiConverter.
 *
 * @author Anto
 */
public class TipoBeneCespiteContoPatrimonialeConverter extends BaseTipoBeneCespiteContoConverter {

	@Override
	protected Conto getConto(TipoBeneCespite tipoBene) {
		return tipoBene.getContoPatrimoniale();
	}

	@Override
	protected void impostaSiacTPdceConto(SiacDCespitiBeneTipo dest, Conto conto) {
		
		List<SiacRCespitiBeneTipoContoPatrCat> siacRCespitiBeneTipoContoPatrCats = new ArrayList<SiacRCespitiBeneTipoContoPatrCat>();
		
		SiacRCespitiBeneTipoContoPatrCat siacRCespitiBeneTipoContoPatrCat = dest.getSiacRCespitiBeneTipoContoPatrCats() == null || dest.getSiacRCespitiBeneTipoContoPatrCats().isEmpty() ?  
				new SiacRCespitiBeneTipoContoPatrCat() : dest.getSiacRCespitiBeneTipoContoPatrCats().get(0);
		
		SiacTPdceConto siacTPdceContoPatrimoniale = new SiacTPdceConto();
		siacTPdceContoPatrimoniale.setUid(conto.getUid());
		
		siacRCespitiBeneTipoContoPatrCat.setSiacTPdceContoPatrimoniale(siacTPdceContoPatrimoniale);
		siacRCespitiBeneTipoContoPatrCat.setPdceContoPatrimonialeCode(conto.getCodice());
		siacRCespitiBeneTipoContoPatrCat.setPdceContoPatrimonialeDesc(conto.getDescrizione());
		siacRCespitiBeneTipoContoPatrCat.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRCespitiBeneTipoContoPatrCat.setLoginOperazione(dest.getLoginOperazione());
		
		siacRCespitiBeneTipoContoPatrCats.add(siacRCespitiBeneTipoContoPatrCat);

		dest.setSiacRCespitiBeneTipoContoPatrCats(siacRCespitiBeneTipoContoPatrCats);
		
	}

	@Override
	protected SiacTPdceConto getSiacTPdceConto(SiacDCespitiBeneTipo siacDCespitiBeneTipo, Date dataInizioValiditaFiltro) {
		final String methodName ="getSiacTPdceConto";
		
		List<SiacRCespitiBeneTipoContoPatrCat> siacRCespitiBeneTipoContoPatrCats = siacDCespitiBeneTipo.getSiacRCespitiBeneTipoContoPatrCats();
		
		if(siacRCespitiBeneTipoContoPatrCats == null || siacRCespitiBeneTipoContoPatrCats.isEmpty()){
			log.warn(methodName, "TipoBeneCespiti [uid: " +siacDCespitiBeneTipo.getUid()+"] priva di Categoria! Controllare su DB. ");
			return null;
		}
		for (SiacRCespitiBeneTipoContoPatrCat siacRCespitiBeneTipoContoPatrCat : siacRCespitiBeneTipoContoPatrCats) {
			if(siacRCespitiBeneTipoContoPatrCat.getDataCancellazione() != null || !siacRCespitiBeneTipoContoPatrCat.isDataValiditaCompresa(dataInizioValiditaFiltro)) {
				continue;
			}
			return siacRCespitiBeneTipoContoPatrCat.getSiacTPdceContoPatrimoniale();
		}
		return null;
	}

	@Override
	protected void impostaConto(TipoBeneCespite dest, SiacTPdceConto siacTPdceConto) {
		//QUESTO DEVE ESSERE FATTO DA UN MAPPING
		Conto contoPatrimoniale = new Conto();
		contoPatrimoniale.setUid(siacTPdceConto.getPdceContoId());
		contoPatrimoniale.setCodice(siacTPdceConto.getPdceContoCode());
		contoPatrimoniale.setDescrizione(siacTPdceConto.getPdceContoDesc());
		dest.setContoPatrimoniale(contoPatrimoniale);
	
	}
	
	


}
