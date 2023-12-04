/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacDCommissioneTipoRepository;
import it.csi.siac.siacfinser.integration.entity.SiacDCommissioneTipoFin;
import it.csi.siac.siacfinser.model.codifiche.CommissioneDocumento;

/**
 * Classe di DAD per il Codice Bollo.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CodiceCommissioneDocumentoDad extends BaseDadImpl {
	
	@Autowired
	SiacDCommissioneTipoRepository siacDCommissioneTipoRepository;

	
	/**
	 * Effettua la ricerca dei codici bollo per un Ente.
	 *
	 * @param ente the ente
	 * @return the list
	 */
	public List<CommissioneDocumento> ricercaCodiciCommissioneDocumento(Ente ente) {			
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(new Date(System.currentTimeMillis()));
		List<SiacDCommissioneTipoFin> elencoCodiciCommissioniDocumentoDB = siacDCommissioneTipoRepository.findDCommissioniTipoValidoByEnte(ente.getUid(),timestampInserimento);
		if(elencoCodiciCommissioniDocumentoDB == null) {
			return new ArrayList<CommissioneDocumento>();
		}
		
		List<CommissioneDocumento> elencoCodiciCommissioneDocumentoReturn = new ArrayList<CommissioneDocumento>(elencoCodiciCommissioniDocumentoDB.size());
		
		for (SiacDCommissioneTipoFin codCommissioni : elencoCodiciCommissioniDocumentoDB) {
			
			CommissioneDocumento codiceCommissioneDocumentoToAdd = mapCodiceCommissioneDocumento(codCommissioni);
			elencoCodiciCommissioneDocumentoReturn.add(codiceCommissioneDocumentoToAdd);
		}
		return elencoCodiciCommissioneDocumentoReturn;
	}

	private CommissioneDocumento mapCodiceCommissioneDocumento(SiacDCommissioneTipoFin codCommissioni) {
		CommissioneDocumento codCommissioniToAdd = new CommissioneDocumento();
		codCommissioniToAdd.setUid(codCommissioni.getUid());
		codCommissioniToAdd.setCodice(codCommissioni.getCommTipoCode());
		codCommissioniToAdd.setDescrizione(codCommissioni.getCommTipoDesc());
		return codCommissioniToAdd;
	}


	public CommissioneDocumento findCodiceCommissioneDocumentoByUid(Integer uid) {
		SiacDCommissioneTipoFin siacDCommissioneTipoFin = siacDCommissioneTipoRepository.findOne(uid);
		return mapCodiceCommissioneDocumento(siacDCommissioneTipoFin);
		
	}
}
