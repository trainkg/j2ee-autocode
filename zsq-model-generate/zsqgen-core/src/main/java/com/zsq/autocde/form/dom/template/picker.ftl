<%@ page language="java" contentType="text/html; charset=${encoding}" pageEncoding="${encoding}"%>
<!--${date} gen by auto code & project:${projectName}-->
<#assign classbody>
<#if form?? && form.configForm.sections??>
<#include "${form.configForm.template}"/>
</#if>
</#assign>
${classbody}