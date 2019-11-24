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
      $.post("/api/game/" + gameid + "/players")
        .done(function(json) {
          window.location.href =
            "http://localhost:8080/web/game.html?gp=" + json.gpid;
        })
        .fail(function(jqXHR, textStatus) {
          showOutput("Failed: " + textStatus);
          alert("Could not join the game!");
        });
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
    .done(function(username) {
      (username = document.getElementById("loginEmail").value),
        swal("Logged in!", "Welcome back " + username + "!", "success").then(
          function() {
            window.location.reload();
          }
        );
    })
    .fail(function() {
      swal("Whoops", "Incorrect username or password", "error");
    });
}

function signup() {
  $.post("/api/players", {
    username: document.getElementById("loginEmail").value,
    password: document.getElementById("loginPassword").value
  })
    .done(function() {
      (username = document.getElementById("loginEmail").value),
        swal("Signed up!", "").then(function() {
          login();
        });
    })
    .fail(function() {
      swal("Whoops", "Incorrect sign up!", "error");
    });
}

function createGame() {
  $.post("/api/games")
    .done(function(json) {
      window.location.href = "game.html?gp=" + json.gpid;
    })
    .fail(function() {
      swal("Whoops", "Couldn't create game!", "error");
    });
}
