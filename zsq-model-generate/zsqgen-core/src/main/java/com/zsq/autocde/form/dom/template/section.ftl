<!--model section-->
<div class="m-section panel panel-default">
	<#if section.title?exists>
	<div class="panel-heading">
		${section.title}
	</div>	
	</#if>
	<div class="panel-body">
		<div class="row">
		<#foreach element in section.inputs>
			<#include "${element.template}">
		</#foreach>
		</div>
	</div>
</div>
