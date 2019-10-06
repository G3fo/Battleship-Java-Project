var app = new Vue({
	el: "#app",
	data: {
		vueScores: []
	}
})

Vue.config.devtools = true;



$.get("/api/leaderboard")
	.done(function (scores) {
		app.vueScores = scores;
		console.log(app.vueScores);
	})

	.fail(function (jqXHR, textStatus) {
		showOutput("Failed: " + textStatus);
	});




