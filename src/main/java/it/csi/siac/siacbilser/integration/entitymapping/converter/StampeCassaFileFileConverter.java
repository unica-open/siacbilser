/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconStampaFile;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampa;
import it.csi.siac.siacbilser.integration.entity.SiacTFile;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siaccorser.model.file.StatoFile.CodiceStatoFile;
@Component
public class StampeCassaFileFileConverter extends ExtendedDozerConverter<StampeCassaFile,SiacTCassaEconStampa > {

	protected StampeCassaFileFileConverter() {
		super(StampeCassaFile.class,SiacTCassaEconStampa.class);
		
	}

	@Override
	public StampeCassaFile convertFrom(SiacTCassaEconStampa src, StampeCassaFile dest) {
		if(src.getSiacRCassaEconStampaFiles()!=null){
			List<File> files = new ArrayList<File>();
			for(SiacRCassaEconStampaFile siacRCassaEconStampaFile : src.getSiacRCassaEconStampaFiles()){
				if(siacRCassaEconStampaFile.getDataCancellazione()==null){
					
					SiacTFile siacTFile = siacRCassaEconStampaFile.getSiacTFile();
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
	
	@Override
	public SiacTCassaEconStampa convertTo(StampeCassaFile src, SiacTCassaEconStampa dest) {
		List<SiacRCassaEconStampaFile> siacRCassaEconStampaFiles = new ArrayList<SiacRCassaEconStampaFile>();
		
		for(File file : src.getFiles()){		
			SiacRCassaEconStampaFile siacRCassaEconStampaFile = new SiacRCassaEconStampaFile();
		 SiacTFile siacTFile = new SiacTFile();
		 siacTFile.setUid(file.getUid());
		 siacRCassaEconStampaFile.setSiacTCassaEconStampa(dest);
		 siacRCassaEconStampaFile.setSiacTFile(siacTFile);
		 siacRCassaEconStampaFile.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		 siacRCassaEconStampaFile.setLoginOperazione(dest.getLoginOperazione());
		 siacRCassaEconStampaFiles.add(siacRCassaEconStampaFile);
		}
		
		dest.setSiacRCassaEconStampaFiles(siacRCassaEconStampaFiles);
		return dest;

	}


}
