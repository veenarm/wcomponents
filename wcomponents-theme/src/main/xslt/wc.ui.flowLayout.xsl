<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:ui="https://github.com/bordertech/wcomponents/namespace/ui/v1.0" 
	xmlns:html="http://www.w3.org/1999/xhtml" version="1.0">
	<xsl:import href="wc.common.getHVGap.xsl"/>
	<xsl:import href="wc.common.n.className.xsl"/>
	<!--
		ui:flowlayout is one of the possible child elements of WPanel

		A flowLayout is used to place elements in a particular linear relationship to
		each other using the align property.
	-->
	<xsl:template match="ui:flowlayout">
		<xsl:variable name="class">
			<xsl:if test="@valign">
				<xsl:value-of select="concat('wc_fl_', @valign)"/>
			</xsl:if>
			<xsl:call-template name="getHVGapClass">
				<xsl:with-param name="isVGap">
					<xsl:if test="@align='vertical'">
						<xsl:number value="1"/>
					</xsl:if>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<div>
			<xsl:call-template name="makeCommonClass">
				<xsl:with-param name="additional" select="$class"/>
			</xsl:call-template>
			<xsl:apply-templates select="ui:cell[node()]" mode="fl"/>
		</div>
	</xsl:template>
</xsl:stylesheet>
