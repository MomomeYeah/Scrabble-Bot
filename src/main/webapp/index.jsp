<!DOCTYPE html>
<%@ page import="game.Board" %>
<html>
    <head>
        <title>Scrabbulator 4000</title>

        <link rel="stylesheet" type="text/css" href="scrabble.css">
    </head>
    <body>
        <div class="outer-panel">
            <div class="inner-panel">
                <div class="main-section">
                    <h1>Scrabbulator 4000</h1>
                    <p>This is the home page of the incredible Scrabble-playing robot.  His name is Scrabbulator 4000 and he shows no mercy.</p>
                    <p>Requests from <a href="https://www.merknera.com/" target="_blank">Merknera</a> are POSTed to /bot, and Scrabbulator 4000 responds.</p>
                    <p style="width: 100px;">
                        <%
                        Board b = new Board();
                        %>
                        <%=
                        b.toString()
                        %>
                    </p>
                </div>
                <div class="footer-section">
                    <p class="footer">Made by <a href="https://github.com/MomomeYeah" target="_blank">Rob Gwynn-Jones</a></p>
                </div>
            </div>
        </div>
    </body>
</html>
