/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfinser.model.ElementoContoEconomico;
import it.csi.siac.siacgenser.model.CodiceBilancio;

// TODO: Auto-generated Javadoc
/**
 * 
 * 		//PianoDeiConti			 			-> PrimoLivelloPDC,SecondoLivelloPDC,TerzoLivelloPDC...QuintoLivelloPDC
		//StrutturaAmministrativaContabile 	-> Centro di Responsabilità, CDC (Settore)
		//SpesaMissioniprogrammi			-> Missione, Programma (solo Uscita)
		//SpesaTitolimacroaggregati			-> TitoloSpesa, Macroaggregato		  (solo Uscita)
		//EntrataTitolitipologiecategorie	-> TitoloEntrata, TipologiaTitolo, CategoriaTipologiaTitolo (solo Entrata)
 * 
 * 
 * @author Domenico
 *
 */
@EnumEntity(entityName="SiacDClassFam", idPropertyName="classifFamId", codePropertyName="classifFamCode")
public enum SiacDClassFamEnum {
	
	SpesaMissioniprogrammi("00001", Programma.class),
	SpesaTitolimacroaggregati("00002", Macroaggregato.class),
	EntrataTitolitipologiecategorie("00003", CategoriaTipologiaTitolo.class),
	StrutturaAmministrativaContabile("00005", StrutturaAmministrativoContabile.class),
	PianoDeiConti("00008", ElementoPianoDeiConti.class),
	Cofog("00009", ClassificazioneCofog.class), 
	SiopeEntrata("00016", SiopeEntrata.class),
	SiopeSpesa("00017", SiopeSpesa.class),
	
	@Deprecated ContoEconomico("00018", ElementoContoEconomico.class),//Utilizzato da FIN
	
	CodiceBilancioContoEconomico("00020", CodiceBilancio.class),
	CodiceBilancioStatoPatrimonialeAttivo("00021", CodiceBilancio.class),
	CodiceBilancioStatoPatrimonialePassivo("00022", CodiceBilancio.class),
	CodiceBilancioContiDOrdine("00023", CodiceBilancio.class),

	// SIAC-5551
	CodiceBilancioContoEconomicoGsa("00024", CodiceBilancio.class),
	CodiceBilancioStatoPatrimonialePassivoGsa("00025", CodiceBilancio.class),
	CodiceBilancioStatoPatrimonialeAttivoGsa("00026", CodiceBilancio.class),
	// non presente su base dati
	CodiceBilancioContiDOrdineGsa("00027", CodiceBilancio.class),
	;

	
	
	/** The codice. */
	private final String codice;
	
	/** The codifica class. */
	private final Class<? extends Codifica> codificaClass;
		

	/**
	 * Instantiates a new siac d class fam enum.
	 *
	 * @param codice the codice
	 * @param codificaClass the codifica class
	 */
	SiacDClassFamEnum(String codice, Class<? extends Codifica> codificaClass){		
		this.codice = codice;
		this.codificaClass = codificaClass;
	}

	

	/**
	 * Gets the codice.
	 *
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * Gets the codifica class.
	 *
	 * @return the codifica class
	 */
	public Class<? extends Codifica> getCodificaClass() {
		return codificaClass;
	}
	
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d class fam enum
	 */
	public static SiacDClassFamEnum byCodice(String codice){
		for(SiacDClassFamEnum e : SiacDClassFamEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDClassFamEnum");
	}
	
	/**
	 * Gets the codifica instance.
	 *
	 * @param <T> the generic type
	 * @return the codifica instance
	 */
	@SuppressWarnings("unchecked")
	public <T extends Codifica> T getCodificaInstance() {
		try {
			return (T) codificaClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Eccezione istanziamento SiacDClassTipo."+name()+"->"+codificaClass + " ["+codice+"]",e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Impossibile accedere al costruttore vuoto di  "+codificaClass + " ["+codice+"]",e);
		}
	}
	

	public static Set<SiacDClassFamEnum> byTipologiaClassificatore(Collection<TipologiaClassificatore> tipologiaClassificatores){
		Set<SiacDClassTipoEnum> classTipiEnum = EnumSet.noneOf(SiacDClassTipoEnum.class);
		
		for(TipologiaClassificatore tipologiaClassificatore : tipologiaClassificatores){
			classTipiEnum.add(SiacDClassTipoEnum.byTipologiaClassificatore(tipologiaClassificatore));
		}
		
		Set<SiacDClassFamEnum> classFamEnum = EnumSet.noneOf(SiacDClassFamEnum.class);
		for(SiacDClassTipoEnum tipologiaClassificatore : classTipiEnum){
			classFamEnum.add(tipologiaClassificatore.getFamiglia());
		}
		
		return classFamEnum;
	}
	

}
