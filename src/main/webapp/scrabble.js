var jsonText = document.getElementById("topScoreValue").innerHTML;

if (jsonText.trim() != "null") {
    var json = JSON.parse(jsonText);

    var showTopScore = document.getElementById("show-top-score");
    var modalContainer = document.getElementById("modal-container");
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

            var span = document.createElement("span");
            span.className = 'board-part';
            //console.log(JSON.stringify(cell));
            if (placement.row == i && placement.column == j) {
                span.className += ' placement';
                span.innerHTML = placement.tile.letter;
                if (placementNum < placements.length - 1) {
                    placement = placements[++placementNum];
                }
            } else if (cell.cell != undefined) {
                var cellType = cell.cell;
                //console.log(cellType);
                switch (cellType) {
                    case 'TL':  span.className += ' cellTL';
                                span.innerHTML = cellType;
                                break;
                    case 'DL':  span.className += ' cellDL';
                                span.innerHTML = cellType;
                                break;
                    case 'TW':  span.className += ' cellTW';
                                span.innerHTML = cellType;
                                break;
                    case 'DW':  span.className += ' cellDW';
                                span.innerHTML = cellType;
                                break;
                    default:    span.className += ' cell';
                                span.innerHTML = '&nbsp;'
                                break;
                }
            } else {
                //console.log(cell.tile.letter);
                span.className += ' tile';
                span.innerHTML = cell.tile.letter;
            }

            boardDiv.appendChild(span);
        }
        var br = document.createElement("br");
        boardDiv.appendChild(br);
    }

    showTopScore.onclick = function() {
        modalContainer.style.display = "block";
    }

    boardDiv.onclick = function() {
        modalContainer.style.display = "none";
    }
} else {
    //console.log("No move found");
}
