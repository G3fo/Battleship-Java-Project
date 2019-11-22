var gamesJSON = [];
var scoresJSON = [];

var app = new Vue({
  el: "#app",
  data: {
    vueScores: scoresJSON,
    vueGames: gamesJSON
  },
  methods: {
    returnToGame(id) {
      window.location.href = "http://localhost:8080/web/game.html?gp=" + id;
    },
    joinGame(gameid) {
      fetch("/api/games/" + gameid + "/players", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "same-origin"
      })
        .then(function(json) {
          window.location.href =
            "http://localhost:8080/web/game.html?gp=" + json.gpid;
        })
        .catch(function(error) {
          console.log(error);
          alert("Could not join game!");
        });

      //   $.post("/api/game/" + gameid + "/players")
      //     .done(function(json) {
      //       window.location.href =
      //         "http://localhost:8080/web/game.html?gp=" + json.gpid;
      //     })
      //     .fail(alert("Could not join game!"));
    }
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

function login() {
  $.post("/api/login", {
    username: document.getElementById("loginEmail").value,
    password: document.getElementById("loginPassword").value
  })
    .done(function() {
      alert("Logged in!");
      window.location.reload();
    })
    .fail(function() {
      alert("Incorrect username or password");
    });
}

function signup() {
  $.post("/api/players", {
    username: document.getElementById("loginEmail").value,
    password: document.getElementById("loginPassword").value
  })
    .done(function() {
      alert("Signed up!");
      login();
    })
    .fail(function() {
      alert("Incorrect sign up!");
    });
}

function createGame() {
  $.post("/api/games")
    .done(function(json) {
      window.location.href = "game.html?gp=" + json.gpid;
    })
    .fail(function() {
      alert("Couldnt create game!");
    });
}
