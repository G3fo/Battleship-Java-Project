var recievedData;

$(function () {
  loadData();
});

function getParameterByName(name) {
  var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
};

function loadData() {
  $.get('/api/game_view/' + getParameterByName('gp'))
    .done(function (data) {
      console.log(data)
      recievedData = data;
      let playerInfo;
      if (recievedData.players[0].id == getParameterByName('gp')) {
        playerInfo1 = [recievedData.players[0].user, recievedData.players[1].user];
      } else {
        playerInfo = [recievedData.players[1].user, recievedData.players[0].user];
      }

      $('#player1Info').text(playerInfo[0] + '(you)');
      $('#player2Info').text(playerInfo[1]);

      recievedData.ships.forEach(function (shipPiece) {
        shipPiece.locations.forEach(function (shipLocation) {
          let turnHitted = isHit(shipLocation, recievedData.salvoes, playerInfo[0].id)
          if (turnHitted > 0) {
            $('#B_' + shipLocation).addClass('ship-piece-hitted');
            $('#B_' + shipLocation).text(turnHitted);
          } else
            $('#B_' + shipLocation).addClass('ship-piece');
        });
      });
      recievedData.salvoes.sort().forEach(function (salvo) {
        console.log(salvo);
        if (playerInfo[0].id === salvo.player) {
          salvo.locations.forEach(function (location) {
            $('#S_' + location).addClass('salvo');
          });
        } else {
          salvo.locations.forEach(function (location) {
            $('#B_' + location).addClass('salvo');
          });
        }
      });
    })
    .fail(function (jqXHR, textStatus) {
      alert("You need to login before you enter a game!");
      window.location.href = "http://localhost:8080/web/games.html"
    });
}

function isHit(shipLocation, salvoes, playerId) {
  var hit = 0;
  salvoes.forEach(function (salvo) {
    if (salvo.player != playerId)
      salvo.locations.forEach(function (location) {
        if (shipLocation === location)
          hit = salvo.turn;
      });
  });
  return hit;
}