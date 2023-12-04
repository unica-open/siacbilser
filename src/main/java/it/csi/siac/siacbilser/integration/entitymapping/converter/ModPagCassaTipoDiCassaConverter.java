/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconModpagTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconTipoModpagTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCassaEconTipoEnum;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.TipoDiCassa;

/**
 * The Class ModPagCassaTipoDiCassaConverter.
 */
@Component
public class ModPagCassaTipoDiCassaConverter extends ExtendedDozerConverter<ModalitaPagamentoCassa, SiacDCassaEconModpagTipo > {
	
	@Autowired
	private EnumEntityFactory eef;

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public ModPagCassaTipoDiCassaConverter() {
		super(ModalitaPagamentoCassa.class, SiacDCassaEconModpagTipo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public ModalitaPagamentoCassa convertFrom(SiacDCassaEconModpagTipo src, ModalitaPagamentoCassa dest) {
		if(src.getSiacRCassaEconTipoModpagTipos() == null){
			return dest;
		}
		for(SiacRCassaEconTipoModpagTipo siacRCassaEconTipoModpagTipo : src.getSiacRCassaEconTipoModpagTipos()){
			if(siacRCassaEconTipoModpagTipo.getDataCancellazione() == null){
				SiacDCassaEconTipo siacDCassaEconTipo  = siacRCassaEconTipoModpagTipo.getSiacDCassaEconTipo();
				SiacDCassaEconTipoEnum siacDCassaEconTipoEnum = SiacDCassaEconTipoEnum.byCodice(siacDCassaEconTipo.getCassaeconTipoCode());
				TipoDiCassa	tipoDiCassa= siacDCassaEconTipoEnum.getTipoDiCassa();
				dest.setTipoDiCassa(tipoDiCassa);
				break;
			}
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDCassaEconModpagTipo convertTo(ModalitaPagamentoCassa src, SiacDCassaEconModpagTipo dest) {
		
		if(src.getTipoDiCassa() == null){
			return dest;
		}
		
		log.debug("convertTo", "ente da dest: " + dest.getSiacTEnteProprietario());
		log.debug("convertTo", "ente da src: " + src.getEnte());
		
		SiacDCassaEconTipoEnum siacDCassaEconTipoEnum = SiacDCassaEconTipoEnum.byCodice(src.getTipoDiCassa().getCodice());
		SiacDCassaEconTipo siacDCassaEconTipo = eef.getEntity(siacDCassaEconTipoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDCassaEconTipo.class); 
		
		List<SiacRCassaEconTipoModpagTipo> siacRCassaEconTipoModpagTipos = new ArrayList<SiacRCassaEconTipoModpagTipo>();
		SiacRCassaEconTipoModpagTipo siacRCassaEconTipoModpagTipo = new SiacRCassaEconTipoModpagTipo();
		siacRCassaEconTipoModpagTipo.setSiacDCassaEconTipo(siacDCassaEconTipo);
		siacRCassaEconTipoModpagTipo.setSiacDCassaEconModpagTipo(dest);
		siacRCassaEconTipoModpagTipo.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRCassaEconTipoModpagTipo.setLoginOperazione(dest.getLoginOperazione());
		siacRCassaEconTipoModpagTipos.add(siacRCassaEconTipoModpagTipo );
		dest.setSiacRCassaEconTipoModpagTipos(siacRCassaEconTipoModpagTipos);
		
		return dest;
	}



	

}
