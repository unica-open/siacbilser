/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRIvaStampaFile;
import it.csi.siac.siacbilser.integration.entity.SiacTFile;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaStampa;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siaccorser.model.file.StatoFile.CodiceStatoFile;
import it.csi.siac.siacfin2ser.model.StampaIva;

/**
 * The Class GruppoAttivitaIvaRegistroIvaConverter.
 */
@Component
public class StampaIvaFileConverter extends ExtendedDozerConverter<StampaIva,SiacTIvaStampa > {
	
	
	/**
	 * Instantiates a new gruppo attivita iva registro iva converter.
	 */
	public StampaIvaFileConverter() {
		super(StampaIva.class, SiacTIvaStampa.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StampaIva convertFrom(SiacTIvaStampa src, StampaIva dest) {
		
		if(src.getSiacRIvaStampaFiles()!=null){
			List<File> files = new ArrayList<File>();
			for(SiacRIvaStampaFile siacRIvaStampaFile : src.getSiacRIvaStampaFiles()){
				if(siacRIvaStampaFile.getDataCancellazione()==null){
					
					SiacTFile siacTFile = siacRIvaStampaFile.getSiacTFile();
					File file = map(siacTFile, File.class, getMapIdFile());
					
					//messo in modo fittizio per evitare che i metodi isElaborato di File vadano in NullPointerException
					file.setStatoFile(CodiceStatoFile.CARICATO);
					
					files.add(file);
				}
			}
			dest.setFiles(files);
		}
		
        return dest;

	}

	protected BilMapId getMapIdFile() {
		return BilMapId.SiacTFile_File;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaStampa convertTo(StampaIva src, SiacTIvaStampa dest) {	
		List<SiacRIvaStampaFile> siacRIvaStampaFiles = new ArrayList<SiacRIvaStampaFile>();
		
		for(File file : src.getFiles()){		
		 SiacRIvaStampaFile siacRIvaStampaFile = new SiacRIvaStampaFile();
		 SiacTFile siacTFile = new SiacTFile();
		 siacTFile.setUid(file.getUid());
		 siacRIvaStampaFile.setSiacTIvaStampa(dest);
		 siacRIvaStampaFile.setSiacTFile(siacTFile);
		 siacRIvaStampaFile.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		 siacRIvaStampaFile.setLoginOperazione(dest.getLoginOperazione());
		 siacRIvaStampaFiles.add(siacRIvaStampaFile);
		}
		
		dest.setSiacRIvaStampaFiles(siacRIvaStampaFiles);
		return dest;
	}



	

}
