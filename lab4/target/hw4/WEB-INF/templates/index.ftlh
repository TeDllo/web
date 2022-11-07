<#-- @ftlvariable name="uri" type="java.lang.String" -->

<#import "./common.ftlh" as c>

<@c.page uri>
    <#list posts?reverse as p>
        <@c.post p true/>
    </#list>
</@c.page>
