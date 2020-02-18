<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:lxslt="http://xml.apache.org/xslt"
    xmlns:redirect="http://xml.apache.org/xalan/redirect"
	xmlns:exslt="http://exslt.org/common"
	xmlns:str="http://exslt.org/strings"	
	
    xmlns:stringutils="xalan://org.apache.tools.ant.util.StringUtils"
    extension-element-prefixes="redirect">


<xsl:import href='junit-frames.originale.xsl'/>

<!--
<xsl:include href='step-parser.xsl'/>
-->

<xsl:param name='ALLSTEPS-URI'>specify steps files</xsl:param>
<xsl:param name='allsteps'>specify steps files</xsl:param>

<xsl:template name='create-step-list'>
	<steps>
		<xsl:for-each select="str:tokenize($allsteps,';:')">
			<step><xsl:value-of select='.'/></step>
		</xsl:for-each>
	</steps>
</xsl:template>

<!-- method header -->
<xsl:template name="testcase.test.header">
    <xsl:param name="show.class" select="''"/>
    <tr valign="top">
	<xsl:if test="boolean($show.class)">
	    <th>Class</th>
	</xsl:if>
        <th>Name</th>
        <th>Steps</th>
        <th>Status</th>
        <th width="80%">Type</th>
        <th nowrap="nowrap">Time(s)</th>
        <th>Timestamp</th>
    </tr>
</xsl:template>

<xsl:template match="testcase" mode="print.test">
    <xsl:param name="show.class" select="''"/>
    <tr valign="top">
        <xsl:attribute name="class">
            <xsl:choose>
                <xsl:when test="error">Error</xsl:when>
                <xsl:when test="failure">Failure</xsl:when>
                <xsl:otherwise>TableRowColor</xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
	<xsl:variable name="class.href">
	    <xsl:value-of select="concat(translate(../@package,'.','/'), '/', ../@id, '_', ../@name, '.html')"/>
	</xsl:variable>
	<xsl:if test="boolean($show.class)">
	    <td><a href="{$class.href}"><xsl:value-of select="../@name"/></a></td>
	</xsl:if>
        <td>
		    <a name="{@name}"/>
		    <xsl:choose>
				<xsl:when test="boolean($show.class)">
				    <a href="{concat($class.href, '#', @name)}"><xsl:value-of select="@name"/></a>
				</xsl:when>
				<xsl:otherwise>
				    <xsl:value-of select="@name"/>
				</xsl:otherwise>
		    </xsl:choose>
		</td>
		<td>
        	<xsl:variable name="pfx" select='concat(../@name,"-",@name,"-")' />
			<ul style="font-size: small;">
				<xsl:variable name='all_steps'>
					<xsl:call-template name="create-step-list">
					</xsl:call-template>					
				</xsl:variable>
				
	        	<xsl:for-each select="exslt:node-set($all_steps)//step">
	        		<xsl:if test="starts-with(.,$pfx)">
	        			<li style='list-style-type: none;'>
	        				<a href='${output.dir}/../../../raw/{.}'>
	        					<xsl:value-of select='stringutils:replace(substring-after(.,$pfx),"_"," ")'/>	        					
	        				</a>
	        			</li>
	        		</xsl:if> 
	        	</xsl:for-each>
	        	
        	</ul>		
		</td>
		
		<xsl:variable name="rtf">
			<xsl:value-of select="failure/text()"></xsl:value-of>
      	</xsl:variable>
        <xsl:choose>
            <xsl:when test="failure">
                <td>Failure</td>
                <td>
                	<xsl:value-of select="failure" disable-output-escaping="yes"></xsl:value-of>                
                </td>
            </xsl:when>
            <xsl:when test="error">
                <td>Error</td>
                <td>
                	<xsl:apply-templates select="error"/>
                </td>
            </xsl:when>
            <xsl:otherwise>
                <td>Success</td>
                <td></td>
            </xsl:otherwise>
        </xsl:choose>
        <td>
            <xsl:call-template name="display-time">
                <xsl:with-param name="value" select="@time"/>
            </xsl:call-template>
        </td>
    </tr>
</xsl:template>

<!-- 
<xsl:template match="failure">
    <xsl:call-template name="display-failures"/>
</xsl:template>

<xsl:template match="error">
    <xsl:call-template name="display-failures"/>
</xsl:template>
-->

</xsl:stylesheet>