/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolo;

import java.math.BigDecimal;

import it.csi.siac.siacbilser.model.ClassificazioneCofogProgramma;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.test.TestBase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class CapitoloMainTestBase.
 */
public class CapitoloMainTestBase extends TestBase
{
	//PARAMETRI SCENARI DI TEST PER TUTTI I CAPITOLI
	/** The uid ente. */
	protected Integer uidEnte;
	
	/** The c fisc. */
	protected String cFisc;
  	
	  /** The uid bil. */
	  protected Integer uidBil;
   	
	   /** The anno bil. */
	   protected Integer annoBil;
	
	/** The anno eser. */
	protected Integer annoEser;	
	
	/** The anno crea. */
	protected Integer annoCrea;
	
	/** The anno capi. */
	protected Integer annoCapi;	

	/** The uid capi. */
	protected Integer uidCapi;	
	
	/** The num capi. */
	protected Integer numCapi;		
	
	/** The num arti. */
	protected Integer numArti;		
	
	/** The num ueb. */
	protected Integer numUEB;
   	
	/** The desc capi. */
	protected String descCapi;
   	
	   /** The stato capi. */
	   protected String statoCapi;   //StatoOperativoElementoDiBilancio.ANNULLATO
	   							//StatoOperativoElementoDiBilancio.PROVVISORIO
	   							//StatoOperativoElementoDiBilancio.VALIDO   
   	
	//private Integer ClCofog;		
   	/** The Macro aggr. */
							   	protected Integer MacroAggr;		
	
	/** The Progr. */
	protected Integer Progr;	
	
	/** The uid piano. */
	protected Integer uidPiano;		
	
	/** The uid strutt. */
	protected Integer uidStrutt;		
	
	/** The imp stanz. */
	protected BigDecimal impStanz;		
	
	/** The imp cassa. */
	protected BigDecimal impCassa;		
	
	/** The imp residuo. */
	protected BigDecimal impResiduo;	
	
	/** The imp stanz1. */
	protected BigDecimal impStanz1;		
	
	/** The imp cassa1. */
	protected BigDecimal impCassa1;		
	
	/** The imp residuo1. */
	protected BigDecimal impResiduo1;	
	
	/** The imp stanz2. */
	protected BigDecimal impStanz2;		
	
	/** The imp cassa2. */
	protected BigDecimal impCassa2;		
	
	/** The imp residuo2. */
	protected BigDecimal impResiduo2;	

	/** The cod pd c. */
	protected String codPdC;
	
	/** The cod strutt. */
	protected String codStrutt;
	
	/** The cod tip strutt. */
	protected String codTipStrutt;
	
	/** The cod miss. */
	protected String codMiss;
	
	/** The cod macro. */
	protected String codMacro;
	
	/** The pagina. */
	protected int pagina;
	
	/** The ele x pag. */
	protected int eleXPag;

	/** The uid var. */
	protected Integer uidVar;
	
	/** The anno comp stanz. */
	protected Integer annoCompStanz;
	
	/** The num var. */
	protected Integer numVar;
	
	/** The desc var imp. */
	protected String descVarImp;
	
	/** The note var. */
	protected String noteVar;

	/** The isannull. */
	protected boolean isannull;
	
	/** The iselimin. */
	protected boolean iselimin;	
	
	/** The stato var imp. */
	protected String statoVarImp;
	
	/** The uid dett var1. */
	protected Integer uidDettVar1;
	
	/** The uid dett var2. */
	protected Integer uidDettVar2;
	
	/** The uid dett var3. */
	protected Integer uidDettVar3;
	
	/** The importo. */
	protected Integer importo;
	
	/** The importo cassa. */
	protected Integer importoCassa;
	
	/** The importo residuo. */
	protected Integer importoResiduo;
	
	/** The stato prec. */
	protected String statoPrec; 
	
	/** The stato succ. */
	protected String statoSucc; 

	/** The tipodisp. */
	protected String tipodisp;
	
	/** The fase bil. */
	protected String faseBil;	
	
	/** The stato bil. */
	protected String statoBil;	

	/**
	 * Gets the ente test.
	 *
	 * @return the ente test
	 */
	protected Ente getEnteTest()
	{		
		try{
			Ente ente = new Ente();
			ente.setUid(uidEnte);
			return  ente;
		} catch(NullPointerException npe){
			return null;
		}		
	}

    /**
     * Gets the bilancio test.
     *
     * @return the bilancio test
     */
    protected Bilancio getBilancioTest(/*int uidBil, int annoBil*/)
	{
    	try {
	    	Bilancio bilancio = new Bilancio();
			bilancio.setUid(uidBil);
			bilancio.setAnno(annoBil);   	
			return bilancio;
    	}catch(NullPointerException npe){
    		return null;
    	}
	}
	
	/**
	 * Gets the macroaggregato.
	 *
	 * @param uidMacro the uid macro
	 * @return the macroaggregato
	 */
	protected Macroaggregato getMacroaggregato(Integer uidMacro)
	{
		try{
			Macroaggregato macroaggregato = new Macroaggregato();
			macroaggregato.setUid(uidMacro);	
			return macroaggregato;
		}catch(NullPointerException e){
			return null;
		}
	}
	
	/**
	 * Gets the programma.
	 *
	 * @param uidProgr the uid progr
	 * @return the programma
	 */
	protected Programma getProgramma(Integer uidProgr)
	{
		try{
			Programma programma = new Programma();
			programma.setUid(uidProgr);
			return programma;
		} catch (NullPointerException npe){
			return null;
		}
	}
	
	/**
	 * Gets the classificazione cofog programma.
	 *
	 * @param uidCofog the uid cofog
	 * @return the classificazione cofog programma
	 */
	protected ClassificazioneCofogProgramma getClassificazioneCofogProgramma(Integer uidCofog)
	{
		try{
			ClassificazioneCofogProgramma classificazioneCofogProgramma = new ClassificazioneCofogProgramma();
			classificazioneCofogProgramma.setUid(uidCofog);
			return classificazioneCofogProgramma;
		} catch (NullPointerException npe){
			return null;
		}		

	}

	/**
	 * Gets the parametri paginazione.
	 *
	 * @param Pagina the pagina
	 * @param EleXPag the ele x pag
	 * @return the parametri paginazione
	 */
	protected ParametriPaginazione getParametriPaginazione(int Pagina, int EleXPag)
	{
		ParametriPaginazione parametriPaginazione = null;

		if (Pagina >= 0 || EleXPag >= 0)
		{
			parametriPaginazione = new ParametriPaginazione();
			
			if (Pagina >= 0) parametriPaginazione.setNumeroPagina(Pagina);
			if (EleXPag >= 0) parametriPaginazione.setElementiPerPagina(EleXPag);
		}

		return parametriPaginazione;
	}
	
	/**
	 * Valorizzati parametri ricerca puntuale.
	 *
	 * @return the boolean
	 */
	protected Boolean valorizzatiParametriRicercaPuntuale() {
		Boolean valorizzatoTutto = false;
		
		if (statoCapi != null)
		{
			valorizzatoTutto = uidEnte != null && annoBil != null && annoEser != null 
				&& annoCapi != null && numCapi != null && numArti != null
				&& numUEB != null && cFisc != null;
		}
		else valorizzatoTutto = true;
		
		return valorizzatoTutto;
	}

	/**
	 * Gets the elemento piano dei conti.
	 *
	 * @param uidPiano the uid piano
	 * @return the elemento piano dei conti
	 */
	protected ElementoPianoDeiConti getElementoPianoDeiConti(Integer uidPiano) {
		try {
			ElementoPianoDeiConti elementoPianoDeiConti = new ElementoPianoDeiConti();
			elementoPianoDeiConti.setUid(uidPiano);
			return elementoPianoDeiConti;
		}catch(NullPointerException npe){
			return null;
		}
	}

	/**
	 * Gets the struttura amministrativo contabile.
	 *
	 * @param uidStrutt the uid strutt
	 * @return the struttura amministrativo contabile
	 */
	protected StrutturaAmministrativoContabile getStrutturaAmministrativoContabile(Integer uidStrutt)
	{		
		try {
			StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile();
			strutturaAmministrativoContabile.setUid(uidStrutt);
			return strutturaAmministrativoContabile;
		}catch(NullPointerException npe){
			return null;
		}
	}
}
