<#-- @ftlvariable name="uri" type="java.lang.String" -->
<#-- @ftlvariable name="user_id" type="java.lang.Long" -->

<#import "./common.ftlh" as c>

<@c.page uri>
    <#if user_id??>
        <#assign count = c.countBy(posts, "user_id", user_id)/>

        <#if count == 0>
            <h2>Posts not found</h2>
        <#else>
            <#list posts?reverse as p>
                <#if user_id == p.user_id>
                    <@c.post p true/>
                </#if>
            </#list>
        </#if>
    <#else>
        <h1>User not found</h1>
    </#if>
</@c.page>
