<!DOCTYPE html>
<%@ page import="db.TopScores" %>
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
                    <%
                    TopScores ts = new TopScores();
                    String topScoreKey = ts.getTopScore();
                    String topScoreValue = null;
                    if (topScoreKey != null) {
                        topScoreValue = ts.get(topScoreKey);
                    }
                    %>
                    <p id="topScoreValue" style="display: none;">
                        <%= topScoreValue %>
                    </p>
                    <p>
                        <% if (topScoreValue != null) { %>
                            Shown below is the highest scoring move so far, worth <span id="topMoveScore"></span> points.</p>
                        <% } else { %>
                            No moves have been made yet.  Watch this space!
                        <% } %>
                    </p>
                    <div id="board" class="board"></div>
                </div>
                <div class="footer-section">
                    <p class="footer">Made by <a href="https://github.com/MomomeYeah" target="_blank">Rob Gwynn-Jones</a></p>
                </div>
            </div>
        </div>
        <script src="scrabble.js"></script>
    </body>
</html>
