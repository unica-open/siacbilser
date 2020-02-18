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
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiCategoriaCalcoloTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRCespitiCategoriaAliquotaCalcoloTipo;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.CategoriaCalcoloTipoCespite;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccorser.model.Bilancio;

/**
 * The Class CategoriaCespitiTipoCalcoloConverter.
 *
 * @author Domenico
 */
@Component
public class CategoriaCespitiTipoCalcoloConverter extends ExtendedDozerConverter<CategoriaCespiti, SiacDCespitiCategoria> {
	
	/**
	 * 
	 */
	public CategoriaCespitiTipoCalcoloConverter() {
		super(CategoriaCespiti.class, SiacDCespitiCategoria.class);
	}

	@Override
	public CategoriaCespiti convertFrom(SiacDCespitiCategoria src, CategoriaCespiti dest) {
		if(src.getSiacRCategoriaCespitiAliquotaCalcoloTipos() == null){
			return dest;
		}
		
		Date dataInizioValiditaFiltro = dest.getDataInizioValiditaFiltro();
		
		if(dataInizioValiditaFiltro == null) {
			Bilancio bilancio = Utility.BTL.get();
			dataInizioValiditaFiltro = Utility.ultimoGiornoDellAnno(bilancio.getAnno());
		}
		
		for (SiacRCespitiCategoriaAliquotaCalcoloTipo siacRCategoriaCespitiAliquotaCalcoloTipo : src.getSiacRCategoriaCespitiAliquotaCalcoloTipos()) {
			if(siacRCategoriaCespitiAliquotaCalcoloTipo.getDataCancellazione() !=  null || !siacRCategoriaCespitiAliquotaCalcoloTipo.isDataValiditaCompresa(dataInizioValiditaFiltro)) {
				continue;
			}
			CategoriaCalcoloTipoCespite categoriaCalcoloTipoCespite = mapNotNull(siacRCategoriaCespitiAliquotaCalcoloTipo.getSiacDCespitiCategoriaCalcoloTipo(), CategoriaCalcoloTipoCespite.class, CespMapId.SiacDCespitiCategoriaCalcoloTipo_CategoriaCespitiCalcoloTipo);
			dest.setCategoriaCalcoloTipoCespite(categoriaCalcoloTipoCespite);
		}
		
		return dest;
		
	}

	@Override
	public SiacDCespitiCategoria convertTo(CategoriaCespiti src, SiacDCespitiCategoria dest) {
		
//		if(src.getCategoriaCalcoloTipoCespite() == null || src.getCategoriaCalcoloTipoCespite().getUid() == 0) {
//			return dest;
//		}
		
		List<SiacRCespitiCategoriaAliquotaCalcoloTipo> siacRCategoriaCespitiAliquotaCalcoloTipos = new ArrayList<SiacRCespitiCategoriaAliquotaCalcoloTipo>();
		SiacRCespitiCategoriaAliquotaCalcoloTipo siacRCespitiCategoriaAliquotaCalcoloTipo = dest.getSiacRCategoriaCespitiAliquotaCalcoloTipos() == null || dest.getSiacRCategoriaCespitiAliquotaCalcoloTipos().isEmpty() ?  
				new SiacRCespitiCategoriaAliquotaCalcoloTipo() : dest.getSiacRCategoriaCespitiAliquotaCalcoloTipos().get(0);
		
		SiacDCespitiCategoriaCalcoloTipo siacDCespitiCategoriaCalcoloTipo = new SiacDCespitiCategoriaCalcoloTipo();
		siacDCespitiCategoriaCalcoloTipo.setUid(src.getCategoriaCalcoloTipoCespite().getUid());
		
		siacRCespitiCategoriaAliquotaCalcoloTipo.setSiacDCespitiCategoriaCalcoloTipo(siacDCespitiCategoriaCalcoloTipo);
		siacRCespitiCategoriaAliquotaCalcoloTipo.setSiacDCespitiCategoria(dest);
		
		siacRCespitiCategoriaAliquotaCalcoloTipo.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRCespitiCategoriaAliquotaCalcoloTipo.setLoginOperazione(dest.getLoginOperazione());
		
		siacRCategoriaCespitiAliquotaCalcoloTipos.add(siacRCespitiCategoriaAliquotaCalcoloTipo);
		
		dest.setSiacRCespitiCategoriaAliquotaCalcoloTipos(siacRCategoriaCespitiAliquotaCalcoloTipos);
		
		return dest;
	}



	

}
