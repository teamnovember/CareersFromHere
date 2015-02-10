var videoPlayerId = "video-player";
var playPauseId = "play-pause-button";
var prevId = "prev-section-button";
var nextId = "next-section-button";
var overlayId = "overlay";
var underlayId = "underlay";

var totalDurationAttr = "data-total-duration";
var pathsAttr = "data-paths";
var questionsAttr = "data-questions";

var playGlyphClass = "glyphicon-play";
var pauseGlyphClass = "glyphicon-pause";

var videoPlayer;
var overlay;
var overlayDelay;
var underlay;
var paths;
var questions;
var totalDuration;
var index;
var numOfVideos;
var isPaused;
var noOverlay;

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
    switchToVideo();
};

function prev() {
    var toLoadIndex = index - 1 > 0 ? index - 1 : 0;
    if (toLoadIndex != index) {
        index = toLoadIndex;
        switchToVideo();
    }
};

function next() {
    var toLoadIndex = index + 1 < numOfVideos - 1 ? index + 1 : numOfVideos - 1;
    if (toLoadIndex != index) {
        index = toLoadIndex;
        switchToVideo();
    } else if (index == numOfVideos - 1) stop();

}

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

    totalDuration = videoPlayer.getAttribute(totalDurationAttr);

    index = 0;

    numOfVideos = paths.length;

    isPaused = true;
    noOverlay = false;

    switchToVideo();
};

document.addEventListener("DOMContentLoaded", function() { initVideoPlayer(); }, false);