<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:ui="https://github.com/bordertech/wcomponents/namespace/ui/v1.0" 
	xmlns:html="http://www.w3.org/1999/xhtml" version="1.0">
	<xsl:import href="wc.common.selectToggle.xsl"/>
	<!--
		This template creates the rowSelection (select all, select none) controls if required. It is called explicitly 
		from the template named `topControls`. If there are no selectable rows then nothing is output.

		NOTE: This template does not make the individual rows selectable. That is done in the transform of ui:tr.

		Structural: do not override.
	-->
	<xsl:template match="ui:rowselection">
		<xsl:variable name="tableId" select="../@id"/>
		<xsl:variable name="numberOfRows" select="count(..//ui:tr[not(@unselectable) and ancestor::ui:table[1]/@id = $tableId])"/>
		<xsl:if test="$numberOfRows &gt; 0">
			<xsl:variable name="numberSelectedRows" select="count(..//ui:tr[@selected and ancestor::ui:table[1]/@id = $tableId])"/>
			<xsl:variable name="selected">
				<xsl:choose>
					<xsl:when test="$numberSelectedRows = 0">
						<xsl:text>none</xsl:text>
					</xsl:when>
					<xsl:when test="@toggle">
						<!-- 
							When in parent row is a select toggle mode any row which is selected but has descendant rows
							(in the same table)  which are not selected is **deemed to be unselected**.

							This is a horrible calculation and I wish I did not have to do it.
						-->
						<xsl:variable name="numberUnselectedParentRows" 
							select="count(..//ui:tr[@selected and 
								ancestor::ui:table[1]/@id = $tableId and 
								.//ui:subtr[ancestor::ui:table[1]/@id = $tableId]/ui:tr[not(@unselectable or @selected)]])"/>
						<xsl:choose>
							<xsl:when test="$numberSelectedRows = $numberUnselectedParentRows">
								<xsl:text>none</xsl:text>
							</xsl:when>
							<xsl:when test="$numberUnselectedParentRows = 0 and $numberSelectedRows = $numberOfRows">
								<xsl:text>all</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>some</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$numberOfRows = $numberSelectedRows">
						<xsl:text>all</xsl:text>
					</xsl:when>
					<xsl:when test="count(..//ui:tr[@selected])=0">
						<xsl:text>none</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>some</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:variable name="controlLabel">
				<xsl:if test="@selectAll='control'">
					<xsl:value-of select="$$${wc.ui.table.string.rowSelection.label}"/>
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="bodyId" select="concat(../@id,'_tb')"/>
			<xsl:call-template name="selectToggle">
				<xsl:with-param name="for" select="$bodyId"/>
				<xsl:with-param name="id" select="$bodyId"/>
				<xsl:with-param name="selected" select="$selected"/>
				<xsl:with-param name="label" select="$controlLabel"/>
				<xsl:with-param name="type" select="@selectAll"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<!--
		Outputs a comma separated list of JSON objects required for registering
		the selection controls. See wc.common.registrationScripts.xsl.
	-->
	<xsl:template match="ui:rowselection" mode="JS">
		<xsl:text>{"identifier":"</xsl:text>
		<xsl:value-of select="concat(../@id,'_tb','_st')"/>
		<xsl:text>","groupName":"</xsl:text>
		<xsl:value-of select="concat(../@id,'_tb')"/>
		<xsl:text>"}</xsl:text>
		<xsl:if test="position() != last()">
			<xsl:text>,</xsl:text>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
