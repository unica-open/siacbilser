/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDElencoDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacRElencoDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacTElencoDoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDElencoDocStatoEnum;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;

// TODO: Auto-generated Javadoc
/**
 * Converter per lo StatoOperativoElencoDocumenti
 *  
 *
 */
@Component
public class ElencoDocStatoConverter extends DozerConverter<ElencoDocumentiAllegato, SiacTElencoDoc> {
	
	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 *  Costruttore vuoto di default.
	 */
	public ElencoDocStatoConverter() {
		super(ElencoDocumentiAllegato.class, SiacTElencoDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public ElencoDocumentiAllegato convertFrom(SiacTElencoDoc src, ElencoDocumentiAllegato dest) {
		for(SiacRElencoDocStato siacTElencoDocStato : src.getSiacRElencoDocStatos()) {
			if(siacTElencoDocStato.getDataCancellazione() == null) {
				StatoOperativoElencoDocumenti statoOperativoElencoDocumenti = SiacDElencoDocStatoEnum.byCodice(siacTElencoDocStato.getSiacDElencoDocStato().getEldocStatoCode()).getStatoOperativoElencoDocumenti();
				dest.setStatoOperativoElencoDocumenti(statoOperativoElencoDocumenti);
			}
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTElencoDoc convertTo(ElencoDocumentiAllegato src, SiacTElencoDoc dest) {
		final String methodName = "convertTo";
		
		if(dest == null) {
			return dest;
		}
		
		List<SiacRElencoDocStato> siacRElencoDocStatos = new ArrayList<SiacRElencoDocStato>();
		SiacRElencoDocStato siacRElencoDocStato = new SiacRElencoDocStato();
		
		SiacDElencoDocStatoEnum stato = SiacDElencoDocStatoEnum.byStatoOperativo(src.getStatoOperativoElencoDocumenti());
		SiacDElencoDocStato siacDElencoDocStato = eef.getEntity(stato, dest.getSiacTEnteProprietario().getUid(), SiacDElencoDocStato.class);
		
		log.debug(methodName, "Setting siacDElencoDocStato to: " + siacDElencoDocStato.getEldocStatoCode() + " [" + siacDElencoDocStato.getUid() + "]");
		
		siacRElencoDocStato.setSiacDElencoDocStato(siacDElencoDocStato);
		siacRElencoDocStato.setSiacTElencoDoc(dest);
		siacRElencoDocStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRElencoDocStato.setLoginOperazione(dest.getLoginOperazione());
		
		siacRElencoDocStatos.add(siacRElencoDocStato);
		dest.setSiacRElencoDocStatos(siacRElencoDocStatos);
		
		return dest;
	}

}
