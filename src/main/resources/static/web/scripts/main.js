var gpId = window.location.search.match(/\d+/g).map(Number);
var player;
var opponent;

var defaultShipsArray = [{
    shipType: "CARRIER",
    locations: ["A1", "A2", "A3", "A4", "A5"]
  },
  {
    shipType: "BATTLESHIP",
    locations: ["C1", "C2", "C3", "C4"]
  },
  {
    shipType: "SUBMARINE",
    locations: ["E1", "E2", "E3"]
  },
  {
    shipType: "DESTROYER",
    locations: ["G1", "G2", "G3"]
  },
  {
    shipType: "PATROL_BOAT",
    locations: ["I1", "I2"]
  }
];

loadGamesJSON();

function loadGamesJSON() {
  fetch("/api/game_view/" + gpId)
    .then(function (response) {
      return response.json();
    })
    .then(function (json) {
      gamesJSON = json;

      if (gamesJSON.ships.length > 0) {
        //Sino, en modo estatico, y no puede mover los ships
        document.getElementById("POSTships").style.display = "none";
        document.getElementById("disableGrid").style.display = "none";
        loadGrid(true);
        createGrid(11, $(".grid-salvoes"), "salvoes");
        setSalvoes();
        salvoesClick();
      } else {
        //Si el jugador no puso ships previamente, carga la grilla en modo dinamico y los ships default
        document.getElementById("POSTsalvoes").style.display = "none";
        document.getElementById("disableGrid").style.display = "block";
        loadGrid(false);
        defaultShips();
        createGrid(11, $(".grid-salvoes"), "salvoes");
      }

      for (let i = 0; i < gamesJSON.players.length; i++) {
        if (gamesJSON.players[i].gpid == gpId) {
          player = gamesJSON.players[i].gpid;
        } else {
          opponent = gamesJSON.players[i].gpid;
        }
      }

      if (gamesJSON.salvoes.length > 0) {
        setSalvoes();
      }
    })
    .catch(function (error) {
      console.log(error);
    });
}

function reload() {
  fetch("/api/game_view/" + gpId)
    .then(function (response) {
      return response.json();
    })
    .then(function (json) {
        gamesJSON = json;
        setSalvoes()
    })
};


function defaultShips() {
  for (var i = 0; i < defaultShipsArray.length; i++) {
    let shipType = defaultShipsArray[i].shipType.toLowerCase();
    let x = +defaultShipsArray[i].locations[0][1] - 1;
    let y = stringToInt(defaultShipsArray[i].locations[0][0].toUpperCase()); //Transformo la string a numero para ubicarla en el axis Y

    var w = defaultShipsArray[i].locations.length;
    var h = 1;
    var orientation = "Horizontal";

    grid.addWidget(
      $(
        '<div id="' +
        shipType +
        '"><div class="grid-stack-item-content ' +
        shipType +
        orientation +
        '"></div><div/>'
      ),
      x,
      y,
      w,
      h
    );
  }

  //Activo los listeners para la rotacion de los barcos
  rotateShips("carrier", 5);
  rotateShips("battleship", 4);
  rotateShips("submarine", 3);
  rotateShips("destroyer", 3);
  rotateShips("patrol_boat", 2);
}

function shipPOST() {
  let shipTypeArray = [
    "battleship",
    "patrol_boat",
    "destroyer",
    "submarine",
    "carrier"
  ];

  let shipArray = [];

  for (let i = 0; i < shipTypeArray.length; i++) {
    let ship = document.querySelector("#" + shipTypeArray[i]);

    let width = ship.getAttribute("data-gs-width");
    let height = ship.getAttribute("data-gs-height");
    let x = parseInt(ship.getAttribute("data-gs-x"));
    let y = parseInt(ship.getAttribute("data-gs-y"));
    let locations = [];

    if (height == 1) {
      for (let j = 1; j <= width; j++) {
        let currentX = x + j -1;
        let yLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(y);
        locations.push(yLetter + currentX);
      }
    } else if (width == 1) {
      for (let j = 0; j < height; j++) {
        let currentY = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(y + j);
        let xPlusOne = x;
        locations.push(currentY + xPlusOne);
      }
    }
    shipArray.push({
      shipType: shipTypeArray[i].toUpperCase(),
      locations: locations
    });
  }

  fetch("/api/games/players/" + gpId + "/ships", {
      method: "POST",
      body: JSON.stringify(shipArray),
      headers: {
        "Content-Type": "application/json"
      },
      credentials: "same-origin"
    })
    .then(function (response) {
      response.status;
      if (response.status == 201) {
        console.log("Ships Fetched");
      } else {
        console.log("invalid");
      }
    })
    .then(function () {
      reload()
    })
    .catch(function (error) {
      console.log(error);
    });
}

var counter = 0;
salvo_cell = [];

function salvoesClick() {
  for (let i = 0; i < 10; i++) {
    for (let j = 0; j < 10; j++) {
      salvo_cell.push(document.getElementById("salvoes" + i + j));
    }
  }

  for (var i = 0; i < salvo_cell.length; i++) {
    salvo_cell[i].addEventListener("click", function (e) {
      e.preventDefault();
      var cellId = e.target.id;
      var cellShot = document.getElementById(cellId);

      if (cellShot.classList.contains("toBeSalvo")) {
        cellShot = document
          .getElementById(cellId)
          .classList.remove("toBeSalvo");
        counter--;
      } else if (!cellShot.classList.contains("toBeSalvo") && counter < 5) {
        cellShot = document.getElementById(cellId).classList.add("toBeSalvo");
        counter++;
      }
    });
  }
}

function salvoesPOST() {
  salvoLocations = [];
  let salvoes = document.querySelectorAll(".toBeSalvo");
  for (let i = 0; i < salvoes.length; i++) {
    var salvoId = salvoes[i].id.slice(-2);
    let x = +salvoId[1];
    let y = intToString(parseInt(salvoId[0]));
    salvoLocations.push(y + x);
  }

  fetch("/api/games/players/" + gpId + "/salvoes", {
      method: "POST",
      body: JSON.stringify(salvoLocations),
      headers: {
        "Content-Type": "application/json"
      },
      credentials: "same-origin"
    })
    .then(function (response) {
      response.status;
      if (response.status == 201) {
        console.log("Salvoes Fetched");
        reload()
      } else {
        console.log("Invalid");
        swal("Whoops", "You need to place 5 salvoes!", "error").then(
          function () {
            reload()
          }
        );
      }
    })
    .then(function () {
      toBeSalvo = document.querySelectorAll(".toBeSalvo");
      for (var i = 0; i < toBeSalvo.length; i++) {
        toBeSalvo[i].classList.add("sentSalvo");
        toBeSalvo[i].classList.remove("toBeSalvo");
      }
    })
    .then(function () {
      counter = 0;
    })
    .catch(function (error) {
      console.log(error);
    });
}

const stringToInt = function (str) {
  switch (str) {
    case "A":
      return 0;
    case "B":
      return 1;
    case "C":
      return 2;
    case "D":
      return 3;
    case "E":
      return 4;
    case "F":
      return 5;
    case "G":
      return 6;
    case "H":
      return 7;
    case "I":
      return 8;
    case "J":
      return 9;
  }
};