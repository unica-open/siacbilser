/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dozer.DozerConverter;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.integration.entity.SiacRProgrammaAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTProgramma;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;

// TODO: Auto-generated Javadoc
/**
 * Converter per l'Atto Amministrativo tra Progetto e SiacTProgramma.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/02/2014
 *
 */
public class ProgettoAttoAmministrativoConverter extends DozerConverter<AttoAmministrativo, SiacTProgramma > {

	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());

	/**
	 * Instantiates a new progetto atto amministrativo converter.
	 */
	public ProgettoAttoAmministrativoConverter() {
		super(AttoAmministrativo.class, SiacTProgramma.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public AttoAmministrativo convertFrom(SiacTProgramma src, AttoAmministrativo dest) {
		if(src.getSiacRProgrammaAttoAmms() == null) {
			return dest;
		}
		final String methodName = "convertFrom";
		
		for (SiacRProgrammaAttoAmm siacRProgrammaAttoAmm : src.getSiacRProgrammaAttoAmms()) {
			// L'atto è facoltativo quindi su db potrebbe essere null (oltre che con dataCancellazione valorizzata)
			if(siacRProgrammaAttoAmm.getDataCancellazione() == null && siacRProgrammaAttoAmm.getSiacTAttoAmm() != null){
				SiacTAttoAmm siacTAttoAmm = siacRProgrammaAttoAmm.getSiacTAttoAmm();				
				log.debug(methodName, "siacRProgrammaAttoAmm.siacTAttoAmm.uid: " + siacTAttoAmm.getUid());		
				
				if(dest == null){
					dest = new AttoAmministrativo();
				}
				
				// Popolo l'atto amministrativo
				dest.setUid(siacTAttoAmm.getUid());	
				dest.setAnno(StringUtils.isNotBlank(siacTAttoAmm.getAttoammAnno()) ? Integer.parseInt(siacTAttoAmm.getAttoammAnno()) : 0);
				dest.setNumero(siacTAttoAmm.getAttoammNumero());
				dest.setNote(siacTAttoAmm.getAttoammNote());
				dest.setOggetto(siacTAttoAmm.getAttoammOggetto());
				dest.setDataCreazione(siacTAttoAmm.getDataCreazione());
				dest.setDataCreazioneAttoAmministrativo(siacTAttoAmm.getDataCreazione());
				dest.setParereRegolaritaContabile(siacTAttoAmm.getParereRegolaritaContabile());
				
				// Reperisco il tipo dell'atto amministrativo
				TipoAtto tipoAtto = new TipoAtto();
				tipoAtto.setUid(siacTAttoAmm.getSiacDAttoAmmTipo().getUid());
				tipoAtto.setCodice(siacTAttoAmm.getSiacDAttoAmmTipo().getAttoammTipoCode());
				tipoAtto.setDescrizione(siacTAttoAmm.getSiacDAttoAmmTipo().getAttoammTipoDesc());
				dest.setTipoAtto(tipoAtto);
			}
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTProgramma convertTo(AttoAmministrativo src, SiacTProgramma dest) {
		if(src == null || src.getUid() == 0) {
			return dest;
		}
		SiacRProgrammaAttoAmm siacRProgrammaAttoAmm;
		List<SiacRProgrammaAttoAmm> siacRProgrammaAttoAmms;
		
		if(dest == null) {
			dest = new SiacTProgramma();
		}
		
		if(dest.getSiacRProgrammaAttoAmms() == null) {
			siacRProgrammaAttoAmms = new ArrayList<SiacRProgrammaAttoAmm>();
			dest.setSiacRProgrammaAttoAmms(siacRProgrammaAttoAmms);
		} else {
			siacRProgrammaAttoAmms = dest.getSiacRProgrammaAttoAmms();
		}
		
		if(dest.getSiacRProgrammaAttoAmms().isEmpty()) {
			siacRProgrammaAttoAmm = new SiacRProgrammaAttoAmm();
			dest.getSiacRProgrammaAttoAmms().add(siacRProgrammaAttoAmm);
		} else {
			siacRProgrammaAttoAmm = dest.getSiacRProgrammaAttoAmms().get(0);
		}
		
		SiacTAttoAmm siacTAttoAmm = null;
		
		// L'atto è facoltativo quindi potrebbe essere passato null.
		siacTAttoAmm = new SiacTAttoAmm();
		siacTAttoAmm.setAttoammId(src.getUid());
		
		siacRProgrammaAttoAmm.setSiacTProgramma(dest);
		siacRProgrammaAttoAmm.setSiacTAttoAmm(siacTAttoAmm);
		siacRProgrammaAttoAmm.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRProgrammaAttoAmm.setLoginOperazione(dest.getLoginOperazione());
		
		dest.setSiacRProgrammaAttoAmms(siacRProgrammaAttoAmms);
		
		return dest;
	}

}
