/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.RegistrazioneMovFinDao;
import it.csi.siac.siacbilser.integration.entity.SiacDCollegamentoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siacbilser.integration.entity.SiacREventoRegMovfin;
import it.csi.siac.siacbilser.integration.entity.SiacTRegMovfin;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * The Class RegistrazioneMovFinMovimentoConverter.
 * 
 * @author Valentina
 */
@Component
public class RegistrazioneMovFinEventoMovimentoConverter extends ExtendedDozerConverter<RegistrazioneMovFin, SiacTRegMovfin> {
	
	@Autowired
	private RegistrazioneMovFinDao registrazioneMovFinDao;
	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Instantiates a new causale ep stato converter.
	 */
	public RegistrazioneMovFinEventoMovimentoConverter() {
		super(RegistrazioneMovFin.class, SiacTRegMovfin.class);
	}

	
	
	@Override
	public RegistrazioneMovFin convertFrom(SiacTRegMovfin src, RegistrazioneMovFin dest) {
		String methodName = "convertFrom";
		if(src.getSiacREventoRegMovfins() == null){
			return dest;
		}
		for(SiacREventoRegMovfin siacREventoRegMovfin: src.getSiacREventoRegMovfins()){
			if(siacREventoRegMovfin.getDataCancellazione() != null){
				continue;
			}
			
			SiacDEvento siacDEvento = siacREventoRegMovfin.getSiacDEvento();
			Evento evento = map(siacDEvento, Evento.class, GenMapId.SiacDEvento_Evento);
			dest.setEvento(evento);
			
			SiacDCollegamentoTipo siacDCollegamentoTipo = siacDEvento.getSiacDCollegamentoTipo();
			if(siacDCollegamentoTipo==null){
				log.warn(methodName, "L'evento "+siacDEvento.getEventoCode() +" [uid:"+siacDEvento.getUid()+"] non ha definito un tipo collegamento. Verra' saltato.");
				continue;
			}
			SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum = SiacDCollegamentoTipoEnum.byCollegamentoTipoCode(siacDCollegamentoTipo.getCollegamentoTipoCode());
			
			SiacTBase siacTBase = registrazioneMovFinDao.ricercaMovimentoById(siacDCollegamentoTipoEnum.getEntityClass().getSimpleName(), siacDCollegamentoTipoEnum.getIdColumnName(), siacREventoRegMovfin.getCampoPkId());
			
			if(siacTBase == null) {
				throw new IllegalStateException("Nessun movimento di tipo " + siacDCollegamentoTipoEnum.getModelClass().getSimpleName() 
						+ " [uid: " + siacREventoRegMovfin.getCampoPkId()+ "] collegato alla registrazione [uid: " + src.getUid()+"]");
			}
			
			
			
			Entita movimento = siacDCollegamentoTipoEnum.getModelInstance();
			mapMovimento(siacDCollegamentoTipoEnum, siacTBase, movimento);
			dest.setMovimento(movimento);
			
			Integer campoPkId2 = siacREventoRegMovfin.getCampoPkId2();
			if (campoPkId2 != null) { //campo facoltativo.
				mapMovimentoCollegato(dest, siacDCollegamentoTipoEnum, campoPkId2);
			}
			
			break;
		}
		return dest;
	}


	/**
	 * Mappa le informazioni del movimento con il mapping predefinito impostato in {@link SiacDCollegamentoTipoEnum}.
	 * 
	 * @param siacDCollegamentoTipoEnum
	 * @param src entity managed sorgente
	 * @param dest Entita di modello di destinazione (il movimento).
	 */
	protected void mapMovimento(SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, SiacTBase src, Entita dest) {
		String methodName = "mapMovimento"; 
		log.debug(methodName, "Mapping "+siacDCollegamentoTipoEnum.getModelClass().getSimpleName() 
				+ " with MapId: " +siacDCollegamentoTipoEnum.getMapId().name() + "[uid: "+src.getUid()+"]");
		
		map(src, dest, siacDCollegamentoTipoEnum.getMapId());
	}
	
	
	private void mapMovimentoCollegato(RegistrazioneMovFin dest, SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, Integer campoPkId2) {
		//Per ora in campoPkId2 ci puo' essere solo l'id del subdoc collegato alla nota credito.
		//Renderne dinamica la gestione se sarà necessario.
		
		Entita subdocNotaCredito = siacDCollegamentoTipoEnum.getModelInstance(); //SubdocumentoSpesa o SubdocumentoEntrata.
		subdocNotaCredito.setUid(campoPkId2); //popolo solo l'uid.
		dest.setMovimentoCollegato(subdocNotaCredito);
	}
	
	
	@Override
	public SiacTRegMovfin convertTo(RegistrazioneMovFin src, SiacTRegMovfin dest) {
		
		if(src.getEvento() == null){
			return dest;
		}
		
		SiacDEvento siacDEvento = null;
		if(src.getEvento().getUid()!=0) { //se ho l'uid imposto l'uid.
			siacDEvento = new SiacDEvento();
			siacDEvento.setUid(src.getEvento().getUid());
		} else if(StringUtils.isNotBlank(src.getEvento().getCodice())){ //se ho il codice ricavo l'uid dal codice.
			SiacDEventoEnum siacDEventoEnum = SiacDEventoEnum.byEventoCodice(src.getEvento().getCodice());
			siacDEvento = eef.getEntity(siacDEventoEnum, dest.getSiacTEnteProprietario().getUid());
		} else {
			return dest;
		}
		
		SiacREventoRegMovfin siacREventoRegMovfin = new SiacREventoRegMovfin();
		siacREventoRegMovfin.setSiacTRegMovfin(dest);
		siacREventoRegMovfin.setSiacDEvento(siacDEvento);
		siacREventoRegMovfin.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacREventoRegMovfin.setCampoPkId(src.getMovimento().getUid());
		siacREventoRegMovfin.setCampoPkId2(src.getMovimentoCollegato()!=null?src.getMovimentoCollegato().getUid():null);
		siacREventoRegMovfin.setLoginOperazione(dest.getLoginOperazione());
		
		dest.setSiacREventoRegMovfins(new ArrayList<SiacREventoRegMovfin>());
		dest.addSiacREventoRegMovfin(siacREventoRegMovfin);
		return dest;
	}



	

}
