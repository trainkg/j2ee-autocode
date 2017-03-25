<!-- Generated ${date} by zsq tools -->
<#if form.configForm.sections??>
<#foreach section in form.configForm.sections>
	<#include "${section.template}">
</#foreach>
</#if>