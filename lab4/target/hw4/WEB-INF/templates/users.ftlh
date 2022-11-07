<#-- @ftlvariable name="uri" type="java.lang.String" -->

<#import "./common.ftlh" as c>

<@c.page uri>
    <div class="datatable" xmlns="http://www.w3.org/1999/html">
        <div class="caption">Users</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Name</th>
                <th>Handle</th>
            </tr>
            </thead>
            <tbody>
            <#list users as user>
                <tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td><@c.userlink user false/></td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>

</@c.page>
