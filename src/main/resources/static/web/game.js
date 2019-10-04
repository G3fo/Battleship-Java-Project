// $(function() {
//     loadData();
// });

// function getParameterByName(name) {
//     var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
//     return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
// };

var recievedData;

// function loadData(){
//     $.get('/api/game_view/'+getParameterByName('gp'))
//         .done(function(data) {
//             recievedData = data;
//             console.log(data);
//             let playerInfo;
//             if(recievedData.gamePlayers[0].id == getParameterByName('gp'))
//                 playerInfo = [recievedData.gamePlayers[0].Player.user,recievedData.gamePlayers[1].Player.user];
//             else
//                 playerInfo = [recievedData.gamePlayers[1].Player.user,recievedData.gamePlayers[0].Player.user];

//             $('#playerInfo').text(playerInfo[0] + '(you) vs ' + playerInfo[1]);

//             data.ships.forEach(function(shipPiece){
//                 shipPiece.locations.forEach(function(shipLocation){
//                     $('#'+shipLocation).addClass('ship-piece');
//                 })
//             });
//         })
//         .fail(function( jqXHR, textStatus ) {
//           alert( "Failed: " + textStatus );
//         });
// };

$(function() {
    loadData();
});

function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
};

function loadData(){
    $.get('/api/game_view/'+getParameterByName('gp'))
        .done(function(data) {
            console.log(data)
            recievedData = data;
            let playerInfo;
            if(recievedData.gamePlayers[0].id == getParameterByName('gp')){
                playerInfo1 = [recievedData.gamePlayers[0].Player.user,recievedData.gamePlayers[1].Player.user];
            }

            else{
                playerInfo = [recievedData.gamePlayers[1].Player.user,recievedData.gamePlayers[0].Player.user];
            }

            $('#player1Info').text(playerInfo[0] + '(you)');
            $('#player2Info').text( playerInfo[1]);

            recievedData.ships.forEach(function (shipPiece) {
                    shipPiece.locations.forEach(function (shipLocation) {
                      let turnHitted = isHit(shipLocation,recievedData.salvoes,playerInfo[0].id)
                      if(turnHitted >0){
                        $('#B_' + shipLocation).addClass('ship-piece-hitted');
                        $('#B_' + shipLocation).text(turnHitted);
                      }
                      else
                        $('#B_' + shipLocation).addClass('ship-piece');
                    });
                  });
                  recievedData.salvoes.sort().forEach(function (salvo) {
                    console.log(salvo);
                    if (playerInfo[0].id === salvo.player) {
                      salvo.locations.forEach(function (location) {
                        $('#S_' + location ).addClass('salvo');
                      });
                    } else {
                      salvo.locations.forEach(function (location) {
                        $('#B_' + location).addClass('salvo');
                      });
                    }
                  });
        })
        .fail(function( jqXHR, textStatus ) {
          alert( "Failed: " + textStatus );
        });
    }

function isHit(shipLocation,salvoes,playerId) {
  var hit = 0;
  salvoes.forEach(function (salvo) {
    if(salvo.player != playerId)
      salvo.locations.forEach(function (location) {
        if(shipLocation === location)
          hit = salvo.turn;
      });
  });
  return hit;
}
