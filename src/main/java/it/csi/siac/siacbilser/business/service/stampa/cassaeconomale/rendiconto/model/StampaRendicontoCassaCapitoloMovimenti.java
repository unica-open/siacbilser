/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaRendicontoCassaCapitoloMovimenti {

		private Capitolo<?,?> capitolo;
		private Impegno impegno;
		private SubImpegno subImpegno;
	
		@XmlElements({
			@XmlElement(name="movimento", type=StampaRendicontoCassaMovimento.class),
		})	
		@XmlElementWrapper(name = "movimenti")
		private List<StampaRendicontoCassaMovimento> listMovimentos = new ArrayList<StampaRendicontoCassaMovimento>();
		
		private BigDecimal importoParziale = BigDecimal.ZERO; //totale per capitolo ex L1
		private BigDecimal importoEntrate = BigDecimal.ZERO; //totale per capitolo L1
		private BigDecimal importoUscite = BigDecimal.ZERO; //totale per capitolo L2
		private BigDecimal importoTotaleUsciteEntrate = BigDecimal.ZERO; //totale per capitolo L3
		private int numMovimentiCapitolo; //numero mov per capitolo
		
		//CR 2521
		private BigDecimal importoEntrateNOASM = BigDecimal.ZERO; //totale per capitolo senza ASM L1 
		private BigDecimal importoUsciteNOASM = BigDecimal.ZERO; //totale per capitolo senza ASM L2
		private BigDecimal importoTotaleUsciteEntrateNOASM = BigDecimal.ZERO; //totale per capitolo senza ASM L3
		private int numMovimentiCapitoloNOASM; //numero mov per capitolo
		
		private BigDecimal importoEntrateASM = BigDecimal.ZERO; //totale per capitolo solo ASM L1
		private BigDecimal importoUsciteASM = BigDecimal.ZERO; //totale per capitolo solo ASM L2
		private BigDecimal importoTotaleUsciteEntrateASM = BigDecimal.ZERO; //totale per capitolo solo ASM L3
		private int numMovimentiCapitoloASM; //numero mov per capitolo
	
		/**
		 * @return the capitolo
		 */
		public Capitolo<?, ?> getCapitolo() {
			return capitolo;
		}
		/**
		 * @param capitolo the capitolo to set
		 */
		public void setCapitolo(Capitolo<?, ?> capitolo) {
			this.capitolo = capitolo;
		}
		/**
		 * @return the impegno
		 */
		public Impegno getImpegno() {
			return impegno;
		}
		/**
		 * @param impegno the impegno to set
		 */
		public void setImpegno(Impegno impegno) {
			this.impegno = impegno;
		}
		/**
		 * @return the subImpegno
		 */
		public SubImpegno getSubImpegno() {
			return subImpegno;
		}
		/**
		 * @param subImpegno the subImpegno to set
		 */
		public void setSubImpegno(SubImpegno subImpegno) {
			this.subImpegno = subImpegno;
		}
		/**
		 * @return the listMovimentos
		 */
		@XmlTransient
		public List<StampaRendicontoCassaMovimento> getListMovimentos() {
			return listMovimentos;
		}
		/**
		 * @param listMovimentos the listMovimentos to set
		 */
		public void setListMovimentos(List<StampaRendicontoCassaMovimento> listMovimentos) {
			this.listMovimentos = listMovimentos;
		}
		/**
		 * @return the importoParziale
		 */
		public BigDecimal getImportoParziale() {
			return importoParziale;
		}
		/**
		 * @param importoParziale the importoParziale to set
		 */
		public void setImportoParziale(BigDecimal importoParziale) {
			this.importoParziale = importoParziale;
		}
		/**
		 * @return the numMovimentiCapitolo
		 */
		public int getNumMovimentiCapitolo() {
			return numMovimentiCapitolo;
		}
		/**
		 * @param numMovimentiCapitolo the numMovimentiCapitolo to set
		 */
		public void setNumMovimentiCapitolo(int numMovimentiCapitolo) {
			this.numMovimentiCapitolo = numMovimentiCapitolo;
		}
		/**
		 * @return the importoEntrate
		 */
		public BigDecimal getImportoEntrate() {
			return importoEntrate;
		}
		/**
		 * @param importoEntrate the importoEntrate to set
		 */
		public void setImportoEntrate(BigDecimal importoEntrate) {
			this.importoEntrate = importoEntrate;
		}
		/**
		 * @return the importoUscite
		 */
		public BigDecimal getImportoUscite() {
			return importoUscite;
		}
		/**
		 * @param importoUscite the importoUscite to set
		 */
		public void setImportoUscite(BigDecimal importoUscite) {
			this.importoUscite = importoUscite;
		}
		/**
		 * @return the importoTotaleUsciteEntrate
		 */
		public BigDecimal getImportoTotaleUsciteEntrate() {
			return importoTotaleUsciteEntrate;
		}
		/**
		 * @param importoTotaleUsciteEntrate the importoTotaleUsciteEntrate to set
		 */
		public void setImportoTotaleUsciteEntrate(
				BigDecimal importoTotaleUsciteEntrate) {
			this.importoTotaleUsciteEntrate = importoTotaleUsciteEntrate;
		}
		/**
		 * @return the importoEntrateNOASM
		 */
		public BigDecimal getImportoEntrateNOASM() {
			return importoEntrateNOASM;
		}
		/**
		 * @param importoEntrateNOASM the importoEntrateNOASM to set
		 */
		public void setImportoEntrateNOASM(BigDecimal importoEntrateNOASM) {
			this.importoEntrateNOASM = importoEntrateNOASM;
		}
		/**
		 * @return the importoUsciteNOASM
		 */
		public BigDecimal getImportoUsciteNOASM() {
			return importoUsciteNOASM;
		}
		/**
		 * @param importoUsciteNOASM the importoUsciteNOASM to set
		 */
		public void setImportoUsciteNOASM(BigDecimal importoUsciteNOASM) {
			this.importoUsciteNOASM = importoUsciteNOASM;
		}
		/**
		 * @return the importoTotaleUsciteEntrateNOASM
		 */
		public BigDecimal getImportoTotaleUsciteEntrateNOASM() {
			return importoTotaleUsciteEntrateNOASM;
		}
		/**
		 * @param importoTotaleUsciteEntrateNOASM the importoTotaleUsciteEntrateNOASM to set
		 */
		public void setImportoTotaleUsciteEntrateNOASM(
				BigDecimal importoTotaleUsciteEntrateNOASM) {
			this.importoTotaleUsciteEntrateNOASM = importoTotaleUsciteEntrateNOASM;
		}
		/**
		 * @return the numMovimentiCapitoloNOASM
		 */
		public int getNumMovimentiCapitoloNOASM() {
			return numMovimentiCapitoloNOASM;
		}
		/**
		 * @param numMovimentiCapitoloNOASM the numMovimentiCapitoloNOASM to set
		 */
		public void setNumMovimentiCapitoloNOASM(int numMovimentiCapitoloNOASM) {
			this.numMovimentiCapitoloNOASM = numMovimentiCapitoloNOASM;
		}
		/**
		 * @return the importoEntrateASM
		 */
		public BigDecimal getImportoEntrateASM() {
			return importoEntrateASM;
		}
		/**
		 * @param importoEntrateASM the importoEntrateASM to set
		 */
		public void setImportoEntrateASM(BigDecimal importoEntrateASM) {
			this.importoEntrateASM = importoEntrateASM;
		}
		/**
		 * @return the importoUsciteASM
		 */
		public BigDecimal getImportoUsciteASM() {
			return importoUsciteASM;
		}
		/**
		 * @param importoUsciteASM the importoUsciteASM to set
		 */
		public void setImportoUsciteASM(BigDecimal importoUsciteASM) {
			this.importoUsciteASM = importoUsciteASM;
		}
		/**
		 * @return the importoTotaleUsciteEntrateASM
		 */
		public BigDecimal getImportoTotaleUsciteEntrateASM() {
			return importoTotaleUsciteEntrateASM;
		}
		/**
		 * @param importoTotaleUsciteEntrateASM the importoTotaleUsciteEntrateASM to set
		 */
		public void setImportoTotaleUsciteEntrateASM(
				BigDecimal importoTotaleUsciteEntrateASM) {
			this.importoTotaleUsciteEntrateASM = importoTotaleUsciteEntrateASM;
		}
		/**
		 * @return the numMovimentiCapitoloASM
		 */
		public int getNumMovimentiCapitoloASM() {
			return numMovimentiCapitoloASM;
		}
		/**
		 * @param numMovimentiCapitoloASM the numMovimentiCapitoloASM to set
		 */
		public void setNumMovimentiCapitoloASM(int numMovimentiCapitoloASM) {
			this.numMovimentiCapitoloASM = numMovimentiCapitoloASM;
		}
		
		
}
