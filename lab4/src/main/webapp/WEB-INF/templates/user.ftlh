<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#-- @ftlvariable name="u" type="ru.itmo.web.hw4.model.User" -->
<#-- @ftlvariable name="uri" type="java.lang.String" -->

<#import "./common.ftlh" as c>

<@c.page uri>
    <#if handle??>
        <#assign u = c.findBy(users, "handle", handle)!/>
    <#elseif user_id?? && user_id?is_number>
        <#assign u = c.findBy(users, "id", user_id)!/>
    </#if>

    <#if u?has_content>
        <#assign position = c.findPositionBy(users, "handle", u.handle)!/>
        <#assign postsCount = c.countBy(posts, "user_id", u.id)!/>

        <div class="user-profile">
            <div class="left">
                <h1><a href="/user?handle=${u.handle}">${u.name}</a></h1>
                <ul>
                    <li>id: ${u.id}</li>
                    <li>handle: ${u.handle}</li>
                    <li>name: ${u.name}</li>
                </ul>
                <a href="/posts?user_id=${u.id}">${postsCount} post(s)</a>
            </div>
            <div class="right">
                <img src="/img/avatar.jpg" alt="avatar"/>
            </div>
        </div>
        <div class="arrows">
            <@arrow user_position=position-1 arrow_text="←"/>
            <@arrow user_position=position+1 arrow_text="→"/>
        </div>
    <#else>
        <h1>No such user</h1>
    </#if>
</@c.page>

<#macro arrow user_position arrow_text>
    <#if user_position != -1 && user_position != users?size>
        <a href="/user?handle=${users[user_position].handle}">${arrow_text}</a>
    <#else>
        <b>${arrow_text}</b>
    </#if>
</#macro>
