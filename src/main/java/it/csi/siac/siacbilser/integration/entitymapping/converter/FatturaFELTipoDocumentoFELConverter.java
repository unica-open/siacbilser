/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SirfelDTipoDocumentoRepository;
import it.csi.siac.siacbilser.integration.dao.SirfelTFatturaRepository;
import it.csi.siac.siacbilser.integration.entity.SirfelDTipoDocumento;
import it.csi.siac.siacbilser.integration.entity.SirfelDTipoDocumentoPK;
import it.csi.siac.siacbilser.integration.entity.SirfelTFattura;
import it.csi.siac.siacbilser.integration.entity.enumeration.SirfelDTipoDocumentoEnum;
import it.csi.siac.sirfelser.model.FatturaFEL;

// TODO: Auto-generated Javadoc
/**
 * The Class FatturaFELTipoDocumentoFELConverter.
 */
@Component
public class FatturaFELTipoDocumentoFELConverter extends DozerConverter<FatturaFEL, SirfelTFattura> {
	
	@Autowired
	private SirfelTFatturaRepository sirfelTFatturaRepository;
	@Autowired
	private SirfelDTipoDocumentoRepository sirfelDTipoDocumentoRepository;
	
	/**
	 * Instantiates a new documento spesa stato converter.
	 */
	public FatturaFELTipoDocumentoFELConverter() {
		super(FatturaFEL.class, SirfelTFattura.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public FatturaFEL convertFrom(SirfelTFattura src, FatturaFEL dest) {
		if(src.getSirfelDTipoDocumento() != null){
			SirfelDTipoDocumentoEnum tipoDocumentoEnum = SirfelDTipoDocumentoEnum.byCodice(src.getSirfelDTipoDocumento().getId().getCodice());
			//SIAC-7557-VG
			List<SirfelDTipoDocumento> sirfelDTipoDocumentos = sirfelDTipoDocumentoRepository.findSirfelDTipoDocumentoByEnteAndCodFel(src.getEnteProprietarioId(), 
					src.getSirfelDTipoDocumento().getId().getCodice());
			
			if(sirfelDTipoDocumentos!= null && !sirfelDTipoDocumentos.isEmpty() &&
					sirfelDTipoDocumentos.get(0).getSiacDDocTipoS()!= null){
				dest.setDocTipoSpesa(sirfelDTipoDocumentos.get(0).getSiacDDocTipoS().getDocTipoId());
			}
			
			dest.setTipoDocumentoFEL(tipoDocumentoEnum.getTipoDocumentoFEL());
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SirfelTFattura convertTo(FatturaFEL src, SirfelTFattura dest) {
		if(src.getTipoDocumentoFEL() != null){
			
			SirfelDTipoDocumentoPK pk = new SirfelDTipoDocumentoPK();
			pk.setCodice(src.getTipoDocumentoFEL().getCodice());
			pk.setEnteProprietarioId(src.getEnte().getUid());
			SirfelDTipoDocumento sirfelDTipoDocumento = sirfelTFatturaRepository.findSirfelDTipoDocumentoBySirfelDTipoDocumentoPK(pk);
			
			dest.setSirfelDTipoDocumento(sirfelDTipoDocumento);
			dest.setTipoDocumento(src.getTipoDocumentoFEL().getCodice());
		}
		return dest;
	}



	

}
