<#-- @ftlvariable name="uri" type="java.lang.String" -->
<#-- @ftlvariable name="post_id" type="java.lang.Long" -->
<#-- @ftlvariable name="p" type="ru.itmo.web.hw4.model.Post" -->
<#import "./common.ftlh" as c>

<@c.page uri>
    <#if post_id?? && post_id?is_number>
        <#assign p = c.findBy(posts, "id", post_id)!/>
        <@c.post p false />
    <#else>
        <h1>Post not found.</h1>
    </#if>
</@c.page>
