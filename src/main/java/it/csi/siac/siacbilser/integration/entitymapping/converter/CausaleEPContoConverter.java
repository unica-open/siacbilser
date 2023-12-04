/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDConciliazioneClasse;
import it.csi.siac.siacbilser.integration.entity.SiacDOperazioneEp;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleEpPdceConto;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleEpPdceContoOper;
import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneClasseCausaleEp;
import it.csi.siac.siacbilser.integration.entity.SiacTCausaleEp;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDConciliazioneClasseEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOperazioneEpEnum;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.Operazione;

/**
 * The Class CausaleEPContoConverter.
 *
 * @author Domenico
 */
@Component
public class CausaleEPContoConverter extends ExtendedDozerConverter<CausaleEP, SiacTCausaleEp > {
	
	@Autowired
	private EnumEntityFactory eef;
	

	public CausaleEPContoConverter() {
		super(CausaleEP.class, SiacTCausaleEp.class);
	}

	@Override
	public CausaleEP convertFrom(SiacTCausaleEp src, CausaleEP dest) {
		for(SiacRCausaleEpPdceConto r : src.getSiacRCausaleEpPdceContos()){
			Date dataInizioValiditaFiltro = dest.getDataInizioValiditaFiltro() != null? dest.getDataInizioValiditaFiltro() : Utility.primoGiornoDellAnno(Utility.BTL.get().getAnno());
			if(r.getDataCancellazione()!=null || !r.isDataValiditaCompresa(dataInizioValiditaFiltro)){
				continue;
			}
			
			ContoTipoOperazione contoTipoOperazione = new ContoTipoOperazione();
			
			SiacTPdceConto siacTPdceConto = r.getSiacTPdceConto();
			if(siacTPdceConto != null){
				Conto conto = new Conto();
				conto.setDataInizioValiditaFiltro(dest.getDataInizioValiditaFiltro());
				map(siacTPdceConto, conto, getMapIdConto());
				contoTipoOperazione.setConto(conto);
			}
			
			
			if(r.getSiacRCausaleEpPdceContoOpers() != null){
				for(SiacRCausaleEpPdceContoOper rOper : r.getSiacRCausaleEpPdceContoOpers()){
					if(rOper.getDataCancellazione() != null || !rOper.isDataValiditaCompresa(dataInizioValiditaFiltro)){
						continue;
					}
					SiacDOperazioneEp siacDOperazioneEp = rOper.getSiacDOperazioneEp();
					
					String codiceComposto = siacDOperazioneEp.getSiacDOperazioneEpTipo().getOperEpTipoCode() + "_" +siacDOperazioneEp.getOperEpCode();
					Operazione operazione = SiacDOperazioneEpEnum.byCodice(codiceComposto).getOperazione();
					contoTipoOperazione.setOperazione(operazione);
				}
			}
			
			//new SIAC-4596
			if(r.getSiacRConciliazioneClasseCausaleEps() != null){
				for(SiacRConciliazioneClasseCausaleEp rConc : r.getSiacRConciliazioneClasseCausaleEps()){
					if(rConc.getDataCancellazione() != null || !rConc.isDataValiditaCompresa(dataInizioValiditaFiltro)){
						continue;
					}
					
					SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum = SiacDConciliazioneClasseEnum.byCodice(rConc.getSiacDConciliazioneClasse().getConcclaCode());
					contoTipoOperazione.setClasseDiConciliazione(siacDConciliazioneClasseEnum.getClasseDiConciliazione());
					break;
				}
			}
			
			dest.addContoTipoOperazione(contoTipoOperazione);
		}
		
		return dest;
	}

	protected GenMapId getMapIdConto() {
		return GenMapId.SiacTPdceConto_Conto_Base;
	}
	

	@Override
	public SiacTCausaleEp convertTo(CausaleEP src, SiacTCausaleEp dest) {
		dest.setSiacRCausaleEpPdceContos(new ArrayList<SiacRCausaleEpPdceConto>());
		
		if(src.getContiTipoOperazione()==null){
			return dest;
		}
		
		for(ContoTipoOperazione contoTipoOperazione : src.getContiTipoOperazione()){

			addConto(dest, src.getLoginOperazione(), contoTipoOperazione);
		}
		
		
		
		return dest;
	}

	private void addConto(SiacTCausaleEp dest, String loginOperazione, ContoTipoOperazione contoTipoOperazione) {
		
		SiacRCausaleEpPdceConto siacRCausaleEpPdceConto = new SiacRCausaleEpPdceConto();
		
		SiacTPdceConto siacTPdceConto = new SiacTPdceConto();
		if(contoTipoOperazione.getConto() != null && contoTipoOperazione.getConto().getUid() != 0){
			siacTPdceConto.setUid(contoTipoOperazione.getConto().getUid());
			siacRCausaleEpPdceConto.setSiacTPdceConto(siacTPdceConto);
		}
		
		siacRCausaleEpPdceConto.setSiacRCausaleEpPdceContoOpers(new ArrayList<SiacRCausaleEpPdceContoOper>());
		
		addOperazione(siacRCausaleEpPdceConto, dest.getSiacTEnteProprietario(), loginOperazione, contoTipoOperazione.getOperazioneSegnoConto());
		addOperazione(siacRCausaleEpPdceConto, dest.getSiacTEnteProprietario(), loginOperazione, contoTipoOperazione.getOperazioneTipoImporto());
		addOperazione(siacRCausaleEpPdceConto, dest.getSiacTEnteProprietario(), loginOperazione, contoTipoOperazione.getOperazioneUtilizzoConto());
		addOperazione(siacRCausaleEpPdceConto, dest.getSiacTEnteProprietario(), loginOperazione, contoTipoOperazione.getOperazioneUtilizzoImporto());
		
		
		siacRCausaleEpPdceConto.setLoginOperazione(loginOperazione);
		siacRCausaleEpPdceConto.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		
		//Classse di conciliazione
		if(contoTipoOperazione.getClasseDiConciliazione() != null){
			SiacRConciliazioneClasseCausaleEp siacRConciliazioneClasseCausaleEp = new SiacRConciliazioneClasseCausaleEp();
			siacRConciliazioneClasseCausaleEp.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			SiacDConciliazioneClasse siacDConciliazioneClasse = eef.getEntity(SiacDConciliazioneClasseEnum.byClasseDiConciliazione(contoTipoOperazione.getClasseDiConciliazione()), dest.getSiacTEnteProprietario().getUid(), SiacDConciliazioneClasse.class);
			siacRConciliazioneClasseCausaleEp.setSiacDConciliazioneClasse(siacDConciliazioneClasse );
			siacRConciliazioneClasseCausaleEp.setLoginOperazione(dest.getLoginOperazione());		
			siacRCausaleEpPdceConto.setSiacRConciliazioneClasseCausaleEps(new ArrayList<SiacRConciliazioneClasseCausaleEp>());
			siacRCausaleEpPdceConto.addSiacRConciliazioneClasseCausaleEp(siacRConciliazioneClasseCausaleEp);
		}
		
		dest.addSiacRCausaleEpPdceConto(siacRCausaleEpPdceConto);
	}
	

	private void addOperazione(SiacRCausaleEpPdceConto siacRCausaleEpPdceConto, SiacTEnteProprietario siacTEnteProprietario, String loginOperazione,
			Enum<? extends Operazione>  operazione) {
		if (operazione != null) {
			String methodName = "addOperazione";
			
			SiacRCausaleEpPdceContoOper siacRCausaleEpPdceContoOper = new SiacRCausaleEpPdceContoOper();
			
			SiacDOperazioneEpEnum siacDOperazioneEpEnum = SiacDOperazioneEpEnum.byOperazione((Operazione) operazione);
			log.debug(methodName, "Operazione: "+ siacDOperazioneEpEnum);
			
			SiacDOperazioneEp siacDOperazioneEp = eef.getEntity(siacDOperazioneEpEnum, siacTEnteProprietario.getUid());
			siacRCausaleEpPdceContoOper.setSiacDOperazioneEp(siacDOperazioneEp);
			
			siacRCausaleEpPdceContoOper.setLoginOperazione(loginOperazione);
			siacRCausaleEpPdceContoOper.setSiacTEnteProprietario(siacTEnteProprietario);
			
			siacRCausaleEpPdceConto.addSiacRCausaleEpPdceContoOper(siacRCausaleEpPdceContoOper);
		}
	}



	

}
