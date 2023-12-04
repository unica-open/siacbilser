/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoStampaFile;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegatoStampa;
import it.csi.siac.siacbilser.integration.entity.SiacTFile;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siaccorser.model.file.StatoFile.CodiceStatoFile;
import it.csi.siac.siacfin2ser.model.AllegatoAttoStampa;

/**
 * The Class StampaAllegatoAttoFileConverter.
 */
@Component
public class AllegatoAttoStampaFileConverter extends ExtendedDozerConverter<AllegatoAttoStampa, SiacTAttoAllegatoStampa> {

	/**
	 * Instantiates a new StampaAllegatoAttoFileConverter
	 */
	public AllegatoAttoStampaFileConverter() {
		super(AllegatoAttoStampa.class, SiacTAttoAllegatoStampa.class);
	}
	
	@Override
	public AllegatoAttoStampa convertFrom(SiacTAttoAllegatoStampa src, AllegatoAttoStampa dest) {
		if(src.getSiacRAttoAllegatoStampaFiles()!=null){
			List<File> files = new ArrayList<File>();
			for(SiacRAttoAllegatoStampaFile siacRAttoAllegatoStampaFile : src.getSiacRAttoAllegatoStampaFiles()){
				if(siacRAttoAllegatoStampaFile.getDataCancellazione()==null){
					
					SiacTFile siacTFile = siacRAttoAllegatoStampaFile.getSiacTFile();
					//File file = map(siacTFile, File.class, getMapIdSiacTFile_File());
					File file = new File();
					file.setUid(siacTFile.getUid());
					//messo in modo fittizio per evitare che i metodi isElaborato di File vadano in NullPointerException
					file.setStatoFile(CodiceStatoFile.CARICATO);
					
					files.add(file);
				}
			}
			dest.setFiles(files);
		}

		return dest;
	}

	@Override
	public SiacTAttoAllegatoStampa convertTo(AllegatoAttoStampa src, SiacTAttoAllegatoStampa dest) {
		List<SiacRAttoAllegatoStampaFile> siacRAttoAllegatoStampaFiles = new ArrayList<SiacRAttoAllegatoStampaFile>();
		
		for(File file : src.getFiles()){		
		 SiacRAttoAllegatoStampaFile siacRAttoAllegatoStampaFile = new SiacRAttoAllegatoStampaFile();
		 SiacTFile siacTFile = new SiacTFile();
		 siacTFile.setUid(file.getUid());
		 siacRAttoAllegatoStampaFile.setSiacTAttoAllegatoStampa(dest);
		 siacRAttoAllegatoStampaFile.setSiacTFile(siacTFile);
		 siacRAttoAllegatoStampaFile.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		 siacRAttoAllegatoStampaFile.setLoginOperazione(dest.getLoginOperazione());
		 siacRAttoAllegatoStampaFiles.add(siacRAttoAllegatoStampaFile);
		}
		
		dest.setSiacRAttoAllegatoStampaFiles(siacRAttoAllegatoStampaFiles);
		return dest;
	}
	protected BilMapId getMapIdFile() {
		return BilMapId.SiacTFile_File;
	}


	
}
