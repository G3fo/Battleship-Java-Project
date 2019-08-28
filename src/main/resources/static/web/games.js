var gamesJSON = [];

var app = new Vue({
  el: "#app",
  data: {
    vueGames: gamesJSON
  }
});

//Idealmente quiero usar fetch en vez del metodo get de jquery, lo dejo para más adelante

// fetch("http://localhost:8080/api/games", { method: "get" })
//   .then(function(games) {
//     gamesJSON = games;
//   })
//   .then(function(games) {
//     console.log(games);
//   });

$.get("/api/games")
  .done(function(games) {
    gamesJSON = games;
    app.vueGames = games;
    //console.log(gamesJSON);
  })
  .fail(function(jqXHR, textStatus) {
    showOutput("Failed: " + textStatus);
  });
