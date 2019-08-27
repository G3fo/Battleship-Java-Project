var gamesJSON = [];

var app = new Vue({
  el: "#app",
  data: {
    vueGames: gamesJSON
  }
});

$.get("/api/games")
  .done(function(games) {
    gamesJSON = games;
    console.log(gamesJSON);
    app.vueGames = games;
  })
  .fail(function(jqXHR, textStatus) {
    showOutput("Failed: " + textStatus);
  });
