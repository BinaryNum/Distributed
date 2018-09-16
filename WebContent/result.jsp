<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="style.css">
<title>Insert title here</title>
</head>
<body>

 <div id="vis" style="width:90%;height:50%"></div>

    <form id="form">
        <p><label for="text">아래 텍스트 상자에 원하는 글을 붙여 넣으세요!</label></p>
        <p><textarea id="text" style="display:none;"><c:forEach items="${list}" begin="0"  end="200" var="dto"><c:forEach begin="0" end="${dto.count / 30}">${dto.word } </c:forEach></c:forEach>
            </textarea>
            
            <button id="gen" type="submit">Go!</button> <button id="control">컨트롤 판넬 토글</button>
        </p>

        <hr>

        <div id="ctlView" style="display:none">
            <div style="float: right; text-align: right">
                <p><label for="max">Number of words:</labl> <input type="number" value="250" min="1" id="max"></p>
                <p><label for="per-line"><input type="checkbox" id="per-line"> One word per line</label></p>
                <p><label>Download:</label>
                    <button id="download-svg">SVG</button>
                </p>
            </div>

            <div style="float: left">
                <p><label>Spiral:</label>
                    <label for="archimedean"><input type="radio" name="spiral" id="archimedean" value="archimedean" checked="checked"> Archimedean</label>
                    <label for="rectangular"><input type="radio" name="spiral" id="rectangular" value="rectangular"> Rectangular</label>
                </p>
                <p><label for="scale">Scale:</label>
                    <label for="scale-log"><input type="radio" name="scale" id="scale-log" value="log" checked="checked"> log n</label>
                    <label for="scale-sqrt"><input type="radio" name="scale" id="scale-sqrt" value="sqrt"> √n</label>
                    <label for="scale-linear"><input type="radio" name="scale" id="scale-linear" value="linear"> n</label>
                </p>
                <p><label for="font">Font:</label> <input type="text" id="font" value="Nanum">
                </p>
            </div>

            <div id="angles">
                <p><input type="number" id="angle-count" value="5" min="1"> <label for="angle-count">orientations</label>
                    <label for="angle-from">from</label> <input type="number" id="angle-from" value="-60" min="-90" max="90"> °
                    <label for="angle-to">to</label> <input type="number" id="angle-to" value="60" min="-90" max="90"> °
                </p><svg width="151" height="70.5"><g transform="translate(75.5,60.5)"><path d="M -40.5 0 A 40.5 40.5 0 0 1 40.5 0" style="fill: none;">
                </path><line x1="-47.5" x2="47.5"></line><line y2="-47.5"></line>
                <text dy=".3em" text-anchor="end" transform="rotate(0)translate(-50.5)rotate(0)translate(2)">-90°</text>
                <text text-anchor="middle" transform="rotate(90)translate(-50.5)rotate(-90)translate(2)">0°</text>
                <text dy=".3em" text-anchor="start" transform="rotate(180)translate(-50.5)rotate(-180)translate(2)">90°</text>
                <path class="angle" d="M-35.07402885326976,-20.250000000000014A40.5,40.5 0 0,1 35.074028853269766,-20.25L0,0Z" style="fill: rgb(255, 204, 0);"></path>
                <line class="angle" transform="rotate(30)" x2="-45.5"></line><line class="angle" transform="rotate(60)" x2="-40.5"></line>
                <line class="angle" transform="rotate(90)" x2="-40.5"></line><line class="angle" transform="rotate(120)" x2="-40.5"></line>
                <line class="angle" transform="rotate(150)" x2="-45.5"></line><path class="drag" d="M-9.5,0L-3,3.5L-3,-3.5Z" transform="rotate(30)translate(-40.5)"></path>
                <path class="drag" d="M-9.5,0L-3,3.5L-3,-3.5Z" transform="rotate(150)translate(-40.5)"></path></g></svg>
            </div>

            <hr style="clear: both">
    </form>
    <script src="https://www.jasondavies.com/d3.min.js"></script>
    <script src="https://www.jasondavies.com/wordcloud/cloud.min.js"></script>

<div class="sign-up">
<h3>워드카운트 Ranking</h3>
<%int i=1; %>
<div>
<c:forEach items="${list}" begin="0"  end="19" var="dto">
	<span style="font-size:<%=23-i%>pt;"><%=i %>위 : ${dto.word } - ${dto.count} 개</span><br/>
	<%i++; %>
</c:forEach>
</div>
</div>
</body>
<script>
    //컨트롤 판넬
    document.getElementById("control").addEventListener("click", function() {
            var styleText = document.getElementById("ctlView").style["display"];
            if (styleText == "none")
                document.getElementById("ctlView").style["display"] = "block";
            else
                document.getElementById("ctlView").style["display"] = "none";

        })
    
    var init = function() {
        w = parseInt(0.8 * window.innerWidth);
        h = parseInt(0.5 * window.innerHeight);
        document.getElementById("vis").style.width = w;
        document.getElementById("vis").style.height = h;
        document.getElementById("vis").firstChild.width.baseVal.value = w;
        document.getElementById("vis").firstChild.height.baseVal.value = h;
      layout = d3.layout.cloud().timeInterval(10).size([w, h]).fontSize(function(t) { return fontSize(+t.value) }).text(function(t) { return t.key }).on("word", progress).on("end", draw)

      var mouseEvent = document.createEvent('MouseEvents');
        mouseEvent.initEvent("click", true, false);
        document.getElementById("gen").dispatchEvent(mouseEvent);

    }
        //윈도우 사이즈 변경시
    window.onresize = init;
    window.onload = init;
</script>
</html>