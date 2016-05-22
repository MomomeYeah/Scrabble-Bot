var jsonText = document.getElementById("topScoreValue").innerHTML;

if (jsonText.trim() != "null") {
    var json = JSON.parse(jsonText);

    var boardDiv = document.getElementById("board");

    var move = json.move;
    var board = json.board;

    var score = move.score;
    document.getElementById("topMoveScore").innerHTML = score;

    //console.log(JSON.stringify(move));
    var placements = move.placements;
    var placement = placements[0];
    var placementNum = 0;

    var cell;
    for (var i = 0; i < 15; i++) {
        var boardLine = "";
        for (var j = 0; j < 15; j++) {
            cell = board[(i * 15) + j];
            //console.log(JSON.stringify(cell));
            if (placement.row == i && placement.column == j) {
                boardLine += placement.tile.letter + "*";
                if (placementNum < placements.length - 1) {
                    placement = placements[++placementNum];
                }
            } else if (cell.cell != undefined) {
                var cellType = cell.cell;
                //console.log(cellType);
                boardLine += cellType;
            } else {
                //console.log(cell.tile.letter);
                boardLine += cell.tile.letter + " ";
            }
        }
        boardDiv.innerHTML += boardLine + "<br/>";
    }
} else {
    //console.log("No move found");
}
