var videoPlayerId = "video-player";
var playPauseId = "play-pause-button";
var prevId = "prev-section-button";
var nextId = "next-section-button";
var overlayId = "overlay";
var underlayId = "underlay";

var progressBarClass = "progress-bar";

var totalDurationAttr = "data-total-duration";
var pathsAttr = "data-paths";
var questionsAttr = "data-questions";
var durationsAttr = "data-durations";

var playGlyphClass = "glyphicon-play";
var pauseGlyphClass = "glyphicon-pause";

var videoPlayer;
var overlay;
var overlayDelay;
var underlay;
var paths;
var questions;
var durations;
var totalDuration;
var index;
var numOfVideos;
var volume;
var isPaused;
var noOverlay;
var passedTime;

function playPause() {
    var button = document.getElementById(playPauseId);

    if (noOverlay) {
        if (isPaused) {
            button.getElementsByClassName(pauseGlyphClass)[0].style.display = "inline";
            button.getElementsByClassName(playGlyphClass)[0].style.display = "none";

            videoPlayer.play();
            isPaused = false;
        } else {
            button.getElementsByClassName(playGlyphClass)[0].style.display = "inline";
            button.getElementsByClassName(pauseGlyphClass)[0].style.display = "none";

            videoPlayer.pause();
            isPaused = true;
        }
    } else {
        if (isPaused) {
            button.getElementsByClassName(pauseGlyphClass)[0].style.display = "inline";
            button.getElementsByClassName(playGlyphClass)[0].style.display = "none";

            overlay.style.display = "none";
            underlay.style.display = "inline-block";
            noOverlay = false;

            videoPlayer.play();
            isPaused = false;
        } else {
            button.getElementsByClassName(playGlyphClass)[0].style.display = "inline";
            button.getElementsByClassName(pauseGlyphClass)[0].style.display = "none";

            videoPlayer.pause();
            isPaused = true;
        }
    }
};

function stop() {
    var button = document.getElementById(playPauseId);
    button.getElementsByClassName(playGlyphClass)[0].style.display = "inline";
    button.getElementsByClassName(pauseGlyphClass)[0].style.display = "none";

    isPaused = true;
    index = 0;
    passedTime = 0;
    updateProgressBar(true);

    switchToVideo();
};

function prev() {
    var toLoadIndex = index - 1 > 0 ? index - 1 : 0;
    if (toLoadIndex != index) {
        index = toLoadIndex;

        passedTime -= durations[index];
        updateProgressBar(true);

        switchToVideo();
    }
};

function next() {
    var toLoadIndex = index + 1 < numOfVideos - 1 ? index + 1 : numOfVideos - 1;
    if (toLoadIndex != index) {
        passedTime += durations[index];
        updateProgressBar(true);

        index = toLoadIndex;
        switchToVideo();
    } else if (index == numOfVideos - 1) {
        stop();
    }

};

function volumeChanged(direction) {
    if (direction == "up") volume += 0.1;
    else
    if (direction == "down") volume -= 0.1;

    if (0 > volume) volume = 0;
    else
    if (volume > 1) volume = 1;

    videoPlayer.volume = volume;
};

function switchToVideo() {
    videoPlayer.pause();

    underlay.style.display = "none";
    underlay.innerHTML = questions[index];

    overlay.innerHTML = questions[index];
    overlay.style.display = "inline";
    noOverlay = false;

    videoPlayer.src = paths[index];
    videoPlayer.load();

    if (index == 0) {
        document.getElementById(prevId).disabled = true;
    } else {
        document.getElementById(prevId).disabled = false;
    }

    if (index == numOfVideos - 1) {
        document.getElementById(nextId).disabled = true;
    } else {
        document.getElementById(nextId).disabled = false;
    }
};

function updateProgressBar(noTimeUpdate) {
    var percentage;
    if (noTimeUpdate == true) percentage = (passedTime) / totalDuration * 100;
    else percentage = (passedTime + videoPlayer.currentTime) / totalDuration * 100;
    percentage = percentage.toString() + "%";

    document.getElementsByClassName(progressBarClass)[0].style.width = percentage;
};

function loadComplete() {
    if (!isPaused) {
        setTimeout(function() {
            if (isPaused) return;

            overlay.style.display = "none";
            underlay.style.display = "inline-block";
            noOverlay = true;

            videoPlayer.play();
        }, overlayDelay);
    }
};

function initVideoPlayer() {
    videoPlayer = document.getElementById(videoPlayerId);
    videoPlayer.addEventListener("timeupdate", updateProgressBar, false);
    videoPlayer.addEventListener("ended", next, false);
    videoPlayer.addEventListener("loadeddata", loadComplete, false);

    overlay = document.getElementById(overlayId);
    overlayDelay = 2000;

    underlay = document.getElementById(underlayId);
    underlay.style.display = "none";

    paths = videoPlayer.getAttribute(pathsAttr);
    paths = JSON.parse(paths);

    questions = videoPlayer.getAttribute(questionsAttr);
    questions = JSON.parse(questions);

    durations = videoPlayer.getAttribute(durationsAttr);
    durations = JSON.parse(durations);

    totalDuration = videoPlayer.getAttribute(totalDurationAttr);

    index = 0;

    numOfVideos = paths.length;

    volume = videoPlayer.volume;

    isPaused = true;
    noOverlay = false;
    passedTime = 0;

    switchToVideo();
};

document.addEventListener("DOMContentLoaded", function() { initVideoPlayer(); }, false);
