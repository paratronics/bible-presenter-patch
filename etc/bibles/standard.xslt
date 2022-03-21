<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:template match="/">
		<html>
			<head/>
			<body>
				<xsl:for-each select="XMLBIBLE">
					<h2 align="center">
						<xsl:for-each select="@biblename">
							<xsl:value-of select="."/>
						</xsl:for-each>
					</h2>
					<h5 align="center">by</h5>
					<h2>
						<xsl:for-each select="INFORMATION">
							<xsl:for-each select="publisher">
								<h5 align="center">
									<xsl:apply-templates/>
								</h5>
							</xsl:for-each>
						</xsl:for-each>
					</h2>
				</xsl:for-each>
				<br/>
				<br/>
				<xsl:for-each select="XMLBIBLE">
					<xsl:for-each select="BIBLEBOOK"> BIBLEBOOK <xsl:for-each select="@bnumber">
							<span style="font-size:larger">&#160;</span>
							<span style="font-size:larger">
								<xsl:value-of select="."/>
							</span>
						</xsl:for-each>
						<xsl:for-each select="@bname">
							<xsl:value-of select="."/>
						</xsl:for-each>
						<br/>
						<xsl:for-each select="CHAPTER">
							<br/>CHAPTER <xsl:for-each select="@cnumber">
								<span style="font-weight:bold">
									<xsl:value-of select="."/>
								</span>
							</xsl:for-each>
							<br/>
							<xsl:for-each select="VERS">
								<br/>
								<xsl:for-each select="@vnumber">
									<br/>
									<span style="font-size:large">
										<xsl:value-of select="."/>
									</span>
									<span style="font-size:large">&#160;</span>
									<br/>
								</xsl:for-each>
								<span style="font-size:large">&#160;</span>
								<div>
									<span style="font-size:large">
										<xsl:apply-templates/>
									</span>
								</div>
								<br/>
								<span style="font-size:large">&#160;&#160;&#160; </span>
							</xsl:for-each>
							<br/>
							<br/>
						</xsl:for-each>
						<br/>
					</xsl:for-each>
					<br/>
				</xsl:for-each>
				<br/>
				<br/>
				<br/>
				<xsl:for-each select="XMLBIBLE">
					<br/>
					<br/>
					<xsl:for-each select="INFORMATION">
						<xsl:for-each select="coverage">
							<xsl:apply-templates/>
						</xsl:for-each>
						<br/>
						<xsl:for-each select="creator">
							<xsl:apply-templates/>
						</xsl:for-each>
					</xsl:for-each>
				</xsl:for-each>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
