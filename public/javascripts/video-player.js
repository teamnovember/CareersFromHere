var videoPlayerId = "video-player";
var playPauseId = "play-pause-button";
var stopId = "stop-button";
var volumeUpId = "volume-up-button";
var volumeDownId = "volume-down-button";
var progressBarId = "progress-bar";


var playClass = "play";
var pauseClass = "pause";
var stopClass = "stop";
var volumeUpClass = "volume-up";
var volumeDownClass = "volume-down";

var playGlyphClass = "glyphicon-play";
var pauseGlyphClass = "glyphicon-pause";

var videoPlayer;

function playPauseButton() {
    var button = document.getElementById(playPauseId);

    if (videoPlayer.paused) {
        button.class = pauseClass;
        button.getElementsByClassName(pauseGlyphClass)[0].style.display = "inline";
        button.getElementsByClassName(playGlyphClass)[0].style.display = "none";
        videoPlayer.play();
    } else {
        button.class = playClass;
        button.getElementsByClassName(playGlyphClass)[0].style.display = "inline";
        button.getElementsByClassName(pauseGlyphClass)[0].style.display = "none";
        videoPlayer.pause();
    }
};

function stopButton() {
    videoPlayer.pause();

    resetPlayer();
};

function volume(direction) {
    var value = videoPlayer.volume;
    if (direction == "up") value += 0.1;
    else
    if (direction == "down") value -= 0.1;

    if (0 > value) value = 0;
    else
    if (value > 1) value = 1;

    videoPlayer.volume = value;
};

function updateProgressBar() {
    var progressBar = document.getElementById(progressBarId);
    var value = Math.floor(100 * videoPlayer.currentTime / videoPlayer.duration);
    progressBar.value = value;
};

function resetPlayer() {
    videoPlayer.currentTime = 0;

    var button = document.getElementById(playPauseId);
    button.getElementsByClassName(playGlyphClass)[0].style.display = "inline";
    button.getElementsByClassName(pauseGlyphClass)[0].style.display = "none";
}

function initialiseVideoPlayer() {
    videoPlayer = document.getElementById(videoPlayerId);
    videoPlayer.controls = false;

    videoPlayer.addEventListener("timeupdate", updateProgressBar, false);
    videoPlayer.addEventListener("ended", resetPlayer, false);
};

document.addEventListener("DOMContentLoaded", function() { initialiseVideoPlayer(); }, false);
