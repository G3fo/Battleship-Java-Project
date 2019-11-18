function reload() {}

function shipPOST() {
  let shipTypeArray = [
    "battleship",
    "patrol_boat",
    "destroyer",
    "submarine",
    "carrier"
  ];

  let shipArray = [];
  let gpId = window.location.search.match(/\d+/g).map(Number);

  for (let i = 0; i < shipTypeArray.length; i++) {
    let ship = document.querySelector("#" + shipTypeArray[i]);

    let width = ship.getAttribute("data-gs-width");
    let height = ship.getAttribute("data-gs-height");
    let x = parseInt(ship.getAttribute("data-gs-x"));
    let y = parseInt(ship.getAttribute("data-gs-y"));
    let locations = [];

    if (height == 1) {
      for (let j = 1; j <= width; j++) {
        let currentX = x + j;
        let yLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(y);
        locations.push(yLetter + currentX);
      }
    } else if (width == 1) {
      for (let j = 0; j < height; j++) {
        let currentY = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(y + j);
        let xPlusOne = x + 1;
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
    headers: { "Content-Type": "application/json" },
    credentials: "same-origin"
  })
    .then(function(response) {
      response.status;
      if (response.status == 201) {
        console.log("Ships Fetched");
      } else {
        console.log("invalid");
      }
    })
    .catch(function(error) {
      console.log(error);
    });
}
