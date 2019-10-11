var gamesJSON = [];
var scoresJSON = [];

var app = new Vue({
  el: "#app",
  data: {
    vueScores: scoresJSON,
    vueGames: gamesJSON
  }
});
Vue.config.devtools = true;

$.get("/api/games")
  .done(function(games) {
    gamesJSON = games;
    var date;
    gamesJSON.games.forEach(game => {
      date = new Date(game.created);
      game.created = date.toLocaleString();
    });

    app.vueGames = gamesJSON;
  })
  .fail(function(jqXHR, textStatus) {
    showOutput("Failed: " + textStatus);
  });
$.get("/api/leaderboard")
  .done(function(scores) {
    scores.sort((a, b) => b["total"] - a["total"]);
    app.vueScores = scores;
    console.log(app.vueScores);
  })
  .fail(function(jqXHR, textStatus) {
    showOutput("Failed: " + textStatus);
  });

function logout() {
  $.post("/api/logout").done(function() {
    console.log("logged out");
  });
}

function logIn() {
  $.post("/api/login", {
    username: document.getElementById("loginEmail").value,
    password: document.getElementById("loginPassword").value
  }).done(function() {
    console.log("logged in!");
  });
}

function signUp() {
  $.post("/api/players", {
    username: document.getElementById("loginEmail").value,
    password: document.getElementById("loginPassword").value
  }).done(function() {
    console.log("Signed up");
  });
}
