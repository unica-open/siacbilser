/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacRCronopAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTCronop;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class CronoprogrammaAttrConverter.
 */
@Component
public class CronoprogrammaAttoAmministrativoConverter extends ExtendedDozerConverter<Cronoprogramma, SiacTCronop > {
	
	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 * Instantiates a new cronoprogramma attr converter.
	 */
	public CronoprogrammaAttoAmministrativoConverter() {
		super(Cronoprogramma.class, SiacTCronop.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Cronoprogramma convertFrom(SiacTCronop src, Cronoprogramma dest) {
		if(src.getSiacRCronopAttoAmms() == null) {
			return dest;
		}
		
		for (SiacRCronopAttoAmm siacRCronopAttoAmm : src.getSiacRCronopAttoAmms()) {
			// L'atto è facoltativo quindi su db potrebbe essere null (oltre che con dataCancellazione valorizzata)
			if(siacRCronopAttoAmm.getDataCancellazione() != null || siacRCronopAttoAmm.getSiacTAttoAmm() == null){
				continue;
			}
			AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
			map(siacRCronopAttoAmm.getSiacTAttoAmm(), attoAmministrativo , BilMapId.SiacTAttoAmm_AttoAmministrativo);
			dest.setAttoAmministrativo(attoAmministrativo);
			
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCronop convertTo(Cronoprogramma src, SiacTCronop dest) {	
		
		if(src == null || src.getAttoAmministrativo() == null || src.getAttoAmministrativo().getUid() == 0) {
			return dest;
		}
		
		SiacTAttoAmm siacTAttoAmm = new SiacTAttoAmm();
		siacTAttoAmm.setAttoammId(src.getAttoAmministrativo().getUid());
		
		SiacRCronopAttoAmm siacRCronopAttoAmm = new SiacRCronopAttoAmm();
		siacRCronopAttoAmm.setSiacTAttoAmm(siacTAttoAmm);
		siacRCronopAttoAmm.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRCronopAttoAmm.setLoginOperazione(dest.getLoginOperazione());
		siacRCronopAttoAmm.setSiacTCronop(dest);
		
		
		List<SiacRCronopAttoAmm> siacRCronopAttoAmms = new ArrayList<SiacRCronopAttoAmm>();
		
		siacRCronopAttoAmms.add(siacRCronopAttoAmm);
		dest.setSiacRCronopAttoAmms(siacRCronopAttoAmms);
		
		return dest;
	
		
	}
	
}
