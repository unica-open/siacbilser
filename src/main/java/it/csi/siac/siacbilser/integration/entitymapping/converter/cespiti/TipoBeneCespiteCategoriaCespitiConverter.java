/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiBeneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacRCespitiBeneTipoContoPatrCat;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.TipoBeneCespite;

/**
 * The Class TipoBeneCespitiCategoriaCespitiConverter.
 *
 * @author Anto
 */
@Component
public class TipoBeneCespiteCategoriaCespitiConverter extends ExtendedDozerConverter<TipoBeneCespite, SiacDCespitiBeneTipo > {
	
	//@Autowired private EnumEntityFactory eef;
	

	public TipoBeneCespiteCategoriaCespitiConverter() {
		super(TipoBeneCespite.class, SiacDCespitiBeneTipo.class);
	}

	@Override
	public TipoBeneCespite convertFrom(SiacDCespitiBeneTipo src, TipoBeneCespite dest) {
		String methodName = "convertFrom";
		List<SiacRCespitiBeneTipoContoPatrCat> siacRCespitiBeneTipoContoPatrCats = src.getSiacRCespitiBeneTipoContoPatrCats();
		
		if( siacRCespitiBeneTipoContoPatrCats == null || siacRCespitiBeneTipoContoPatrCats.isEmpty()){
			log.warn(methodName, "TipoBeneCespiti [uid: " +src.getUid()+"] priva di Categoria! Controllare su DB. Entita associata:"+src.getClass().getSimpleName());
			return dest;
		}
		
		Date dataValidita = dest.getDataInizioValiditaFiltro() != null? dest.getDataInizioValiditaFiltro() : Utility.primoGiornoDellAnno(Utility.BTL.get().getAnno());
		
		for (SiacRCespitiBeneTipoContoPatrCat siacRCespitiBeneTipoContoPatrCat : siacRCespitiBeneTipoContoPatrCats) {
			if(siacRCespitiBeneTipoContoPatrCat.getDataCancellazione() != null || !siacRCespitiBeneTipoContoPatrCat.isDataValiditaCompresa(dataValidita)) {
				continue;
			}
			
			CategoriaCespiti categoriaCespiti = mapNotNull(siacRCespitiBeneTipoContoPatrCat.getSiacDCespitiCategoria(), CategoriaCespiti.class,CespMapId.SiacDCespitiCategoria_CategoriaCespiti_Minimal );			
			dest.setCategoriaCespiti(categoriaCespiti );
			return dest;
		}
		
		return dest;
	}

	@Override
	public SiacDCespitiBeneTipo convertTo(TipoBeneCespite src, SiacDCespitiBeneTipo dest) {
		if(src.getCategoriaCespiti() == null || src.getCategoriaCespiti().getUid() == 0) {
			return dest;
		}
		List<SiacRCespitiBeneTipoContoPatrCat> siacRCespitiBeneTipoContoPatrCats = new ArrayList<SiacRCespitiBeneTipoContoPatrCat>();
		SiacRCespitiBeneTipoContoPatrCat siacRCespitiBeneTipoContoPatrCat = dest.getSiacRCespitiBeneTipoContoPatrCats() == null || dest.getSiacRCespitiBeneTipoContoPatrCats().isEmpty() ?  
				new SiacRCespitiBeneTipoContoPatrCat() : dest.getSiacRCespitiBeneTipoContoPatrCats().get(0);
		
		SiacDCespitiCategoria siacDCespitiCategoria = new SiacDCespitiCategoria();
		siacDCespitiCategoria.setUid(src.getCategoriaCespiti().getUid());
				
		siacRCespitiBeneTipoContoPatrCat.setSiacDCespitiCategoria(siacDCespitiCategoria);		
		siacRCespitiBeneTipoContoPatrCat.setSiacDCespitiBeneTipo(dest);
		siacRCespitiBeneTipoContoPatrCat.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRCespitiBeneTipoContoPatrCat.setLoginOperazione(dest.getLoginOperazione());
		
		
		siacRCespitiBeneTipoContoPatrCats.add(siacRCespitiBeneTipoContoPatrCat); 
		
		dest.setSiacRCespitiBeneTipoContoPatrCats(siacRCespitiBeneTipoContoPatrCats);
		
		return dest;
	}


}
