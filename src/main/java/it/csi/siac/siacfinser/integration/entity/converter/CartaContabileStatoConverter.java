/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siacfinser.integration.dao.EnumEntityFinFactory;
import it.csi.siac.siacfinser.integration.entity.SiacDCartacontStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRCartacontStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTCartacontFin;
import it.csi.siac.siacfinser.integration.entity.enumeration.SiacDCartaContabileStatoOpEnum;
import it.csi.siac.siacfinser.model.carta.CartaContabile.StatoOperativoCartaContabile;

@Component
public class CartaContabileStatoConverter extends FinExtendedDozerConverter<StatoOperativoCartaContabile, SiacTCartacontFin> { 


	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	@Autowired
	private EnumEntityFinFactory eef;
	
	public CartaContabileStatoConverter() {
		super(StatoOperativoCartaContabile.class, SiacTCartacontFin.class);
		
	}

	@Override
	public StatoOperativoCartaContabile convertFrom(SiacTCartacontFin src, StatoOperativoCartaContabile dest) {
		
		List<SiacRCartacontStatoFin> listaRCartacontStato =  src.getSiacRCartacontStatos();
		if(listaRCartacontStato!=null && listaRCartacontStato.size()>0){
			for(SiacRCartacontStatoFin rCartaContStato : listaRCartacontStato){
				if(rCartaContStato!=null && rCartaContStato.getDataFineValidita()==null){
					String code = rCartaContStato.getSiacDCartacontStato().getCartacStatoCode();
					
					return SiacDCartaContabileStatoOpEnum.byCodice(code).getStatoCartaContabile();
					
				}
			}
		}
		
		
		return null;
	}
	
	@Override
	public SiacTCartacontFin convertTo(StatoOperativoCartaContabile src, SiacTCartacontFin dest) {
		final String methodName = "convertTo";
		
		if(dest == null) {
			return dest;
		}
		
		List<SiacRCartacontStatoFin> listaRCartacontStato  = new ArrayList<SiacRCartacontStatoFin>();
		SiacRCartacontStatoFin siacRCartacontStato = new SiacRCartacontStatoFin();
		
		SiacDCartaContabileStatoOpEnum statoCarta = SiacDCartaContabileStatoOpEnum.byStatoCarta(src);
		
		
		SiacDCartacontStatoFin siacDCartacontStato = eef.getEntity(statoCarta, dest.getSiacTEnteProprietario().getUid(), SiacDCartacontStatoFin.class);
		
		log.debug(methodName, "Setting siacDCartacontStato to: " + siacDCartacontStato.getCartacStatoCode() + " [" + siacDCartacontStato.getUid() + "]");
		
		siacRCartacontStato.setSiacDCartacontStato(siacDCartacontStato);
		siacRCartacontStato.setSiacTCartacont(dest);
		siacRCartacontStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRCartacontStato.setLoginOperazione(dest.getLoginOperazione());
		
		listaRCartacontStato.add(siacRCartacontStato);
		
		dest.setSiacRCartacontStatos(listaRCartacontStato);
		
		return dest;
		
		
	}

}