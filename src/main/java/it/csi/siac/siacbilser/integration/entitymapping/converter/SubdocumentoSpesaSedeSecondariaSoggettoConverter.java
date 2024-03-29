/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTSoggettoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocSog;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * The Class SubdocumentoSpesaSedeSecondariaSoggettoConverter.
 */
@Component
public class SubdocumentoSpesaSedeSecondariaSoggettoConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {
	

	@Autowired
	private SiacTSoggettoRepository siacTSoggettoRepository;
	
	/**
	 * Instantiates a new subdocumento spesa sede secondaria soggetto converter.
	 */
	public SubdocumentoSpesaSedeSecondariaSoggettoConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		
		if(src.getSiacRSubdocSogs()!=null){
			for(SiacRSubdocSog siacRSubdocClass : src.getSiacRSubdocSogs()){
				if(siacRSubdocClass.getDataCancellazione()!=null){
					continue;
				}
				
				SiacTSoggetto siacTSoggetto = siacTSoggettoRepository.findOne(siacRSubdocClass.getSiacTSoggetto().getUid());
						
				
				SedeSecondariaSoggetto sss = new SedeSecondariaSoggetto();
				
				sss.setUid(siacTSoggetto.getUid());
				sss.setCodiceSedeSecondaria(siacTSoggetto.getSoggettoCode());
				sss.setDenominazione(siacTSoggetto.getSoggettoDesc());			
				
				dest.setSedeSecondariaSoggetto(sss);
							
			}	
		}
		
		return dest;
	}
	

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		
		//La sede secondaria soggetto è facoltativa, quindi può arrivare null
		if(src.getSedeSecondariaSoggetto()==null || src.getSedeSecondariaSoggetto().getUid()==0){
			return dest;
		}
		
		dest.setSiacRSubdocSogs(new ArrayList<SiacRSubdocSog>());
		
		SiacRSubdocSog siacRSubdocSog = new SiacRSubdocSog();
		
		SiacTSoggetto siacTSoggetto = new SiacTSoggetto();
		siacTSoggetto.setUid(src.getSedeSecondariaSoggetto().getUid());
		siacRSubdocSog.setSiacTSoggetto(siacTSoggetto);
		siacRSubdocSog.setSiacTSubdoc(dest);
		
		siacRSubdocSog.setLoginOperazione(dest.getLoginOperazione());
		siacRSubdocSog.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRSubdocSog(siacRSubdocSog);
		
		
		return dest;
	}



}
