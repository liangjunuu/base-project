<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
<h2>Hello World!</h2>
数量：${count}
abc=${abc}
<c:forEach var="item" items="${list}">
    ${item}<br/>
</c:forEach>

-----MAP-----
${map.a}<br/>
${map.b}<br/>
<script type="text/javascript" src="http://10.1.70.24:8099/base-project/static/plugins/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
    $(function(){
        getIndex();
    });

    function getIndex(){
        $.ajax({
            type : "get",
            url : "http://10.1.70.24:8099/base-project/index/getIndex",
            cache : false,
            dataType : "text",
            data:{},
            success : function(data) {
                alert(data);
                var abc = data
                getIndex2(abc);
            },
            error:function(){
                alert("出错了！！！");
            }
        });
    }

    function getIndex2(abc){
        $.ajax({
            type : "get",
            url : "http://10.1.70.24:8099/base-project/index/getIndex?timestamp="+new Date().getTime(),
            dataType : "json",
            data:{},
            beforeSend: function(request) {
                request.setRequestHeader("abc", abc);
            },
            success : function(data) {
                alert(data);
            },
            error:function(){
                //alert("出错了！！！");
            }
        });
    }

</script>
</body>
</html>
