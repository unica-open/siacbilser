/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

import it.csi.siac.siacbilser.integration.entity.SiacRPdceContoClass;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassFamEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siacgenser.model.CodiceBilancio;
import it.csi.siac.siacgenser.model.Conto;


public abstract class BaseContoClassifConverter extends ExtendedDozerConverter<Conto, SiacTPdceConto> {
	
	public BaseContoClassifConverter() {
		super(Conto.class, SiacTPdceConto.class);
	}

	@Override
	public Conto convertFrom(SiacTPdceConto src, Conto dest) {
		
		
		for(SiacRPdceContoClass siacRPdceContoClass : src.getSiacRPdceContoClasses()){
			if(siacRPdceContoClass.getDataCancellazione()!=null
					|| !siacRPdceContoClass.isDataValiditaCompresa(dest.getDataInizioValiditaFiltro())){
				continue;
			}
			
			Set<SiacDClassFamEnum> siacDClassFamEnumSet = EnumSet.of(
					SiacDClassFamEnum.CodiceBilancioContoEconomico,
					SiacDClassFamEnum.CodiceBilancioStatoPatrimonialeAttivo,
					SiacDClassFamEnum.CodiceBilancioStatoPatrimonialePassivo,
					SiacDClassFamEnum.CodiceBilancioContiDOrdine,
					SiacDClassFamEnum.CodiceBilancioContoEconomicoGsa,
					SiacDClassFamEnum.CodiceBilancioStatoPatrimonialeAttivoGsa,
					SiacDClassFamEnum.CodiceBilancioStatoPatrimonialePassivoGsa,
					SiacDClassFamEnum.CodiceBilancioContiDOrdineGsa
				);
			
			SiacTClass siacTClass = siacRPdceContoClass.getSiacTClass();
			
			String classifTipoCode = siacTClass.getSiacDClassTipo().getClassifTipoCode();
			SiacDClassTipoEnum tipo = SiacDClassTipoEnum.byCodice(classifTipoCode);
			if(siacDClassFamEnumSet.contains(tipo.getFamiglia())){
				CodiceBilancio codiceBilancio = tipo.getCodificaInstance();
				mapToCodiceBilancio(siacTClass, codiceBilancio);
				dest.setCodiceBilancio(codiceBilancio);
				
			} else if(tipo.getFamiglia() == SiacDClassFamEnum.PianoDeiConti){
				ElementoPianoDeiConti elementoPianoDeiConti = tipo.getCodificaInstance();
				map(siacTClass,elementoPianoDeiConti,BilMapId.SiacTClass_ClassificatoreGerarchico);
				dest.setElementoPianoDeiConti(elementoPianoDeiConti);
				
			}
		}
		
		
		return dest;
	}

	protected abstract void mapToCodiceBilancio(SiacTClass siacTClass, CodiceBilancio codiceBilancio);

	@Override
	public SiacTPdceConto convertTo(Conto src, SiacTPdceConto dest) {
		
		dest.setSiacRPdceContoClasses(new ArrayList<SiacRPdceContoClass>());
		
		addClassif(dest, src.getCodiceBilancio());
		addClassif(dest, src.getElementoPianoDeiConti());
		
		return dest;
	}

	/**
	 * Adds the classif.
	 *
	 * @param dest the dest
	 * @param src the src
	 */
	private void addClassif(SiacTPdceConto dest, Codifica src) {
		
		if(src==null || src.getUid()==0){
			return;
		}
		
		SiacRPdceContoClass siacRPdceContoClass = new SiacRPdceContoClass();
		siacRPdceContoClass.setSiacTPdceConto(dest);
		SiacTClass siacTClass = new SiacTClass();
		siacTClass.setUid(src.getUid());	
		siacRPdceContoClass.setSiacTClass(siacTClass);
		
		siacRPdceContoClass.setLoginOperazione(dest.getLoginOperazione());
		siacRPdceContoClass.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRPdceContoClass(siacRPdceContoClass);
	}

}
